package com.example.controllers;

import com.example.dao.UserDAO;
import com.example.models.User;

public class AuthController {
    private UserDAO userDAO = new UserDAO();

    // Register a user
    public void registerUser(String username, String email, String password, String role) {
        User user = new User(username, email, password, role);
        if (userDAO.registerUser(user)) {
            System.out.println("✅ User registered successfully!");
        } else {
            System.out.println("❌ User registration failed.");
        }
    }

    // Login a user
    public void loginUser(String email, String password) {
        User user = userDAO.loginUser(email, password);
        if (user != null) {
            System.out.println("✅ Login successful! Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        } else {
            System.out.println("❌ Login failed. Invalid credentials.");
        }
    }
}
