package com.example.dao;

import com.example.database.databaseConnection;
import com.example.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Register new user
    public boolean registerUser(User user) {
        String query = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Hash the password before storing it in the database
            // String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
            // In registerUser method
String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(10));
System.out.println("Password being stored during registration: " + hashedPassword);

            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, hashedPassword);  // Store the hashed password
            pstmt.setString(4, user.getRole());
             
            int result = pstmt.executeUpdate();
            return result > 0; // Registration successful
        } catch (SQLException e) {
            System.err.println("❌ Registration failed: " + e.getMessage());
            return false;
        }
    }

    // Authenticate user (Login)
    public User loginUser(String email, String plainPassword) {
        String query = "SELECT user_id, username, email, password, role FROM users WHERE email = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                String storedHash = rs.getString("password");
                // If BCrypt verification passes, return the user
                if (BCrypt.checkpw(plainPassword, storedHash)) {
                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Login failed: " + e.getMessage());
        }
        return null;
    }
    
    

    // Get user by email
    public User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT user_id, username, email, password, role FROM users WHERE email = ?"; // Query to search by email

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setRole(resultSet.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
