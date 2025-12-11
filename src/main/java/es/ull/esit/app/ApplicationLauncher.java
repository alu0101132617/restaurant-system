package es.ull.esit.app;

/**
 * @brief Auxiliary entry point used to start the application's UI.
 *
 *        Serves as a simple launcher responsible for opening
 *        the Login window when the program is executed directly.
 *
 */
public class ApplicationLauncher {

  /**
   * @brief Alternate entry point used to launch the Login window.
   *
   *        Creates and displays the application's Login screen.
   *
   * @param args [String[]] Command-line arguments (unused).
   */
  public static void main(String[] args) {
    // Use a factory so tests can inject a mock Login instance and avoid
    // creating real GUI components (which can fail in headless CI).
    java.awt.EventQueue.invokeLater(() -> getLoginFactory().get().setVisible(true));
  }

  // ---------------------------------------------------------------------------
  // Test seam: a supplier that creates the Login instance. By default it
  // constructs a real Login, but tests can replace it with a supplier that
  // returns a mock to avoid UI creation.
  // ---------------------------------------------------------------------------
  private static java.util.function.Supplier<Login> loginFactory = Login::new;

  /**
   * Package-visible to allow tests in the same package to override the
   * factory. Keep it non-public to minimize API surface.
   */
  static void setLoginFactory(java.util.function.Supplier<Login> factory) {
    loginFactory = factory;
  }

  static java.util.function.Supplier<Login> getLoginFactory() {
    return loginFactory;
  }
}
