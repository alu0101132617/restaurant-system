package es.ull.esit.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link ApplicationLauncher}.
 *
 * The test injects a mock Login instance so no real GUI components are
 * created (avoids HeadlessException in CI). It verifies that
 * {@code setVisible(true)} is invoked.
 */
public class ApplicationLauncherTest {

  @AfterEach
  void tearDown() {
    // Reset factory to default to avoid interfering with other tests
    ApplicationLauncher.setLoginFactory(Login::new);
  }

  @Test
  void main_shouldInvokeSetVisibleOnLogin() {
    // Create a mock Login to prevent real UI construction
    Login mockLogin = mock(Login.class);

    // Inject the mock via the factory
    ApplicationLauncher.setLoginFactory(() -> mockLogin);

    // Run the launcher
    ApplicationLauncher.main(new String[0]);

    // Verify the mock had setVisible(true) called.
    verify(mockLogin).setVisible(true);
  }
}
