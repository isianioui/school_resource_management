package com.example.controllers;

import org.junit.jupiter.api.Test;

import com.example.database.databaseConnection;
import com.example.models.User;

public class AuthTest {

    @Test
    public void testRegistration() {
        AuthController auth = new AuthController();
        auth.registerUser("moha", "moha@gmail.com", "123", "admin");
    }

    @Test
    public void testLogin() {
        AuthController auth = new AuthController();
        auth.loginUser("moha@gmail.com", "123");
    }


}



