package es.ull.esit.app.middleware.service;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import java.util.List;

/**
 * Service class handling business logic for products.
 * Decouples the UI (Swing) from the Data Access (API).
 */
public class ProductService {

    private final ApiClient client;

    public ProductService(ApiClient client) {
        this.client = client;
    }

    // ==================== DRINKS LOGIC ====================

    public List<Drink> getAllDrinks() throws Exception {
        return client.getAllDrinks();
    }

    public Drink getDrinkById(Long id) throws Exception {
        return client.getDrinkById(id);
    }

    public void addDrink(String name, String priceStr) throws Exception {
        int price = validateAndParsePrice(priceStr);
        validateName(name);
        
        Drink newDrink = new Drink(null, name, price, null);
        client.createDrink(newDrink);
    }

    public void updateDrink(Long id, String name, String priceStr) throws Exception {
        if (id == null) throw new IllegalArgumentException("No drink selected!");
        
        int price = validateAndParsePrice(priceStr);
        validateName(name);

        Drink updatedDrink = new Drink(id, name, price, null);
        client.updateDrink(id, updatedDrink);
    }

    // ==================== APPETIZERS LOGIC ====================

    public List<Appetizer> getAllAppetizers() throws Exception {
        return client.getAllAppetizers();
    }

    public Appetizer getAppetizerById(Long id) throws Exception {
        return client.getAppetizerById(id);
    }

    public void addAppetizer(String name, String priceStr) throws Exception {
        int price = validateAndParsePrice(priceStr);
        validateName(name);

        Appetizer newAppetizer = new Appetizer(null, name, price, null);
        client.createAppetizer(newAppetizer);
    }

    public void updateAppetizer(Long id, String name, String priceStr) throws Exception {
        if (id == null) throw new IllegalArgumentException("No appetizer selected!");
        
        int price = validateAndParsePrice(priceStr);
        validateName(name);

        Appetizer updatedAppetizer = new Appetizer(id, name, price, null);
        client.updateAppetizer(id, updatedAppetizer);
    }

    // ==================== MAIN COURSE LOGIC ====================

    public List<MainCourse> getAllMainCourses() throws Exception {
        return client.getAllMainCourses();
    }

    public MainCourse getMainCourseById(Long id) throws Exception {
        return client.getMainCourseById(id);
    }

    public void addMainCourse(String name, String priceStr) throws Exception {
        int price = validateAndParsePrice(priceStr);
        validateName(name);

        MainCourse newMainCourse = new MainCourse(null, name, price, null);
        client.createMainCourse(newMainCourse);
    }

    public void updateMainCourse(Long id, String name, String priceStr) throws Exception {
        if (id == null) throw new IllegalArgumentException("No main course selected!");
        
        int price = validateAndParsePrice(priceStr);
        validateName(name);

        MainCourse updatedMainCourse = new MainCourse(id, name, price, null);
        client.updateMainCourse(id, updatedMainCourse);
    }

    // ==================== VALIDATION HELPERS ====================

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty.");
        }
    }

    private int validateAndParsePrice(String priceStr) {
        if (priceStr == null || priceStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Price cannot be empty.");
        }
        try {
            int price = Integer.parseInt(priceStr.trim());
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative.");
            }
            return price;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Price must be a valid whole number.");
        }
    }
}