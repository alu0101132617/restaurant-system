package es.ull.esit.server.middleware.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Item")
@IdClass(Appetizer.AppetizerId.class)
public class Appetizer {
    @Id
    @Column(name = "appetizers_id")
    private Long appetizersId;

    @Column(name = "Item_appetizers")
    private String itemAppetizers;

    @Column(name = "appetizers_price")
    private Integer appetizersPrice;

    @Column(name = "Receipt_id")
    private Long receiptId;

    public Appetizer() {}

    public Appetizer(Long appetizersId, String itemAppetizers, Integer appetizersPrice, Long receiptId) {
        this.appetizersId = appetizersId;
        this.itemAppetizers = itemAppetizers;
        this.appetizersPrice = appetizersPrice;
        this.receiptId = receiptId;
    }

    public Long getAppetizersId() { return appetizersId; }
    public void setAppetizersId(Long appetizersId) { this.appetizersId = appetizersId; }
    public String getItemAppetizers() { return itemAppetizers; }
    public void setItemAppetizers(String itemAppetizers) { this.itemAppetizers = itemAppetizers; }
    public Integer getAppetizersPrice() { return appetizersPrice; }
    public void setAppetizersPrice(Integer appetizersPrice) { this.appetizersPrice = appetizersPrice; }
    public Long getReceiptId() { return receiptId; }
    public void setReceiptId(Long receiptId) { this.receiptId = receiptId; }

    // Clase para clave compuesta
    public static class AppetizerId implements Serializable {
        private Long appetizersId;
        
        public AppetizerId() {}
        public AppetizerId(Long appetizersId) { this.appetizersId = appetizersId; }
        
        public Long getAppetizersId() { return appetizersId; }
        public void setAppetizersId(Long appetizersId) { this.appetizersId = appetizersId; }
    }
}

