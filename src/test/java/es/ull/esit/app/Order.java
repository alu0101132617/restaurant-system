package es.ull.esit.app;

/** Minimal test stub to avoid launching the real Order window during tests. */
class TestOrder extends javax.swing.JFrame {
  TestOrder() {
    // avoid heavy initialization
  }

  @Override
  public void setVisible(boolean b) {
    TestStubs.orderShown.set(b);
  }
}
