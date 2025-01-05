package com.example.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.example.dao.UserDAO;
import com.example.database.databaseConnection;
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
   public User loginUser(String email, String plainPassword) {
    String query = "SELECT user_id, username, email, password, role FROM users WHERE email = ?";
    
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String storedHash = rs.getString("password");
            
            if (BCrypt.checkpw(plainPassword, storedHash)) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                System.out.println("✅ Login successful!");
                return user;
            }
        }
        System.out.println("❌ Invalid credentials");
        
    } catch (SQLException e) {
        System.err.println("❌ Database error during login: " + e.getMessage());
    }
    return null;
}

}
