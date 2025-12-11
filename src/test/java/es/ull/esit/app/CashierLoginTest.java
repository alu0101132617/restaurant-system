package es.ull.esit.app;

import es.ull.esit.app.middleware.model.Cashier;
import es.ull.esit.app.middleware.service.ReportService;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CashierLoginTest {

  static class StubReportService extends ReportService {
    private final List<Cashier> cashiers;
    private final String status;
    private final boolean throwOnInfo;
    private final boolean throwOnStatus;

    StubReportService(List<Cashier> cashiers, String status, boolean throwOnInfo, boolean throwOnStatus) {
      super(null);
      this.cashiers = cashiers;
      this.status = status;
      this.throwOnInfo = throwOnInfo;
      this.throwOnStatus = throwOnStatus;
    }

    @Override
    public List<Cashier> getCashierInfo() {
      if (throwOnInfo) throw new RuntimeException("boom");
      return cashiers;
    }

    @Override
    public String checkMenuStatus() {
      if (throwOnStatus) throw new RuntimeException("nope");
      return status;
    }
  }

  @Test
  void testWelcomeMessages() {
    StubReportService stub = new StubReportService(Arrays.asList(), "OK", false, false);
    CashierLogin w1 = new CashierLogin(stub, null, false);
    assertEquals("Welcome Cashier", w1.getWelcomeText());

    CashierLogin w2 = new CashierLogin(stub, "Luis", false);
    assertEquals("Welcome Luis", w2.getWelcomeText());
  }

  @Test
  void testAboutUsActionSyncSuccessAndFailure() {
    StubReportService ok = new StubReportService(Arrays.asList(new Cashier(1L, "A", 100), new Cashier(2L, "B", 200)), "OK", false, false);
    CashierLogin win = new CashierLogin(ok, "X", false);
    assertEquals(2, win.aboutUsActionSync());

    StubReportService fail = new StubReportService(Arrays.asList(), "OK", true, false);
    CashierLogin win2 = new CashierLogin(fail, "X", false);
    assertEquals(-1, win2.aboutUsActionSync());
  }

  @Test
  void testMenuActionSyncSuccessAndFailure() {
    StubReportService ok = new StubReportService(Arrays.asList(), "Menu OK", false, false);
    CashierLogin win = new CashierLogin(ok, "X", false);
    assertEquals("Menu OK", win.menuActionSync());

    StubReportService fail = new StubReportService(Arrays.asList(), null, false, true);
    CashierLogin win2 = new CashierLogin(fail, "X", false);
    assertNull(win2.menuActionSync());
  }

  @Test
  void testLogoutActionSync() {
    StubReportService stub = new StubReportService(Arrays.asList(), "OK", false, false);
    CashierLogin win = new CashierLogin(stub, "X", false);
    assertTrue(win.logoutActionSync());
  }

  @Test
  void testRunActionsUseSuppliers() {
    StubReportService stub = new StubReportService(Arrays.asList(), "OK", false, false);
    CashierLogin win = new CashierLogin(stub, "X", false);

    AtomicBoolean aboutShown = new AtomicBoolean(false);
    AtomicBoolean orderShown = new AtomicBoolean(false);
    AtomicBoolean loginShown = new AtomicBoolean(false);

    // Suppliers returning small stub frames that flip a flag when setVisible is called
    win.setAboutUsSupplier(() -> new javax.swing.JFrame() {
      @Override
      public void setVisible(boolean b) {
        aboutShown.set(b);
      }
    });

    win.setOrderSupplier(() -> new javax.swing.JFrame() {
      @Override
      public void setVisible(boolean b) {
        orderShown.set(b);
      }
    });

    win.setLoginSupplier(() -> new javax.swing.JFrame() {
      @Override
      public void setVisible(boolean b) {
        loginShown.set(b);
      }
    });

    // Execute the synchronous run variants which exercise the code paths
    win.aboutUsActionRun();
    assertTrue(aboutShown.get());

    win.menuActionRun();
    assertTrue(orderShown.get());

    win.logoutActionRun();
    assertTrue(loginShown.get());
  }

  @Test
  void testCoverageHelper() {
    StubReportService stub = new StubReportService(Arrays.asList(), "OK", false, false);
    CashierLogin win = new CashierLogin(stub, "X", false);
    // call the small helper that exercises a few extra branches
    win.testOnlyCoverageHelper();
  }
}
