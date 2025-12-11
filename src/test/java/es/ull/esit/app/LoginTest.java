package es.ull.esit.app;

import es.ull.esit.app.middleware.model.User;
import es.ull.esit.app.middleware.service.AuthService;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
            

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Swing Login window focusing on UI initialization and the
 * login failure path. Uses reflection to inject a stub AuthService.
 */
public class LoginTest {

    @Test
    void testInitComponents_defaults() throws Exception {
        Login l = new Login();

        // Access private fields
        Field btnField = Login.class.getDeclaredField("jButton1");
        btnField.setAccessible(true);
        JButton btn = (JButton) btnField.get(l);

        Field comboField = Login.class.getDeclaredField("usertypecmbo");
        comboField.setAccessible(true);
        JComboBox<?> combo = (JComboBox<?>) comboField.get(l);

        Field userField = Login.class.getDeclaredField("usernametxt");
        userField.setAccessible(true);
        JTextField usertxt = (JTextField) userField.get(l);

        Field passField = Login.class.getDeclaredField("jPasswordField1");
        passField.setAccessible(true);
        JPasswordField pass = (JPasswordField) passField.get(l);

        // Basic assertions about initialized components
        assertEquals("Login", l.getTitle());
        assertEquals("Log in", btn.getText());
        assertFalse(btn.isEnabled()==false); // button should be enabled (not explicitly disabled)
        assertEquals(2, combo.getItemCount());
        assertEquals("admin", combo.getItemAt(0));
        assertEquals("cashier", combo.getItemAt(1));

        assertEquals("", usertxt.getText());
        assertEquals(0, pass.getPassword().length);

        // clean up
        l.dispose();
    }

    @Test
    void testLogin_failure_restoresButton() throws Exception {
        Login l = new Login();

        // Inject stub AuthService that throws an exception
        AuthService stub = new AuthService(null) {
            @Override
            public User authenticate(String username, String password) {
                throw new RuntimeException("boom");
            }
        };

        Field authField = Login.class.getDeclaredField("authService");
        authField.setAccessible(true);
        // Remove final by setting accessible and then set
        authField.set(l, stub);

        // Set username/password values
        Field userField = Login.class.getDeclaredField("usernametxt");
        userField.setAccessible(true);
        JTextField usertxt = (JTextField) userField.get(l);
        usertxt.setText("u");

        Field passField = Login.class.getDeclaredField("jPasswordField1");
        passField.setAccessible(true);
        JPasswordField pass = (JPasswordField) passField.get(l);
        pass.setText("p");

        Field btnField = Login.class.getDeclaredField("jButton1");
        btnField.setAccessible(true);
        JButton btn = (JButton) btnField.get(l);

        // Invoke the private action handler
        Method m = Login.class.getDeclaredMethod("jButton1ActionPerformed", java.awt.event.ActionEvent.class);
        m.setAccessible(true);

        // Call action - it spawns a background thread. Immediately the button should be disabled and show Loading...
        m.invoke(l, (Object) null);

        // Immediately after invocation the button should be disabled and show Loading...
        assertFalse(btn.isEnabled());
        assertEquals("Loading...", btn.getText());

        l.dispose();
    }
}
