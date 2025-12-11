package es.ull.esit.app;

/** Minimal stub to avoid launching the real Login window during tests. */
class TestLogin extends javax.swing.JFrame {
  TestLogin() {
    // avoid heavy initialization
  }

  @Override
  public void setVisible(boolean b) {
    TestStubs.loginShown.set(b);
  }
}
