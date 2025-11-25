package es.ull.esit.app.middleware.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MainCourse {
    @JsonProperty("foodId")
    private Long foodId;
    
    @JsonProperty("itemFood")
    private String itemFood;
    
    @JsonProperty("foodPrice")
    private Integer foodPrice;
    
    @JsonProperty("receiptId")
    private Long receiptId;

    public MainCourse() {}

    public MainCourse(Long foodId, String itemFood, Integer foodPrice, Long receiptId) {
        this.foodId = foodId;
        this.itemFood = itemFood;
        this.foodPrice = foodPrice;
        this.receiptId = receiptId;
    }

    public Long getFoodId() { return foodId; }
    public void setFoodId(Long foodId) { this.foodId = foodId; }
    
    public String getItemFood() { return itemFood; }
    public void setItemFood(String itemFood) { this.itemFood = itemFood; }
    
    public Integer getFoodPrice() { return foodPrice; }
    public void setFoodPrice(Integer foodPrice) { this.foodPrice = foodPrice; }
    
    public Long getReceiptId() { return receiptId; }
    public void setReceiptId(Long receiptId) { this.receiptId = receiptId; }
}