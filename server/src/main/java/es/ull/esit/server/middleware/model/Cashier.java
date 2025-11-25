package es.ull.esit.server.middleware.model;

import javax.persistence.*;

@Entity
@Table(name = "Cashier")
public class Cashier {
    @Id
    @Column(name = "Cashier_id")
    private Long id;

    @Column(name = "Cashier_name")
    private String name;

    @Column(name = "Cashier_salary")
    private Integer salary;

    public Cashier() {}

    public Cashier(Long id, String name, Integer salary) {
        this.id = id; this.name = name; this.salary = salary;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getSalary() { return salary; }
    public void setSalary(Integer salary) { this.salary = salary; }
}

