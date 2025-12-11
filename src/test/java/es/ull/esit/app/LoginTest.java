package es.ull.esit.app;

import es.ull.esit.app.middleware.model.User;
import es.ull.esit.app.middleware.service.AuthService;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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
        void testLogin_success_admin_opensAdmin() throws Exception {
            Login l = new Login();

            // Stub AuthService to return admin user
            AuthService stub = new AuthService(null) {
                @Override
                public User authenticate(String username, String password) {
                    return new User(username, "ADMIN");
                }
            };

            Field authField = Login.class.getDeclaredField("authService");
            authField.setAccessible(true);
            authField.set(l, stub);

            AtomicBoolean adminShown = new AtomicBoolean(false);
            l.setAdminSupplier(() -> new javax.swing.JFrame() {
                @Override
                public void setVisible(boolean b) {
                    adminShown.set(b);
                }
            });

            // set credentials
            Field userField = Login.class.getDeclaredField("usernametxt");
            userField.setAccessible(true);
            JTextField usertxt = (JTextField) userField.get(l);
            usertxt.setText("admin");

            Field passField = Login.class.getDeclaredField("jPasswordField1");
            passField.setAccessible(true);
            JPasswordField pass = (JPasswordField) passField.get(l);
            pass.setText("p");

            // button field not needed in this test

            Method m = Login.class.getDeclaredMethod("jButton1ActionPerformed", java.awt.event.ActionEvent.class);
            m.setAccessible(true);

            m.invoke(l, (Object) null);

            // Wait for EDT/background to run
            long start = System.currentTimeMillis();
            while (!adminShown.get() && System.currentTimeMillis() - start < 2000) {
                Thread.sleep(50);
                try { java.awt.EventQueue.invokeAndWait(() -> {}); } catch (Exception ignored) {}
            }

            assertTrue(adminShown.get(), "Admin frame should have been shown");
            l.dispose();
        }

        @Test
        void testLogin_success_cashier_opensCashierWithUsername() throws Exception {
            Login l = new Login();

            AuthService stub = new AuthService(null) {
                @Override
                public User authenticate(String username, String password) {
                    return new User(username, "CASHIER");
                }
            };

            Field authField = Login.class.getDeclaredField("authService");
            authField.setAccessible(true);
            authField.set(l, stub);

            AtomicBoolean cashierShown = new AtomicBoolean(false);
            AtomicReference<String> seenUsername = new AtomicReference<>();

            l.setCashierFactory((uname) -> new javax.swing.JFrame() {
                final String u = uname;
                @Override
                public void setVisible(boolean b) {
                    seenUsername.set(u);
                    cashierShown.set(b);
                }
            });

            // set credentials
            Field userField = Login.class.getDeclaredField("usernametxt");
            userField.setAccessible(true);
            JTextField usertxt = (JTextField) userField.get(l);
            usertxt.setText("cashierUser");

            Field passField = Login.class.getDeclaredField("jPasswordField1");
            passField.setAccessible(true);
            JPasswordField pass = (JPasswordField) passField.get(l);
            pass.setText("p");

            // button field not needed in this test

            Method m = Login.class.getDeclaredMethod("jButton1ActionPerformed", java.awt.event.ActionEvent.class);
            m.setAccessible(true);

            m.invoke(l, (Object) null);

            long start = System.currentTimeMillis();
            while (!cashierShown.get() && System.currentTimeMillis() - start < 2000) {
                Thread.sleep(50);
                try { java.awt.EventQueue.invokeAndWait(() -> {}); } catch (Exception ignored) {}
            }

            assertTrue(cashierShown.get(), "Cashier frame should have been shown");
            assertEquals("cashierUser", seenUsername.get());
            l.dispose();
        }

        @Test
        void testLogin_unknownRole_showsDialog_andRestoresButton() throws Exception {
            Login l = new Login();

            AuthService stub = new AuthService(null) {
                @Override
                public User authenticate(String username, String password) {
                    return new User(username, "GUEST");
                }
            };

            Field authField = Login.class.getDeclaredField("authService");
            authField.setAccessible(true);
            authField.set(l, stub);

            AtomicReference<String> msg = new AtomicReference<>();
            l.setMessageDialog(new Login.MessageDialog() {
                @Override
                public void showMessage(java.awt.Component parent, Object message) {
                    msg.set(String.valueOf(message));
                }

                @Override
                public void showMessage(java.awt.Component parent, Object message, String title, int messageType) {
                    msg.set(String.valueOf(message));
                }
            });

            // set credentials
            Field userField = Login.class.getDeclaredField("usernametxt");
            userField.setAccessible(true);
            JTextField usertxt = (JTextField) userField.get(l);
            usertxt.setText("guser");

            Field passField = Login.class.getDeclaredField("jPasswordField1");
            passField.setAccessible(true);
            JPasswordField pass = (JPasswordField) passField.get(l);
            pass.setText("p");

            Field btnField = Login.class.getDeclaredField("jButton1");
            btnField.setAccessible(true);
            JButton btn = (JButton) btnField.get(l);

            Method m = Login.class.getDeclaredMethod("jButton1ActionPerformed", java.awt.event.ActionEvent.class);
            m.setAccessible(true);

            m.invoke(l, (Object) null);

            long start = System.currentTimeMillis();
            while ((msg.get() == null || !btn.isEnabled() || !"Log in".equals(btn.getText())) && System.currentTimeMillis() - start < 2000) {
                Thread.sleep(50);
                try { java.awt.EventQueue.invokeAndWait(() -> {}); } catch (Exception ignored) {}
            }

            assertEquals("Unknown Role: GUEST", msg.get());
            assertTrue(btn.isEnabled());
            assertEquals("Log in", btn.getText());
            l.dispose();
        }

        @Test
        void testLogin_failure_showsErrorDialog_andRestoresButton() throws Exception {
            Login l = new Login();

            AuthService stub = new AuthService(null) {
                @Override
                public User authenticate(String username, String password) {
                    throw new RuntimeException("boom");
                }
            };

            Field authField = Login.class.getDeclaredField("authService");
            authField.setAccessible(true);
            authField.set(l, stub);

            AtomicReference<Object> msg = new AtomicReference<>();
            AtomicReference<String> title = new AtomicReference<>();
            AtomicInteger type = new AtomicInteger(-1);

            l.setMessageDialog(new Login.MessageDialog() {
                @Override
                public void showMessage(java.awt.Component parent, Object message) {
                    msg.set(message);
                }

                @Override
                public void showMessage(java.awt.Component parent, Object message, String t, int messageType) {
                    msg.set(message);
                    title.set(t);
                    type.set(messageType);
                }
            });

            // set credentials
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

            Method m = Login.class.getDeclaredMethod("jButton1ActionPerformed", java.awt.event.ActionEvent.class);
            m.setAccessible(true);

            m.invoke(l, (Object) null);

            long start = System.currentTimeMillis();
            while ((msg.get() == null || !btn.isEnabled() || !"Log in".equals(btn.getText()) || title.get() == null) && System.currentTimeMillis() - start < 2000) {
                Thread.sleep(50);
                try { java.awt.EventQueue.invokeAndWait(() -> {}); } catch (Exception ignored) {}
            }

            assertTrue(String.valueOf(msg.get()).contains("Login failed: boom"));
            assertEquals("Login Error", title.get());
            assertEquals(JOptionPane.ERROR_MESSAGE, type.get());
            assertTrue(btn.isEnabled());
            assertEquals("Log in", btn.getText());
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
