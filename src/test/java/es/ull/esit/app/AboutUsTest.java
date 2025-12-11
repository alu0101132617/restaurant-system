package es.ull.esit.app;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the AboutUs Swing window.
 */
public class AboutUsTest {

    @Test
    void testInitComponents_textAndButton() throws Exception {
        AboutUs about = new AboutUs();

        Field txtField = AboutUs.class.getDeclaredField("ourStory");
        txtField.setAccessible(true);
        JTextArea ourStory = (JTextArea) txtField.get(about);

        Field btnField = AboutUs.class.getDeclaredField("jButton3");
        btnField.setAccessible(true);
        JButton btn = (JButton) btnField.get(about);

        // Basic checks
        assertEquals("Info", about.getTitle());
        assertEquals("Go Back", btn.getText());
        assertFalse(ourStory.isEditable());

        String txt = ourStory.getText();
        assertNotNull(txt);
        assertTrue(txt.startsWith("<html>"));
        assertTrue(txt.contains("Our Story"));

        about.dispose();
    }

    @Test
    void testGoBack_disposesWindow() throws Exception {
        AboutUs about = new AboutUs();

        Field btnField = AboutUs.class.getDeclaredField("jButton3");
        btnField.setAccessible(true);
        JButton btn = (JButton) btnField.get(about);

        // The handler is private; invoke via reflection
        Method m = AboutUs.class.getDeclaredMethod("jButton3ActionPerformed", java.awt.event.ActionEvent.class);
        m.setAccessible(true);

        m.invoke(about, (Object) null);

        // After action, the AboutUs frame should be disposed
        assertFalse(about.isDisplayable());
    }
}
