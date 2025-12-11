package es.ull.esit.app.middleware.service;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ProductService} validating input parsing and that the
 * service delegates to the ApiClient with the expected objects.
 */
class ProductServiceTest {

  static class SpyApiClient extends ApiClient {
    Drink createdDrink;
    Long updatedDrinkId;
    Drink updatedDrink;

    Appetizer createdAppetizer;
    Long updatedAppetizerId;
    Appetizer updatedAppetizer;

    MainCourse createdMainCourse;
    Long updatedMainCourseId;
    MainCourse updatedMainCourse;

    SpyApiClient() { super("http://unused"); }

    @Override
    public Drink createDrink(Drink appetizer) {
      this.createdDrink = appetizer;
      return appetizer;
    }

    @Override
    public Drink updateDrink(Long id, Drink drink) {
      this.updatedDrinkId = id;
      this.updatedDrink = drink;
      return drink;
    }

    @Override
    public Appetizer createAppetizer(Appetizer app) {
      this.createdAppetizer = app;
      return app;
    }

    @Override
    public Appetizer updateAppetizer(Long id, Appetizer app) {
      this.updatedAppetizerId = id;
      this.updatedAppetizer = app;
      return app;
    }

    @Override
    public MainCourse createMainCourse(MainCourse mc) {
      this.createdMainCourse = mc;
      return mc;
    }

    @Override
    public MainCourse updateMainCourse(Long id, MainCourse mc) {
      this.updatedMainCourseId = id;
      this.updatedMainCourse = mc;
      return mc;
    }
  }

  @Test
  void addDrink_parsesPriceAndDelegatesToClient() {
    SpyApiClient spy = new SpyApiClient();
    ProductService svc = new ProductService(spy);

    svc.addDrink("Lemonade", " 42 ");

    assertNotNull(spy.createdDrink);
    assertNull(spy.createdDrink.getDrinksId());
    assertEquals("Lemonade", spy.createdDrink.getItemDrinks());
    assertEquals(42, spy.createdDrink.getDrinksPrice().intValue());
  }

  @Test
  void updateDrink_requiresIdAndDelegates() {
    SpyApiClient spy = new SpyApiClient();
    ProductService svc = new ProductService(spy);

    assertThrows(IllegalArgumentException.class, () -> svc.updateDrink(null, "X", "1"));

    svc.updateDrink(7L, "Cola", "10");
    assertEquals(7L, spy.updatedDrinkId);
    assertNotNull(spy.updatedDrink);
    assertEquals(7L, spy.updatedDrink.getDrinksId());
    assertEquals("Cola", spy.updatedDrink.getItemDrinks());
    assertEquals(10, spy.updatedDrink.getDrinksPrice().intValue());
  }

  @Test
  void addAppetizer_parsesPriceAndDelegatesToClient() {
    SpyApiClient spy = new SpyApiClient();
    ProductService svc = new ProductService(spy);

    svc.addAppetizer("Nachos", "5");
    assertNotNull(spy.createdAppetizer);
    assertNull(spy.createdAppetizer.getAppetizersId());
    assertEquals("Nachos", spy.createdAppetizer.getItemAppetizers());
    assertEquals(5, spy.createdAppetizer.getAppetizersPrice().intValue());
  }

  @Test
  void updateAppetizer_requiresIdAndDelegates() {
    SpyApiClient spy = new SpyApiClient();
    ProductService svc = new ProductService(spy);

    assertThrows(IllegalArgumentException.class, () -> svc.updateAppetizer(null, "A", "2"));

    svc.updateAppetizer(11L, "Bruschetta", "12");
    assertEquals(11L, spy.updatedAppetizerId);
    assertNotNull(spy.updatedAppetizer);
    assertEquals(11L, spy.updatedAppetizer.getAppetizersId());
    assertEquals("Bruschetta", spy.updatedAppetizer.getItemAppetizers());
    assertEquals(12, spy.updatedAppetizer.getAppetizersPrice().intValue());
  }

  @Test
  void addMainCourse_parsesPriceAndDelegatesToClient() {
    SpyApiClient spy = new SpyApiClient();
    ProductService svc = new ProductService(spy);

    svc.addMainCourse("Pasta", "100");
    assertNotNull(spy.createdMainCourse);
    assertNull(spy.createdMainCourse.getFoodId());
    assertEquals("Pasta", spy.createdMainCourse.getItemFood());
    assertEquals(100, spy.createdMainCourse.getFoodPrice().intValue());
  }

  @Test
  void updateMainCourse_requiresIdAndDelegates() {
    SpyApiClient spy = new SpyApiClient();
    ProductService svc = new ProductService(spy);

    assertThrows(IllegalArgumentException.class, () -> svc.updateMainCourse(null, "M", "1"));

    svc.updateMainCourse(21L, "Stew", "55");
    assertEquals(21L, spy.updatedMainCourseId);
    assertNotNull(spy.updatedMainCourse);
    assertEquals(21L, spy.updatedMainCourse.getFoodId());
    assertEquals("Stew", spy.updatedMainCourse.getItemFood());
    assertEquals(55, spy.updatedMainCourse.getFoodPrice().intValue());
  }

  @Test
  void invalidPricesAndNames_throwIllegalArgument() {
    SpyApiClient spy = new SpyApiClient();
    ProductService svc = new ProductService(spy);

    assertThrows(IllegalArgumentException.class, () -> svc.addDrink("", "1"));
    assertThrows(IllegalArgumentException.class, () -> svc.addDrink("Name", ""));
    assertThrows(IllegalArgumentException.class, () -> svc.addDrink("Name", "-5"));
    assertThrows(IllegalArgumentException.class, () -> svc.addDrink("Name", "not-a-number"));
  }
}
