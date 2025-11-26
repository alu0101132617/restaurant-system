package es.ull.esit.app.middleware.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Cashier model class
 */
public class Cashier {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("salary")
    private Integer salary;

    public Cashier() {}

    /**
     * Constructor with parameters
     * @param id the cashier ID
     * @param name the cashier name
     * @param salary the cashier salary
     */
    public Cashier(Long id, String name, Integer salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }
    /**
     * Getters and Setters
     * @return values of the attributes
     */
    public Long getId() { return id; }

    /**
     * Set ID   
     * @param id the cashier ID
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Get name
     * @return the cashier name
     */
    public String getName() { return name; }
    
    /**
     * Set name
     * @param name the cashier name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Get salary
     * @return the cashier salary
     */
    public Integer getSalary() { return salary; }

    /**
     * Set salary
     * @param salary the cashier salary
     */
    public void setSalary(Integer salary) { this.salary = salary; }
}