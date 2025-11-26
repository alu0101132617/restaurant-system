package es.ull.esit.app.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.http.*;
import java.net.URI;
import java.time.Duration;
import java.util.List;

import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Cashier;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;

/**
 * ApiClient is a middleware class that handles HTTP requests to a RESTful API.
 * It provides methods to perform CRUD operations on various resources such as
 * Appetizers, Cashiers, Drinks, and MainCourses.
 */
public class ApiClient {
    private final HttpClient http;
    private final String baseUrl;
    private final ObjectMapper mapper;

    /**
     * Constructs an ApiClient with the specified base URL.
     *
     * @param baseUrl The base URL of the API.
     */
    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.mapper = new ObjectMapper();
    }

    // Generic method for GET requests returning objects
    private <T> T get(String path, Class<T> responseType) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(res.body(), responseType);
    }

    /**
     * Generic method for GET requests returning lists.
     *
     * @param <T> The type of the elements in the list.
     * @param path The API endpoint path.
     * @param typeRef The type reference for the list.
     * @return A list of objects of type T.
     * @throws Exception If an error occurs during the request.
     */
    private <T> List<T> getList(String path, TypeReference<List<T>> typeRef) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(res.body(), typeRef);
    }

    /**
     * Generic method for POST requests.
     * @param <T> The type of the request body.
     * @param <R> The type of the response body.
     * @param path The API endpoint path.
     * @param body the request body
     * @param responseType the class of the response type
     * @return An object of type R.
     * @throws Exception If an error occurs during the request.
     */
    private <T, R> R post(String path, T body, Class<R> responseType) throws Exception {
        String json = mapper.writeValueAsString(body);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(res.body(), responseType);
    }

    /**
     * GET all appetizers.
     * @return a list of Appetizer objects
     * @throws Exception if an error occurs during the request
     */
    public List<Appetizer> getAllAppetizers() throws Exception {
        return getList("/api/appetizers", new TypeReference<List<Appetizer>>() {});
    }

    /**
     * GET appetizer by ID.
     * @param id the appetizer ID
     * @return an Appetizer object
     * @throws Exception if an error occurs during the request
     */
    public Appetizer getAppetizerById(Long id) throws Exception {
        return get("/api/appetizers/" + id, Appetizer.class);
    }

    /**
     * POST create new appetizer.
     * @param appetizer the Appetizer object to create
     * @return an Appetizer object
     * @throws Exception if an error occurs during the request
     */
    public Appetizer createAppetizer(Appetizer appetizer) throws Exception {
        return post("/api/appetizers", appetizer, Appetizer.class);
    }

    /**
     * PUT update appetizer.
     * @param id the appetizer ID
     * @param appetizer the Appetizer object to update
     * @return an Appetizer object
     * @throws Exception if an error occurs during the request
    */
    public Appetizer updateAppetizer(Long id, Appetizer appetizer) throws Exception {
        String json = mapper.writeValueAsString(appetizer);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/appetizers/" + id))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(res.body(), Appetizer.class);
    }

    /**
     * DELETE appetizer.
     * @param id the appetizer ID
     * @throws Exception if an error occurs during the request
     */
    public void deleteAppetizer(Long id) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/appetizers/" + id))
                .DELETE()
                .build();
        http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    /** GET all cashiers
     * @return a list of Cashier objects
     * @throws Exception if an error occurs during the request
     */
    public List<Cashier> getAllCashiers() throws Exception {
        return getList("/api/cashiers", new TypeReference<List<Cashier>>() {});
    }

    /** GET cashier by ID
     * @param id the cashier ID
     * @return a Cashier object
     * @throws Exception if an error occurs during the request
     */
    public Cashier getCashierById(Long id) throws Exception {
        return get("/api/cashiers/" + id, Cashier.class);
    }

    /** POST create new cashier
     * @param cashier the Cashier object to create
     * @return a Cashier object
     * @throws Exception if an error occurs during the request
     */
    public Cashier createCashier(Cashier cashier) throws Exception {
        return post("/api/cashiers", cashier, Cashier.class);
    }

    /** PUT update cashier
     * @param id the cashier ID
     * @param cashier the Cashier object to update
     * @return a Cashier object
     * @throws Exception if an error occurs during the request
     */
    public Cashier updateCashier(Long id, Cashier cashier) throws Exception {
        String json = mapper.writeValueAsString(cashier);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/cashiers/" + id))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(res.body(), Cashier.class);
    }

    /** DELETE cashier
     * @param id the cashier ID
     * @throws Exception if an error occurs during the request
     */
    public void deleteCashier(Long id) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/cashiers/" + id))
                .DELETE()
                .build();
        http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    /** GET all drinks
     * @return a list of Drink objects
     * @throws Exception if an error occurs during the request
     */
    public List<Drink> getAllDrinks() throws Exception {
        return getList("/api/drinks", new TypeReference<List<Drink>>() {});
    }

    /**
     * GET drink by ID
     * @param id the drink ID
     * @return a Drink object
     * @throws Exception if an error occurs during the request
     */
    public Drink getDrinkById(Long id) throws Exception {
        return get("/api/drinks/" + id, Drink.class);
    }

    /** POST create new drink
     * @param drink the Drink object to create
     * @return a Drink object
     * @throws Exception if an error occurs during the request
     */
    public Drink createDrink(Drink drink) throws Exception {
        return post("/api/drinks", drink, Drink.class);
    }

    /** PUT update drink
     * @param id the drink ID
     * @param drink the Drink object to update
     * @return a Drink object
     * @throws Exception if an error occurs during the request
     */
    public Drink updateDrink(Long id, Drink drink) throws Exception {
        String json = mapper.writeValueAsString(drink);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/drinks/" + id))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(res.body(), Drink.class);
    }

    /** DELETE drink
     * @param id the drink ID
     * @throws Exception if an error occurs during the request
     */
    public void deleteDrink(Long id) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/drinks/" + id))
                .DELETE()
                .build();
        http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    /** GET all maincourses
     * @return a list of MainCourse objects
     * @throws Exception if an error occurs during the request
     */
    public List<MainCourse> getAllMainCourses() throws Exception {
        return getList("/api/maincourses", new TypeReference<List<MainCourse>>() {});
    }

    /** GET maincourse by ID
     * @param id the maincourse ID
     * @return a MainCourse object
     * @throws Exception if an error occurs during the request
     */
    public MainCourse getMainCourseById(Long id) throws Exception {
        return get("/api/maincourses/" + id, MainCourse.class);
    }

    /** POST create new maincourse
     * @param mainCourse the MainCourse object to create
     * @return a MainCourse object
     * @throws Exception if an error occurs during the request
     */
    public MainCourse createMainCourse(MainCourse mainCourse) throws Exception {
        return post("/api/maincourses", mainCourse, MainCourse.class);
    }

    /** PUT update maincourse
     * @param id the maincourse ID
     * @param mainCourse the MainCourse object to update
     * @return a MainCourse object
     * @throws Exception if an error occurs during the request
     */
    public MainCourse updateMainCourse(Long id, MainCourse mainCourse) throws Exception {
        String json = mapper.writeValueAsString(mainCourse);
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/maincourses/" + id))
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(res.body(), MainCourse.class);
    }

    /** DELETE maincourse
     * @param id the maincourse ID
     * @throws Exception if an error occurs during the request
     */
    public void deleteMainCourse(Long id) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/maincourses/" + id))
                .DELETE()
                .build();
        http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    // Placeholder login/ping method
    // Algunas clases del front llaman a `api.login(String)` como ping/logout.
    // Implementamos un método vacío para mantener compatibilidad con la UI.
    public void login(String ignored) throws Exception {
        // No-op: si en el futuro se añade autenticación, aquí se puede implementar.
    }
}