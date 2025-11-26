package es.ull.esit.app.middleware.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Drink model class
 */
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

    /**
     * Constructor with parameters
     * @param drinksId the drinks ID
     * @param itemDrinks the drink item name
     * @param drinksPrice the drink price
     * @param receiptId the receipt ID
     */
    public Drink(Long drinksId, String itemDrinks, Integer drinksPrice, Long receiptId) {
        this.drinksId = drinksId;
        this.itemDrinks = itemDrinks;
        this.drinksPrice = drinksPrice;
        this.receiptId = receiptId;
    }

    /**
     * Getters and Setters
     * @return values of the attributes
     */
    public Long getDrinksId() { return drinksId; }

    /**
     * Set drinks ID
     * @param drinksId the drinks ID
     */
    public void setDrinksId(Long drinksId) { this.drinksId = drinksId; }
    
    /**
     * Get item drinks
     * @return the drink item name
     */
    public String getItemDrinks() { return itemDrinks; }

    /**
     * Set item drinks
     * @param itemDrinks the drink item name
     */
    public void setItemDrinks(String itemDrinks) { this.itemDrinks = itemDrinks; }

    /**
     * Get drinks price
     * @return the drink price
     */
    public Integer getDrinksPrice() { return drinksPrice; }

    /**
     * Set drinks price
     * @param drinksPrice the drink price
     */
    public void setDrinksPrice(Integer drinksPrice) { this.drinksPrice = drinksPrice; }

    /**
     * Get receipt ID
     * @return the receipt ID
     */
    public Long getReceiptId() { return receiptId; }

    /**
     * Set receipt ID
     * @param receiptId the receipt ID
     */
    public void setReceiptId(Long receiptId) { this.receiptId = receiptId; }
}