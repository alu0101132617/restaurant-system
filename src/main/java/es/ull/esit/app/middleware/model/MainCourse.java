package es.ull.esit.app.middleware.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * MainCourse model class
 */
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

    /**
     * Constructor with parameters
     * @param foodId the food ID
     * @param itemFood the food item name
     * @param foodPrice the food price
     * @param receiptId the receipt ID
     */
    public MainCourse(Long foodId, String itemFood, Integer foodPrice, Long receiptId) {
        this.foodId = foodId;
        this.itemFood = itemFood;
        this.foodPrice = foodPrice;
        this.receiptId = receiptId;
    }

    /**
     * Getters and Setters
     * @return values of the attributes
     */
    public Long getFoodId() { return foodId; }

    /**
     * Set food ID
     * @param foodId the food ID
     */
    public void setFoodId(Long foodId) { this.foodId = foodId; }

    /**
     * Get item food
     * @return
     */
    public String getItemFood() { return itemFood; }

    /**
     * Set item food
     * @param itemFood the food item name
     */
    public void setItemFood(String itemFood) { this.itemFood = itemFood; }

    /**
     * Get food price
     * @return the food price
     */
    public Integer getFoodPrice() { return foodPrice; }

    /**
     * Set food price
     * @param foodPrice the food price
     */
    public void setFoodPrice(Integer foodPrice) { this.foodPrice = foodPrice; }
    
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