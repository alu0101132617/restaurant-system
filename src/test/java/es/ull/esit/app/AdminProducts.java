package es.ull.esit.app;

/** Minimal test stub to avoid launching the real AdminProducts window. */
class TestAdminProducts extends javax.swing.JFrame {
  TestAdminProducts() {
    // avoid heavy initialization
  }

  @Override
  public void setVisible(boolean b) {
    TestStubs.adminProductsShown.set(b);
  }
}
