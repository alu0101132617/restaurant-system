package es.ull.esit.app.middleware.service;

import es.ull.esit.app.middleware.model.BillResult;
import java.io.PrintWriter;

public class OrderService {

    private static final double VAT_RATE = 0.15;

    public BillResult calculateBill(double itemsTotalSum) {
        double vat = itemsTotalSum * VAT_RATE;
        double total = itemsTotalSum + vat;
        // Runden auf 2 Nachkommastellen
        return new BillResult(
            Math.round(itemsTotalSum * 100.0) / 100.0,
            Math.round(vat * 100.0) / 100.0,
            Math.round(total * 100.0) / 100.0
        );
    }

    public void generateReceiptFile(int receiptNo, BillResult bill) throws Exception {
        // Ordner sicherstellen (optional, verhindert Crash wenn Ordner fehlt)
        new java.io.File("receipts").mkdirs(); 
        
        try (PrintWriter output = new PrintWriter("receipts/billNo." + receiptNo + ".txt")) {
            output.println(" Bill number is: " + receiptNo);
            output.println("==============");
            output.println("--------------");
            output.println("Subtotal is: " + bill.getSubTotal() + " SR");
            output.println("vat: " + bill.getVat() + " SR");
            output.println("Total is: " + bill.getTotal() + " SR");
            output.println();
            output.println("THANK YOU FOR ORDERING");
        }
    }
}