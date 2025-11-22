package es.ull.esit.app.middleware.model;

import jakarta.persistence.*;

@Entity
@Table(name = "drinks")
public class Drink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    public Drink() {}
    public Drink(Long id,String name,Integer price){this.id=id;this.name=name;this.price=price;}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public Integer getPrice(){return price;} public void setPrice(Integer price){this.price=price;}
}

