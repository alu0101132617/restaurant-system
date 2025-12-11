package es.ull.esit.app;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for AdminLogin.
 *
 * These tests use lightweight stubs for dependent windows so the tests do not
 * open real UI windows or call external services.
 */
public class AdminLoginTest {

  private AdminLogin frame;

  @BeforeEach
  public void setUp() throws Exception {
    // Clear flags
    TestStubs.adminProductsShown.set(false);
    TestStubs.orderShown.set(false);
    TestStubs.loginShown.set(false);

    // Inject test stubs via the suppliers so we don't need to shadow
    // production classes. This avoids classfile mismatches for JaCoCo.
    AdminLogin.adminProductsSupplier = () -> new TestAdminProducts();
    AdminLogin.orderSupplier = () -> new TestOrder();
    AdminLogin.loginSupplier = () -> new TestLogin();

    // Create AdminLogin on EDT to mimic real usage
    SwingUtilities.invokeAndWait(() -> frame = new AdminLogin());
  }

  @AfterEach
  public void tearDown() throws Exception {
    if (frame != null) {
      SwingUtilities.invokeAndWait(() -> {
        frame.dispose();
      });
    }
  }

  private Object getPrivateField(Object obj, String name) throws Exception {
    Field f = obj.getClass().getDeclaredField(name);
    f.setAccessible(true);
    return f.get(obj);
  }

  @Test
  public void testInitComponents_textsAndButtons() throws Exception {
    // Access private components
    JLabel welcome = (JLabel) getPrivateField(frame, "jLabel3");
    JButton btnUpdatePrices = (JButton) getPrivateField(frame, "jButton2");
    JButton btnMenu = (JButton) getPrivateField(frame, "jButton1");
    JButton btnLogout = (JButton) getPrivateField(frame, "jButton3");

    assertEquals("Welcome Admin", welcome.getText());
    assertEquals("Update Prices", btnUpdatePrices.getText());
    assertEquals("Menu", btnMenu.getText());
    assertEquals("LogOut", btnLogout.getText());

    // Title
    assertEquals("Admin ", frame.getTitle());
  }

  @Test
  public void testLogout_opensLoginAndDisposes() throws Exception {
    // Call private method jButton3ActionPerformed via reflection
    var m = frame.getClass().getDeclaredMethod("jButton3ActionPerformed", java.awt.event.ActionEvent.class);
    m.setAccessible(true);
    // Invoke with a dummy ActionEvent
    m.invoke(frame, new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "cmd"));

    // After logout, our stub Login should have been shown and frame disposed
    assertTrue(TestStubs.loginShown.get(), "Login.setVisible(true) should be called");
    // frame should be disposed
    assertFalse(frame.isDisplayable());
  }

  @Test
  public void testUpdatePrices_opensAdminProductsAndDisposes() throws Exception {
    var m = frame.getClass().getDeclaredMethod("jButton2ActionPerformed", java.awt.event.ActionEvent.class);
    m.setAccessible(true);
    m.invoke(frame, new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "cmd"));

    // Our stub AdminProducts should be shown
    assertTrue(TestStubs.adminProductsShown.get(), "AdminProducts.setVisible(true) should be called");
    assertFalse(frame.isDisplayable());
  }

  @Test
  public void testMenu_opensOrderAndDisposes() throws Exception {
    var m = frame.getClass().getDeclaredMethod("jButton1ActionPerformed", java.awt.event.ActionEvent.class);
    m.setAccessible(true);
    m.invoke(frame, new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "cmd"));

    assertTrue(TestStubs.orderShown.get(), "Order.setVisible(true) should be called");
    assertFalse(frame.isDisplayable());
  }

  @Test
  public void testMain_runsWithoutThrowing() {
    // Just ensure main() does not throw and schedules the UI
    AdminLogin.main(new String[] {});
  }
}
