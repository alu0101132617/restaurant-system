package es.ull.esit.app.middleware.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Drink {
    @JsonProperty("drinksId")
    private Long drinksId;
    
    @JsonProperty("itemDrinks")
    private String itemDrinks;
    
    @JsonProperty("drinksPrice")
    private Integer drinksPrice;
    
    @JsonProperty("receiptId")
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
}