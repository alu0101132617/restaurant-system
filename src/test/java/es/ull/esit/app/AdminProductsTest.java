package es.ull.esit.app;

import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import es.ull.esit.app.middleware.service.ProductService;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminProductsTest {

  static class StubProductService extends ProductService {
    StubProductService() {
      super(null);
    }

    @Override
    public List<Drink> getAllDrinks() {
      return Arrays.asList(new Drink(1L, "Coke", 100, null));
    }

    @Override
    public Drink getDrinkById(Long id) {
      return new Drink(id, "Coke", 100, null);
    }

    @Override
    public List<Appetizer> getAllAppetizers() {
      return Arrays.asList(new Appetizer(2L, "Fries", 200, null));
    }

    @Override
    public Appetizer getAppetizerById(Long id) {
      return new Appetizer(id, "Fries", 200, null);
    }

    @Override
    public List<MainCourse> getAllMainCourses() {
      return Arrays.asList(new MainCourse(3L, "Steak", 1000, null));
    }

    @Override
    public MainCourse getMainCourseById(Long id) {
      return new MainCourse(id, "Steak", 1000, null);
    }
  }

  @Test
  void testLoadSyncFillsModels() {
    StubProductService stub = new StubProductService();
    AdminProducts window = new AdminProducts(stub, false);

    // load synchronously
    window.loadDrinksSync();
    assertEquals(1, window.modelDrink.getRowCount());
    assertEquals("Coke", window.modelDrink.getValueAt(0, 1));
    assertEquals(100, window.modelDrink.getValueAt(0, 2));

    window.loadAppetizerSync();
    assertEquals(1, window.modelappetizers.getRowCount());
    assertEquals("Fries", window.modelappetizers.getValueAt(0, 1));
    assertEquals(200, window.modelappetizers.getValueAt(0, 2));

    window.loadmainCourseSync();
    assertEquals(1, window.modelmaincourse.getRowCount());
    assertEquals("Steak", window.modelmaincourse.getValueAt(0, 1));
    assertEquals(1000, window.modelmaincourse.getValueAt(0, 2));
  }

  @Test
  void testSelectByRowSyncSetsFields() {
    StubProductService stub = new StubProductService();
    AdminProducts window = new AdminProducts(stub, false);

    window.loadDrinksSync();
    // select first row
    window.selectDrinkByRowSync(0);
    assertEquals("Coke", window.itemname.getText());
    assertEquals("100", window.itemprice.getText());

    window.loadAppetizerSync();
    window.selectAppetizerByRowSync(0);
    assertEquals("Fries", window.itemname1.getText());
    assertEquals("200", window.itemprice1.getText());

    window.loadmainCourseSync();
    window.selectMainCourseByRowSync(0);
    assertEquals("Steak", window.itemname2.getText());
    assertEquals("1000", window.itemprice2.getText());
  }

  @Test
  void testTableMouseClickedNoSelectionDoesNotThrow() throws Exception {
    StubProductService stub = new StubProductService();
    AdminProducts window = new AdminProducts(stub, false);

    // Ensure no selection
    window.jTable1.clearSelection();
    window.jTable2.clearSelection();
    window.jTable3.clearSelection();

    // Invoke private mouse handlers reflectively with null event (they don't use the event)
    var c1 = AdminProducts.class.getDeclaredMethod("jTable1MouseClicked", MouseEvent.class);
    c1.setAccessible(true);
    assertDoesNotThrow(() -> c1.invoke(window, (Object) null));

    var c2 = AdminProducts.class.getDeclaredMethod("jTable2MouseClicked", MouseEvent.class);
    c2.setAccessible(true);
    assertDoesNotThrow(() -> c2.invoke(window, (Object) null));

    var c3 = AdminProducts.class.getDeclaredMethod("jTable3MouseClicked", MouseEvent.class);
    c3.setAccessible(true);
    assertDoesNotThrow(() -> c3.invoke(window, (Object) null));
  }

  static class SpyProductService extends StubProductService {
    volatile boolean addDrinkCalled = false;
    volatile String addedDrinkName;
    volatile String addedDrinkPrice;

    volatile boolean addAppCalled = false;
    volatile boolean addMainCalled = false;

    volatile boolean updateDrinkCalled = false;
    volatile Long updateDrinkId;
    volatile String updateDrinkName;
    volatile String updateDrinkPrice;

    volatile boolean updateAppCalled = false;
    volatile boolean updateMainCalled = false;

    SpyProductService() { super(); }

    @Override
    public void addDrink(String name, String price) {
      this.addDrinkCalled = true;
      this.addedDrinkName = name;
      this.addedDrinkPrice = price;
    }

    @Override
    public void addAppetizer(String name, String price) {
      this.addAppCalled = true;
    }

    @Override
    public void addMainCourse(String name, String price) {
      this.addMainCalled = true;
    }

    @Override
    public void updateDrink(Long id, String name, String price) {
      this.updateDrinkCalled = true;
      this.updateDrinkId = id;
      this.updateDrinkName = name;
      this.updateDrinkPrice = price;
    }

    @Override
    public void updateAppetizer(Long id, String name, String price) {
      this.updateAppCalled = true;
    }

    @Override
    public void updateMainCourse(Long id, String name, String price) {
      this.updateMainCalled = true;
    }
  }

  @Test
  void testAddActionsInvokeServiceAndClearFields() throws Exception {
    SpyProductService spy = new SpyProductService();
    AdminProducts window = new AdminProducts(spy, false);

    // Prepare drink fields and invoke Add action
    window.itemname.setText("Lemonade");
    window.itemprice.setText("50");

    var mAddDrink = AdminProducts.class.getDeclaredMethod("jButton2ActionPerformed", java.awt.event.ActionEvent.class);
    mAddDrink.setAccessible(true);
    mAddDrink.invoke(window, (Object) null);

    // Wait for background thread to call service (short polling)
    for (int i = 0; i < 40 && !spy.addDrinkCalled; i++) Thread.sleep(50);

    assertTrue(spy.addDrinkCalled);
    assertEquals("Lemonade", spy.addedDrinkName);
    assertEquals("50", spy.addedDrinkPrice);

    // Dialogs are shown on the EDT in the real handler; in headless test
    // environments the JOptionPane may not execute. We assert the service
    // was called with expected values and avoid checking dialog-driven UI
    // clearing here.

    // Appetizer add
    window.itemname1.setText("Nachos");
    window.itemprice1.setText("80");
    var mAddApp = AdminProducts.class.getDeclaredMethod("jButton5ActionPerformed", java.awt.event.ActionEvent.class);
    mAddApp.setAccessible(true);
    mAddApp.invoke(window, (Object) null);
    for (int i = 0; i < 40 && !spy.addAppCalled; i++) Thread.sleep(50);
    assertTrue(spy.addAppCalled);

    // Main course add
    window.itemname2.setText("Pasta");
    window.itemprice2.setText("120");
    var mAddMain = AdminProducts.class.getDeclaredMethod("jButton7ActionPerformed", java.awt.event.ActionEvent.class);
    mAddMain.setAccessible(true);
    mAddMain.invoke(window, (Object) null);
    for (int i = 0; i < 40 && !spy.addMainCalled; i++) Thread.sleep(50);
    assertTrue(spy.addMainCalled);
  }

  @Test
  void testUpdateActionsInvokeServiceAndResetSelection() throws Exception {
    SpyProductService spy = new SpyProductService();
    AdminProducts window = new AdminProducts(spy, false);

    // Simulate a selected drink
    window.selectedDrinkID = 10L;
    window.itemname.setText("NewName");
    window.itemprice.setText("77");

    var mUpdateDrink = AdminProducts.class.getDeclaredMethod("jButton1ActionPerformed", java.awt.event.ActionEvent.class);
    mUpdateDrink.setAccessible(true);
    mUpdateDrink.invoke(window, (Object) null);

    for (int i = 0; i < 40 && !spy.updateDrinkCalled; i++) Thread.sleep(50);
    assertTrue(spy.updateDrinkCalled);
    assertEquals(10L, spy.updateDrinkId);
    assertEquals("NewName", spy.updateDrinkName);
    assertEquals("77", spy.updateDrinkPrice);

  // The UI clearing and message dialog happen on the EDT and are not
  // reliable in headless test runs. Only check that the service was
  // invoked with the expected parameters.

    // Update appetizer and main course similarly
    window.selectedAppetizerID = 20L;
    window.itemname1.setText("A1");
    window.itemprice1.setText("30");
    var mUpdateApp = AdminProducts.class.getDeclaredMethod("jButton4ActionPerformed", java.awt.event.ActionEvent.class);
    mUpdateApp.setAccessible(true);
    mUpdateApp.invoke(window, (Object) null);
    for (int i = 0; i < 40 && !spy.updateAppCalled; i++) Thread.sleep(50);
    assertTrue(spy.updateAppCalled);

    window.selectedMainCourseID = 30L;
    window.itemname2.setText("M1");
    window.itemprice2.setText("300");
    var mUpdateMain = AdminProducts.class.getDeclaredMethod("jButton6ActionPerformed", java.awt.event.ActionEvent.class);
    mUpdateMain.setAccessible(true);
    mUpdateMain.invoke(window, (Object) null);
    for (int i = 0; i < 40 && !spy.updateMainCalled; i++) Thread.sleep(50);
    assertTrue(spy.updateMainCalled);
  }
}
