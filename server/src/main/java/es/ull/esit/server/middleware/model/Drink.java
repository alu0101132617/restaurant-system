package es.ull.esit.server.middleware.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Item")
@IdClass(Drink.DrinkId.class)
public class Drink {
    @Id
    @Column(name = "drinks_id")
    private Long drinksId;

    @Column(name = "Item_drinks")
    private String itemDrinks;

    @Column(name = "drinks_price")
    private Integer drinksPrice;

    @Column(name = "Receipt_id")
    private Long receiptId;

    public Drink() {}

    public Drink(Long drinksId, String itemDrinks, Integer drinksPrice, Long receiptId) {
        this.drinksId = drinksId;
        this.itemDrinks = itemDrinks;
        this.drinksPrice = drinksPrice;
        this.receiptId = receiptId;
    }

    public Long getDrinksId() { return drinksId; }
    public void setDrinksId(Long drinksId) { this.drinksId = drinksId; }
    public String getItemDrinks() { return itemDrinks; }
    public void setItemDrinks(String itemDrinks) { this.itemDrinks = itemDrinks; }
    public Integer getDrinksPrice() { return drinksPrice; }
    public void setDrinksPrice(Integer drinksPrice) { this.drinksPrice = drinksPrice; }
    public Long getReceiptId() { return receiptId; }
    public void setReceiptId(Long receiptId) { this.receiptId = receiptId; }

    // Clase para clave compuesta
    public static class DrinkId implements Serializable {
        private Long drinksId;
        
        public DrinkId() {}
        public DrinkId(Long drinksId) { this.drinksId = drinksId; }
        
        public Long getDrinksId() { return drinksId; }
        public void setDrinksId(Long drinksId) { this.drinksId = drinksId; }
    }
}

