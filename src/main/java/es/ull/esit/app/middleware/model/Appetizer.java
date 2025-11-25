package es.ull.esit.app.middleware.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}