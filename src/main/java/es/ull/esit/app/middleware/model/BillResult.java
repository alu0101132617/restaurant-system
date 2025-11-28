package es.ull.esit.app.middleware.model;

public class BillResult {
    private final double subTotal;
    private final double vat;
    private final double total;

    public BillResult(double subTotal, double vat, double total) {
        this.subTotal = subTotal;
        this.vat = vat;
        this.total = total;
    }

    public double getSubTotal() { return subTotal; }
    public double getVat() { return vat; }
    public double getTotal() { return total; }
}