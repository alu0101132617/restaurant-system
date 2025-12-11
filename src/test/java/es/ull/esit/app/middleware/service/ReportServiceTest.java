package es.ull.esit.app.middleware.service;

import static org.junit.jupiter.api.Assertions.*;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import es.ull.esit.app.middleware.model.Cashier;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ReportServiceTest {

  static class StubClient extends ApiClient {
    StubClient() { super("http://x"); }

    @Override
    public java.util.List<Appetizer> getAllAppetizers() {
      return Arrays.asList(new Appetizer(1L, "A", 1, null), new Appetizer(2L, "B", 2, null));
    }

    @Override
    public java.util.List<Drink> getAllDrinks() {
      return Arrays.asList(new Drink(1L, "D1", 1, null), new Drink(2L, "D2", 2, null), new Drink(3L, "D3", 3, null));
    }

    @Override
    public java.util.List<MainCourse> getAllMainCourses() {
      return Arrays.asList(new MainCourse(1L, "M1", 5, null));
    }

    @Override
    public java.util.List<Cashier> getAllCashiers() {
      return Arrays.asList(new Cashier(1L, "c1", 100), new Cashier(2L, "c2", 200));
    }
  }

  @Test
  void checkMenuStatus_reportsCounts() {
    ReportService srv = new ReportService(new StubClient());
    String status = srv.checkMenuStatus();
    assertTrue(status.contains("2 appetizers"));
    assertTrue(status.contains("3 drinks"));
    assertTrue(status.contains("1 main courses"));
  }

  @Test
  void getCashierInfo_returnsList() {
    ReportService srv = new ReportService(new StubClient());
    java.util.List<Cashier> list = srv.getCashierInfo();
    assertEquals(2, list.size());
  assertEquals("c1", list.get(0).getName());
  }
}
