package es.ull.esit.server.middleware.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Item")
@IdClass(MainCourse.MainCourseId.class)
public class MainCourse {
    @Id
    @Column(name = "food_id")
    private Long foodId;

    @Column(name = "Item_food")
    private String itemFood;

    @Column(name = "food_price")
    private Integer foodPrice;

    @Column(name = "Receipt_id")
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

    // Clase para clave compuesta
    public static class MainCourseId implements Serializable {
        private Long foodId;
        
        public MainCourseId() {}
        public MainCourseId(Long foodId) { this.foodId = foodId; }
        
        public Long getFoodId() { return foodId; }
        public void setFoodId(Long foodId) { this.foodId = foodId; }
    }
}

