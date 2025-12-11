package es.ull.esit.app;

import es.ull.esit.app.middleware.service.ProductService;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.MainCourse;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

  static class StubProductService extends ProductService {
    public StubProductService() { super(null); }

    @Override
    public List<Drink> getAllDrinks() {
      return Arrays.asList(new Drink(1L, "Coke", 5, null));
    }

    @Override
    public List<Appetizer> getAllAppetizers() {
      return Arrays.asList(new Appetizer(2L, "Fries", 3, null));
    }

    @Override
    public List<MainCourse> getAllMainCourses() {
      return Arrays.asList(new MainCourse(3L, "Steak", 20, null));
    }
  }

  private static final Order.MessageDialog NO_OP_DIALOG = new Order.MessageDialog() {
    @Override
    public void showMessage(java.awt.Component parent, Object message) {
      // no-op for headless tests
    }

    @Override
    public void showMessage(java.awt.Component parent, Object message, String title, int messageType) {
      // no-op for headless tests
    }
  };

  @Test
  public void testFillSumPayAndNewReceipt() throws Exception {
    StubProductService stub = new StubProductService();

    // Use package-private constructor that avoids asynchronous loading
    Order order = new Order(stub, false);
    order.setMessageDialog(NO_OP_DIALOG);

    // Call private fill methods to populate models
    Method fillDrinks = Order.class.getDeclaredMethod("fillDrinks", List.class);
    fillDrinks.setAccessible(true);
    fillDrinks.invoke(order, stub.getAllDrinks());

    Method fillAppetizers = Order.class.getDeclaredMethod("fillAppetizers", List.class);
    fillAppetizers.setAccessible(true);
    fillAppetizers.invoke(order, stub.getAllAppetizers());

    Method fillMains = Order.class.getDeclaredMethod("fillMains", List.class);
    fillMains.setAccessible(true);
    fillMains.invoke(order, stub.getAllMainCourses());

    // Access drinksModel and set quantities
    Field drinksModelField = Order.class.getDeclaredField("drinksModel");
    drinksModelField.setAccessible(true);
    DefaultTableModel drinksModel = (DefaultTableModel) drinksModelField.get(order);
    assertEquals(1, drinksModel.getRowCount());

    // Set quantity to 2 for the drink
    drinksModel.setValueAt(2, 0, 3);

    // Sum from model (private)
    Method sumFromModel = Order.class.getDeclaredMethod("sumFromModel", DefaultTableModel.class);
    sumFromModel.setAccessible(true);
    double drinksSum = (double) sumFromModel.invoke(order, drinksModel);
    assertEquals(10.0, drinksSum, 0.0001);

    // Now invoke pay button handler (private)
    Method pay = Order.class.getDeclaredMethod("payBtnActionPerformed", java.awt.event.ActionEvent.class);
    pay.setAccessible(true);
    pay.invoke(order, (java.awt.event.ActionEvent) null);

    // Validate that subtotal/total fields were updated
    Field subField = Order.class.getDeclaredField("subTotal");
    Field vatField = Order.class.getDeclaredField("vat");
    Field totalField = Order.class.getDeclaredField("total");
    subField.setAccessible(true);
    vatField.setAccessible(true);
    totalField.setAccessible(true);

    double sub = (double) subField.get(order);
    double vat = (double) vatField.get(order);
    double total = (double) totalField.get(order);

    assertTrue(sub > 0.0);
    assertTrue(vat >= 0.0);
    assertTrue(total > 0.0);

    // Invoke newReceipt (should reset because total > 0)
    Method newReceipt = Order.class.getDeclaredMethod("newReceiptBtnActionPerformed", java.awt.event.ActionEvent.class);
    newReceipt.setAccessible(true);
    newReceipt.invoke(order, (java.awt.event.ActionEvent) null);

    // After new receipt, lastBill should be null and receiptNo incremented
    Field lastBillField = Order.class.getDeclaredField("lastBill");
    lastBillField.setAccessible(true);
    assertNull(lastBillField.get(order));

    Field receiptNoField = Order.class.getDeclaredField("receiptNo");
    receiptNoField.setAccessible(true);
    int receiptNo = (int) receiptNoField.get(order);
    assertTrue(receiptNo >= 2);

    // Labels should be reset
    Field subLbl = Order.class.getDeclaredField("subTotalLbl");
    subLbl.setAccessible(true);
    javax.swing.JLabel sLbl = (javax.swing.JLabel) subLbl.get(order);
    assertTrue(sLbl.getText().contains("0.0"));
  }

  @Test
  public void testSumFromModelStringFallbackAndResetQty() throws Exception {
    StubProductService stub = new StubProductService();
    Order order = new Order(stub, false);
    order.setMessageDialog(NO_OP_DIALOG);

    // Prepare a model with string values to force the fallback parsing
    DefaultTableModel model = new DefaultTableModel(new Object[] {"ID","Item","Price (SR)","Qty"}, 0);
    model.addRow(new Object[] {1L, "X", "5", "2"}); // price and qty as strings

    Method sumFromModel = Order.class.getDeclaredMethod("sumFromModel", DefaultTableModel.class);
    sumFromModel.setAccessible(true);
    double sum = (double) sumFromModel.invoke(order, model);
    assertEquals(10.0, sum, 0.0001);

    // test resetQtyColumn
    Method resetQty = Order.class.getDeclaredMethod("resetQtyColumn", DefaultTableModel.class);
    resetQty.setAccessible(true);
    resetQty.invoke(order, model);
    assertEquals(0, model.getValueAt(0, 3));
  }

  @Test
  public void testSaveReceiptNoBillDoesNotThrow() throws Exception {
    StubProductService stub = new StubProductService();
    Order order = new Order(stub, false);
    order.setMessageDialog(NO_OP_DIALOG);

    // Ensure lastBill is null
    Field lastBillField = Order.class.getDeclaredField("lastBill");
    lastBillField.setAccessible(true);
    lastBillField.set(order, null);

    Method save = Order.class.getDeclaredMethod("saveReceiptBtnActionPerformed", java.awt.event.ActionEvent.class);
    save.setAccessible(true);

    // Should not throw, and will show an info dialog in the UI thread
    save.invoke(order, (java.awt.event.ActionEvent) null);
  }
}
