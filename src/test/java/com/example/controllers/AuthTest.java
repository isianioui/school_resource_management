package com.example.controllers;

import org.junit.jupiter.api.Test;

public class AuthTest {

    @Test
    public void testRegistration() {
        AuthController auth = new AuthController();
        auth.registerUser("isianioui", "isianioui@gmail.com", "isianioui", "admin");
    }

    @Test
    public void testLogin() {
        AuthController auth = new AuthController();
        auth.loginUser("isianioui@gmail.com", "$2a$10$.nWDX57kZB7xa54OErs5JOVyQsjeUf9LVzD7YoRAC3RC/T7a81EYq");
    }
}
