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

public class ApiClient {
    private final HttpClient http;
    private final String baseUrl;
    private final ObjectMapper mapper;

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

    // Generic method for GET requests returning lists
    private <T> List<T> getList(String path, TypeReference<List<T>> typeRef) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(res.body(), typeRef);
    }

    // Generic method for POST requests
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

    // GET all appetizers
    public List<Appetizer> getAllAppetizers() throws Exception {
        return getList("/api/appetizers", new TypeReference<List<Appetizer>>() {});
    }

    // GET appetizer by ID
    public Appetizer getAppetizerById(Long id) throws Exception {
        return get("/api/appetizers/" + id, Appetizer.class);
    }

    // POST create new appetizer
    public Appetizer createAppetizer(Appetizer appetizer) throws Exception {
        return post("/api/appetizers", appetizer, Appetizer.class);
    }

    // PUT update appetizer
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

    // DELETE appetizer
    public void deleteAppetizer(Long id) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/appetizers/" + id))
                .DELETE()
                .build();
        http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    // GET all cashiers
    public List<Cashier> getAllCashiers() throws Exception {
        return getList("/api/cashiers", new TypeReference<List<Cashier>>() {});
    }

    // GET cashier by ID
    public Cashier getCashierById(Long id) throws Exception {
        return get("/api/cashiers/" + id, Cashier.class);
    }

    // POST create new cashier
    public Cashier createCashier(Cashier cashier) throws Exception {
        return post("/api/cashiers", cashier, Cashier.class);
    }

    // PUT update cashier
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

    // DELETE cashier
    public void deleteCashier(Long id) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/cashiers/" + id))
                .DELETE()
                .build();
        http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    // GET all drinks
    public List<Drink> getAllDrinks() throws Exception {
        return getList("/api/drinks", new TypeReference<List<Drink>>() {});
    }

    // GET drink by ID
    public Drink getDrinkById(Long id) throws Exception {
        return get("/api/drinks/" + id, Drink.class);
    }

    // POST create new drink
    public Drink createDrink(Drink drink) throws Exception {
        return post("/api/drinks", drink, Drink.class);
    }

    // PUT update drink
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

    // DELETE drink
    public void deleteDrink(Long id) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/drinks/" + id))
                .DELETE()
                .build();
        http.send(req, HttpResponse.BodyHandlers.ofString());
    }

    // GET all maincourses
    public List<MainCourse> getAllMainCourses() throws Exception {
        return getList("/api/maincourses", new TypeReference<List<MainCourse>>() {});
    }

    // GET maincourse by ID
    public MainCourse getMainCourseById(Long id) throws Exception {
        return get("/api/maincourses/" + id, MainCourse.class);
    }

    // POST create new maincourse
    public MainCourse createMainCourse(MainCourse mainCourse) throws Exception {
        return post("/api/maincourses", mainCourse, MainCourse.class);
    }

    // PUT update maincourse
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

    // DELETE maincourse
    public void deleteMainCourse(Long id) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/maincourses/" + id))
                .DELETE()
                .build();
        http.send(req, HttpResponse.BodyHandlers.ofString());
    }
}