package es.ull.esit.app.middleware.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Appetizer model class
 */
public class Appetizer {
    @JsonProperty("appetizersId")
    private Long appetizersId;
    
    @JsonProperty("itemAppetizers")
    private String itemAppetizers;
    
    @JsonProperty("appetizersPrice")
    private Integer appetizersPrice;
    
    @JsonProperty("receiptId")
    private Long receiptId;

    public Appetizer() {}

    /**
     * Constructor with parameters
     * @param appetizersId the appetizers ID
     * @param itemAppetizers the appetizer item name
     * @param appetizersPrice the appetizer price
     * @param receiptId the receipt ID
     */
    public Appetizer(Long appetizersId, String itemAppetizers, Integer appetizersPrice, Long receiptId) {
        this.appetizersId = appetizersId;
        this.itemAppetizers = itemAppetizers;
        this.appetizersPrice = appetizersPrice;
        this.receiptId = receiptId;
    }

    /**
     * Getters and Setters
     * @return values of the attributes
     */
    public Long getAppetizersId() { return appetizersId; }

    /**
     * Set appetizers ID
     * @param appetizersId the appetizers ID
     */
    public void setAppetizersId(Long appetizersId) { this.appetizersId = appetizersId; }

    /**
     * Get item appetizers
     * @return the appetizer item name
     */
    public String getItemAppetizers() { return itemAppetizers; }

    /**
     * Set item appetizers
     * @param itemAppetizers the appetizer item name
     */
    public void setItemAppetizers(String itemAppetizers) { this.itemAppetizers = itemAppetizers; }

    /**
     * Get appetizers price
     * @return the appetizer price
     */
    public Integer getAppetizersPrice() { return appetizersPrice; }

    /**
     * Set appetizers price
     * @param appetizersPrice the appetizer price
     */
    public void setAppetizersPrice(Integer appetizersPrice) { this.appetizersPrice = appetizersPrice; }
    
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