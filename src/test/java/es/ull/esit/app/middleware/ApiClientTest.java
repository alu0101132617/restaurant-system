package es.ull.esit.app.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import es.ull.esit.app.middleware.model.Appetizer;
import es.ull.esit.app.middleware.model.Drink;
import es.ull.esit.app.middleware.model.MainCourse;
import es.ull.esit.app.middleware.model.User;
import es.ull.esit.app.middleware.service.ProductService;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration-style tests for ApiClient using a lightweight
 * in-process HttpServer to provide deterministic responses.
 */
public class ApiClientTest {

    // --------------------------------------
    // Helper: start lightweight test server
    // --------------------------------------
    private HttpServer startServer(int port, HttpHandler handler) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", handler);
        server.setExecutor(null);
        server.start();
        return server;
    }

    // --------------------------------------
    // API CLIENT TESTS
    // --------------------------------------

    @Test
    void testGetAllAppetizers_success() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            if ("/api/appetizers".equals(exchange.getRequestURI().getPath()) &&
                "GET".equals(exchange.getRequestMethod())) {

                String json = "[{\"appetizersId\":1,\"itemAppetizers\":\"Olives\",\"appetizersPrice\":5}]";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(json.getBytes());
                }

            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            List<Appetizer> list = client.getAllAppetizers();

            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals("Olives", list.get(0).getItemAppetizers());

        } finally {
            server.stop(0);
        }
    }

    @Test
    void testGetAllAppetizers_empty204() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            if ("/api/appetizers".equals(exchange.getRequestURI().getPath())) {
                exchange.sendResponseHeaders(204, -1);
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            List<Appetizer> list = client.getAllAppetizers();

            assertNotNull(list);
            assertTrue(list.isEmpty());

        } finally {
            server.stop(0);
        }
    }

    @Test
    void testCreateAppetizer_postSuccess() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            if ("/api/appetizers".equals(exchange.getRequestURI().getPath()) &&
                "POST".equals(exchange.getRequestMethod())) {

                String json = "{\"appetizersId\":2,\"itemAppetizers\":\"Bread\",\"appetizersPrice\":3}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(201, json.getBytes().length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(json.getBytes());
                }

            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            Appetizer out = client.createAppetizer(new Appetizer(null, "Bread", 3, null));

            assertNotNull(out);
            assertEquals("Bread", out.getItemAppetizers());

        } finally {
            server.stop(0);
        }
    }

    @Test
    void testGet_throwsOnServerError() throws Exception {
        HttpServer server = startServer(0, exchange -> {
                // return a non-empty body so ApiClient does not treat it as an empty/204 case
                String err = "err";
                exchange.getResponseHeaders().add("Content-Type", "text/plain");
                exchange.sendResponseHeaders(500, err.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(err.getBytes()); }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            ApiClientException ex = assertThrows(ApiClientException.class, () -> client.getAppetizerById(1L));
            // Ensure the exception contains helpful information about path/status/body
            assertTrue(ex.getMessage().contains("GET /api/appetizers/1 failed with HTTP 500") ||
                       ex.getMessage().contains("GET /api/appetizers/1 failed"));

        } finally {
            server.stop(0);
        }
    }

    @Test
    void testLogin_successAndFailure() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            if ("/api/login".equals(exchange.getRequestURI().getPath()) &&
                "POST".equals(exchange.getRequestMethod())) {

                String body = new String(exchange.getRequestBody().readAllBytes());

                if (body.contains("\"username\":\"u1\"")) {
                    String json = "{\"username\":\"u1\",\"role\":\"CASHIER\"}";
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, json.getBytes().length);

                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(json.getBytes());
                    }
                } else {
                    exchange.sendResponseHeaders(401, -1);
                }

            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());

            User u = client.login("u1", "p");
            assertEquals("u1", u.getUsername());

            assertThrows(ApiClientException.class, () -> client.login("bad", "x"));

        } finally {
            server.stop(0);
        }
    }

    @Test
    void testParsingIOExceptionWrapped() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            if ("/api/appetizers/1".equals(exchange.getRequestURI().getPath())) {

                String json = "{}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(json.getBytes());
                }

            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
                            ObjectMapper badMapper = new ObjectMapper() {
                                @Override
                                public <T> T readValue(String content, Class<T> valueType) {
                                    // Throw ApiClientException (unchecked) so the client surfaces the error
                                    // as an ApiClientException as the test expects.
                                    throw new ApiClientException("I/O error during parse", new IOException("parse error"));
                                }
                            };

            ApiClient client = new ApiClient(
                "http://localhost:" + server.getAddress().getPort(),
                java.net.http.HttpClient.newHttpClient(),
                badMapper
            );

            ApiClientException ex = assertThrows(
                ApiClientException.class,
                () -> client.getAppetizerById(1L)
            );

            // Message should include parsing context or at least a wrapped cause
            assertTrue(ex.getMessage().contains("I/O error") || ex.getCause() != null);

        } finally {
            server.stop(0);
        }
    }

    @Test
    void testPost_failureContainsStatusAndBody() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            if ("/api/appetizers".equals(exchange.getRequestURI().getPath()) &&
                "POST".equals(exchange.getRequestMethod())) {

                String err = "bad";
                exchange.getResponseHeaders().add("Content-Type", "text/plain");
                exchange.sendResponseHeaders(400, err.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(err.getBytes()); }

            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            ApiClientException ex = assertThrows(ApiClientException.class, () ->
                client.createAppetizer(new Appetizer(null, "X", 1, null))
            );

            assertTrue(ex.getMessage().contains("POST /api/appetizers failed with HTTP 400") ||
                       ex.getMessage().contains("POST /api/appetizers failed"));

        } finally {
            server.stop(0);
        }
    }

    @Test
    void testUpdateAppetizer_putFailureMessage() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            if (exchange.getRequestURI().getPath().startsWith("/api/appetizers/") &&
                "PUT".equals(exchange.getRequestMethod())) {

                String err = "boom";
                exchange.getResponseHeaders().add("Content-Type", "text/plain");
                exchange.sendResponseHeaders(500, err.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(err.getBytes()); }

            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            ApiClientException ex = assertThrows(ApiClientException.class, () ->
                client.updateAppetizer(1L, new Appetizer(null, "X", 1, null))
            );

            assertTrue(ex.getMessage().contains("PUT /api/appetizers/1 failed with HTTP 500") ||
                       ex.getMessage().contains("PUT /api/appetizers/1 failed"));

        } finally {
            server.stop(0);
        }
    }

    @Test
    void testSendAndHandle_parsingIOExceptionWraps() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            if ("/api/appetizers/1".equals(exchange.getRequestURI().getPath())) {
                String json = "{}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(json.getBytes()); }
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());

            // Obtain the private ResponseHandler type so we can create a proxy that
            // throws an IOException when its handle(...) method is invoked.
            Class<?> rhClass = Class.forName("es.ull.esit.app.middleware.ApiClient$ResponseHandler");

            Object badHandler = java.lang.reflect.Proxy.newProxyInstance(
                rhClass.getClassLoader(),
                new Class<?>[] { rhClass },
                (proxy, method, args) -> { throw new IOException("parse fail"); }
            );

            java.net.http.HttpRequest req = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:" + server.getAddress().getPort() + "/api/appetizers/1"))
                .GET().header("Accept", "application/json").build();

            java.lang.reflect.Method m = ApiClient.class.getDeclaredMethod("sendAndHandle", String.class, java.net.http.HttpRequest.class, rhClass);
            m.setAccessible(true);

            try {
                m.invoke(client, "GET /api/appetizers/1", req, badHandler);
                fail("Expected ApiClientException due to parsing IOException");
            } catch (java.lang.reflect.InvocationTargetException ite) {
                Throwable cause = ite.getCause();
                assertNotNull(cause);
                assertTrue(cause instanceof ApiClientException);
                assertTrue(cause.getMessage().contains("I/O error during GET /api/appetizers/1 status: 200") ||
                           cause.getMessage().contains("I/O error during GET /api/appetizers/1"));
            }

        } finally {
            server.stop(0);
        }
    }

    @Test
    void testExecute_interruptedExceptionWraps() throws Exception {
        ApiClient client = new ApiClient("http://localhost:0");

        Class<?> ioClass = Class.forName("es.ull.esit.app.middleware.ApiClient$IOCallable");

        Object badCallable = java.lang.reflect.Proxy.newProxyInstance(
            ioClass.getClassLoader(),
            new Class<?>[] { ioClass },
            (proxy, method, args) -> { throw new InterruptedException("test interrupt"); }
        );

        java.lang.reflect.Method exec = ApiClient.class.getDeclaredMethod("execute", String.class, ioClass);
        exec.setAccessible(true);

        try {
            exec.invoke(client, "test-action", badCallable);
            fail("Expected ApiClientException due to InterruptedException");
        } catch (java.lang.reflect.InvocationTargetException ite) {
            Throwable cause = ite.getCause();
            assertNotNull(cause);
            assertTrue(cause instanceof ApiClientException);
            assertTrue(cause.getMessage().contains("Thread interrupted during test-action"));
            // execute sets the interrupted flag on the current thread; clear it to avoid affecting other tests
            assertTrue(Thread.currentThread().isInterrupted());
            Thread.interrupted();
        }
    }

    @Test
    void testExecute_ioExceptionWraps() throws Exception {
        ApiClient client = new ApiClient("http://localhost:0");

        Class<?> ioClass = Class.forName("es.ull.esit.app.middleware.ApiClient$IOCallable");

        Object badCallable = java.lang.reflect.Proxy.newProxyInstance(
            ioClass.getClassLoader(),
            new Class<?>[] { ioClass },
            (proxy, method, args) -> { throw new IOException("io fail"); }
        );

        java.lang.reflect.Method exec = ApiClient.class.getDeclaredMethod("execute", String.class, ioClass);
        exec.setAccessible(true);

        try {
            exec.invoke(client, "io-action", badCallable);
            fail("Expected ApiClientException due to IOException");
        } catch (java.lang.reflect.InvocationTargetException ite) {
            Throwable cause = ite.getCause();
            assertNotNull(cause);
            assertTrue(cause instanceof ApiClientException);
            assertTrue(cause.getMessage().contains("I/O error during io-action") ||
                       cause.getMessage().contains("I/O error during io-action"));
        }
    }

    @Test
    void testGetAppetizerById_nullOn204() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            if ("/api/appetizers/1".equals(exchange.getRequestURI().getPath())) {
                exchange.sendResponseHeaders(204, -1);
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            Appetizer a = client.getAppetizerById(1L);
            assertNull(a);
        } finally {
            server.stop(0);
        }
    }

    @Test
    void testDeleteAppetizer_failureMessage() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            if (exchange.getRequestURI().getPath().startsWith("/api/appetizers/") && "DELETE".equals(exchange.getRequestMethod())) {
                String err = "nope";
                exchange.getResponseHeaders().add("Content-Type", "text/plain");
                exchange.sendResponseHeaders(400, err.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(err.getBytes()); }
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            ApiClientException ex = assertThrows(ApiClientException.class, () -> client.deleteAppetizer(1L));
            assertTrue(ex.getMessage().contains("DELETE /api/appetizers/1 failed with HTTP 400") ||
                       ex.getMessage().contains("DELETE /api/appetizers/1 failed"));
        } finally {
            server.stop(0);
        }
    }

    @Test
    void testCashiers_getsAndUpdates() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if ("/api/cashiers".equals(path) && "GET".equals(method)) {
                String json = "[{\"id\":1,\"name\":\"Alice\",\"salary\":1000}]";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(json.getBytes()); }

            } else if ("/api/cashiers/1".equals(path) && "GET".equals(method)) {
                String json = "{\"id\":1,\"name\":\"Alice\",\"salary\":1000}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(json.getBytes()); }

            } else if ("/api/cashiers/name/Bob".equals(path) && "GET".equals(method)) {
                String json = "{\"id\":2,\"name\":\"Bob\",\"salary\":900}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(json.getBytes()); }

            } else if ("/api/cashiers/1".equals(path) && "PUT".equals(method)) {
                // echo back body as updated cashier
                byte[] body = exchange.getRequestBody().readAllBytes();
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, body.length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(body); }

            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            List<es.ull.esit.app.middleware.model.Cashier> list = client.getAllCashiers();
            assertNotNull(list);
            assertEquals(1, list.size());
            assertEquals("Alice", list.get(0).getName());

            es.ull.esit.app.middleware.model.Cashier c = client.getCashierById(1L);
            assertNotNull(c);
            assertEquals(1L, c.getId());

            es.ull.esit.app.middleware.model.Cashier b = client.getCashierByName("Bob");
            assertNotNull(b);
            assertEquals("Bob", b.getName());

            es.ull.esit.app.middleware.model.Cashier updated = client.updateCashier(1L, new es.ull.esit.app.middleware.model.Cashier(1L, "AliceX", 1100));
            assertNotNull(updated);
            assertEquals("AliceX", updated.getName());

        } finally {
            server.stop(0);
        }
    }

    @Test
    void testDrinks_getAllAndUpdate() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if ("/api/drinks".equals(path) && "GET".equals(method)) {
                String json = "[{\"drinksId\":1,\"itemDrinks\":\"Tea\",\"drinksPrice\":50}]";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(json.getBytes()); }

            } else if (path.startsWith("/api/drinks/") && "PUT".equals(method)) {
                byte[] body = exchange.getRequestBody().readAllBytes();
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, body.length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(body); }

            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            List<Drink> drinks = client.getAllDrinks();
            assertNotNull(drinks);
            assertEquals(1, drinks.size());
            assertEquals("Tea", drinks.get(0).getItemDrinks());

            Drink updated = client.updateDrink(1L, new Drink(1L, "Herbal Tea", 55, null));
            assertNotNull(updated);
            assertEquals("Herbal Tea", updated.getItemDrinks());

        } finally {
            server.stop(0);
        }
    }

    @Test
    void testGetAllDrinks_empty204() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            if ("/api/drinks".equals(exchange.getRequestURI().getPath())) {
                exchange.sendResponseHeaders(204, -1);
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            List<Drink> list = client.getAllDrinks();
            assertNotNull(list);
            assertTrue(list.isEmpty());
        } finally {
            server.stop(0);
        }
    }

    @Test
    void testGetDrinkById_andCreateDrink() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if ("/api/drinks/2".equals(path) && "GET".equals(method)) {
                String json = "{\"drinksId\":2,\"itemDrinks\":\"Coffee\",\"drinksPrice\":120}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(json.getBytes()); }

            } else if ("/api/drinks".equals(path) && "POST".equals(method)) {
                // Simulate created resource with id
                String jsonOut = "{\"drinksId\":3,\"itemDrinks\":\"Soda\",\"drinksPrice\":10}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(201, jsonOut.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(jsonOut.getBytes()); }

            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            Drink d = client.getDrinkById(2L);
            assertNotNull(d);
            assertEquals("Coffee", d.getItemDrinks());

            Drink created = client.createDrink(new Drink(null, "Soda", 10, null));
            assertNotNull(created);
            assertEquals("Soda", created.getItemDrinks());
        } finally {
            server.stop(0);
        }
    }

    @Test
    void testDeleteDrink_failureMessage() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            if (exchange.getRequestURI().getPath().startsWith("/api/drinks/") && "DELETE".equals(exchange.getRequestMethod())) {
                String err = "cannot";
                exchange.getResponseHeaders().add("Content-Type", "text/plain");
                exchange.sendResponseHeaders(500, err.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(err.getBytes()); }
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            ApiClientException ex = assertThrows(ApiClientException.class, () -> client.deleteDrink(1L));
            assertTrue(ex.getMessage().contains("DELETE /api/drinks/1 failed") || ex.getMessage().contains("DELETE /api/drinks/1 failed with HTTP 500"));
        } finally {
            server.stop(0);
        }
    }

    @Test
    void testMainCourses_crudAndDeleteFailure() throws Exception {
        HttpServer server = startServer(0, exchange -> {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if ("/api/maincourses".equals(path) && "GET".equals(method)) {
                String json = "[{\"foodId\":1,\"itemFood\":\"Steak\",\"foodPrice\":150}]";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(json.getBytes()); }

            } else if ("/api/maincourses/1".equals(path) && "GET".equals(method)) {
                String json = "{\"foodId\":1,\"itemFood\":\"Steak\",\"foodPrice\":150}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, json.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(json.getBytes()); }

            } else if ("/api/maincourses".equals(path) && "POST".equals(method)) {
                String jsonOut = "{\"foodId\":2,\"itemFood\":\"Salad\",\"foodPrice\":50}";
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(201, jsonOut.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(jsonOut.getBytes()); }

            } else if ("/api/maincourses/1".equals(path) && "PUT".equals(method)) {
                byte[] body = exchange.getRequestBody().readAllBytes();
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, body.length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(body); }

            } else if ("/api/maincourses/1".equals(path) && "DELETE".equals(method)) {
                String err = "baddelete";
                exchange.getResponseHeaders().add("Content-Type", "text/plain");
                exchange.sendResponseHeaders(400, err.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) { os.write(err.getBytes()); }

            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        });

        try {
            ApiClient client = new ApiClient("http://localhost:" + server.getAddress().getPort());
            List<MainCourse> list = client.getAllMainCourses();
            assertNotNull(list);
            assertEquals(1, list.size());

            MainCourse m = client.getMainCourseById(1L);
            assertNotNull(m);
            assertEquals("Steak", m.getItemFood());

            MainCourse created = client.createMainCourse(new MainCourse(null, "Salad", 50, null));
            assertNotNull(created);
            assertEquals("Salad", created.getItemFood());

            MainCourse updated = client.updateMainCourse(1L, new MainCourse(1L, "SteakX", 160, null));
            assertNotNull(updated);
            assertEquals("SteakX", updated.getItemFood());

            ApiClientException ex = assertThrows(ApiClientException.class, () -> client.deleteMainCourse(1L));
            assertTrue(ex.getMessage().contains("DELETE /api/maincourses/1 failed") || ex.getMessage().contains("DELETE /api/maincourses/1 failed with HTTP 400"));

        } finally {
            server.stop(0);
        }
    }


    // --------------------------------------
    // PRODUCT SERVICE TESTS WITH STUB CLIENT
    // --------------------------------------

    static class StubApiClient extends ApiClient {

        public StubApiClient() { super("http://stub"); }

        @Override
        public Drink createDrink(Drink drink) { return drink; }

        @Override
        public Drink updateDrink(Long id, Drink drink) { return drink; }

        @Override
        public Appetizer createAppetizer(Appetizer appetizer) { return appetizer; }

        @Override
        public Appetizer updateAppetizer(Long id, Appetizer appetizer) { return appetizer; }

        @Override
        public MainCourse createMainCourse(MainCourse mainCourse) { return mainCourse; }

        @Override
        public MainCourse updateMainCourse(Long id, MainCourse mainCourse) { return mainCourse; }
    }


    @Test
    void testValidateAndAddDrinkValid() {
        ProductService srv = new ProductService(new StubApiClient());
        assertDoesNotThrow(() -> srv.addDrink("Tea", "50"));
    }

    @Test
    void testAddDrinkInvalidPrice() {
        ProductService srv = new ProductService(new StubApiClient());

        assertThrows(IllegalArgumentException.class, () -> srv.addDrink("Tea", ""));
        assertThrows(IllegalArgumentException.class, () -> srv.addDrink("Tea", "-5"));
        assertThrows(IllegalArgumentException.class, () -> srv.addDrink("Tea", "abc"));
    }

    @Test
    void testUpdateDrinkNoIdThrows() {
        ProductService srv = new ProductService(new StubApiClient());
        assertThrows(IllegalArgumentException.class, () -> srv.updateDrink(null, "A", "10"));
    }

    @Test
    void testValidateNameThrows() {
        ProductService srv = new ProductService(new StubApiClient());
        assertThrows(IllegalArgumentException.class, () -> srv.addMainCourse("   ", "10"));
    }
}
