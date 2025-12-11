package es.ull.esit.app.middleware.service;

import static org.junit.jupiter.api.Assertions.*;

import es.ull.esit.app.middleware.model.BillResult;
import java.io.File;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

  @Test
  void calculateBill_rounding() {
    OrderService srv = new OrderService();
    BillResult res = srv.calculateBill(100.0);
    assertEquals(100.0, res.getSubTotal());
    assertEquals(15.0, res.getVat());
    assertEquals(115.0, res.getTotal());
  }

  @Test
  void generateReceiptFile_createsFile() throws Exception {
    OrderService srv = new OrderService();
    BillResult res = new BillResult(10.0, 1.5, 11.5);
    int receiptNo = 9999;
    srv.generateReceiptFile(receiptNo, res);

    File f = new File("receipts/billNo." + receiptNo + ".txt");
    assertTrue(f.exists());
    // cleanup
    f.delete();
    new File("receipts").delete();
  }
}
