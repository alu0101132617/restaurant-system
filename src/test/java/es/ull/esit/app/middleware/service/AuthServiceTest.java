package es.ull.esit.app.middleware.service;

import static org.junit.jupiter.api.Assertions.*;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.User;
import org.junit.jupiter.api.Test;

class AuthServiceTest {

  static class StubSuccessClient extends ApiClient {
    StubSuccessClient() {
      super("http://localhost");
    }

    @Override
    public User login(String username, String password) {
      return new User(username, "CASHIER");
    }
  }

  static class StubFailClient extends ApiClient {
    StubFailClient() {
      super("http://localhost");
    }

    @Override
    public User login(String username, String password) {
      throw new RuntimeException("bad creds");
    }
  }

  @Test
  void authenticate_success() {
    AuthService srv = new AuthService(new StubSuccessClient());
    User u = srv.authenticate("john", "pwd");
    assertNotNull(u);
    assertEquals("john", u.getUsername());
  }

  @Test
  void authenticate_invalidUsername() {
    AuthService srv = new AuthService(new StubSuccessClient());
    assertThrows(IllegalArgumentException.class, () -> srv.authenticate("  ", "p"));
  }

  @Test
  void authenticate_invalidPassword() {
    AuthService srv = new AuthService(new StubSuccessClient());
    assertThrows(IllegalArgumentException.class, () -> srv.authenticate("u", ""));
  }

  @Test
  void authenticate_backendFailurePropagates() {
    AuthService srv = new AuthService(new StubFailClient());
    assertThrows(RuntimeException.class, () -> srv.authenticate("u", "p"));
  }
}
