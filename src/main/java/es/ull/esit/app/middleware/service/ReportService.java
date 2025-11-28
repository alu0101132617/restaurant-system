package es.ull.esit.app.middleware.service;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Cashier;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import java.util.List;

/**
 * Service for reporting, statistics, and general system info.
 * Relieves the Cashier UI from direct API calls.
 */
public class ReportService {
    
    private final ApiClient client;

    public ReportService(ApiClient client) {
        this.client = client;
    }

    /**
     * Loads all cashier information.
     */
    public List<Cashier> getCashierInfo() throws Exception {
        return client.getAllCashiers();
    }
    
    /**
     * Checks backend connectivity and counts menu items.
     * Serves as a pre-flight check before opening the order window.
     * @return A status string for the log
     */
    public String checkMenuStatus() throws Exception {
        // Fetch lists to verify connectivity
        List<Appetizer> appetizers = client.getAllAppetizers();
        List<Drink> drinks = client.getAllDrinks();
        List<MainCourse> mainCourses = client.getAllMainCourses();
        
        return String.format("Menu System Online: %d appetizers, %d drinks, %d main courses available.",
                appetizers.size(), drinks.size(), mainCourses.size());
    }
}