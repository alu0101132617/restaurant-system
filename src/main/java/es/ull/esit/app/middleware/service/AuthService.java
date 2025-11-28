package es.ull.esit.app.middleware.service;

import es.ull.esit.app.middleware.ApiClient;
import es.ull.esit.app.middleware.model.User;

/**
 * Service to handle authentication logic.
 */
public class AuthService {
    
    private final ApiClient client;

    public AuthService(ApiClient client) {
        this.client = client;
    }

    /**
     * Attempts to log in a user.
     * @param username The username input
     * @param password The password input
     * @return The authenticated User object
     * @throws Exception if validation fails or server returns error
     */
    public User authenticate(String username, String password) throws Exception {
        // 1. Local Validation
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        // 2. Call API
        return client.login(username, password);
    }
}