package com.example.dao;

import com.example.database.databaseConnection;
import com.example.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;
    private databaseConnection databaseConnection;

    PreparedStatement statement = null;
    ResultSet resultSet = null;


    public UserDAO() {
        this.databaseConnection = new databaseConnection();
        this.connection = databaseConnection.getConnection();
    }

    public boolean registerUser(User user) {
        String query = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());

            int result = pstmt.executeUpdate();
            return result > 0; // Registration successful
        } catch (SQLException e) {
            System.err.println("❌ Registration failed: " + e.getMessage());
            return false;
        }
    }

    public User loginUser(String email, String plainPassword) {
        String query = "SELECT user_id, username, email, password, role FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = databaseConnection.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
    
            if (rs.next()) {
                String storedHash = rs.getString("password");
                
                if (BCrypt.checkpw(plainPassword, storedHash)) {
                    User user = new User();
                    user.setId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setRole(rs.getString("role"));
                    
                    // Update last login timestamp
                    String updateQuery = "UPDATE Users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, user.getId());
                        updateStmt.executeUpdate();
                    }
                    
                    System.out.println("✅ Login successful!");
                    return user;
                }
            }
            System.out.println("❌ Invalid credentials");
            
        } catch (SQLException e) {
            System.err.println("❌ Database error during login: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return null;
    }
    
    
    public User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT user_id, username, email, password, role FROM users WHERE email = ?";
        
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

    public int getUserCount() {
        String query = "SELECT COUNT(*) FROM Users";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getAdminCount() {
        String query = "SELECT COUNT(*) FROM Users WHERE role = 'admin'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public int getUserCountRegular() {
        String query = "SELECT COUNT(*) FROM Users WHERE role = 'user'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT user_id, username, email, role, account_status,last_login FROM Users";
        
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setAccountStatus(rs.getBoolean("account_status"));
                user.setLastLogin(rs.getTimestamp("last_login"));

                users.add(user);
                
                // Debug output
                // System.out.println("Loaded user: " + user.getUsername() + 
                //                  ", Status: " + user.getAccountStatus());
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }
        return users;
    }

    public void updateLastLogin(int userId) {
        String query = "UPDATE Users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating last login: " + e.getMessage());
        }
    }
    
    

public boolean updateAccountStatus(int userId, boolean status) {
    String query = "UPDATE Users SET account_status = ? WHERE user_id = ?";
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setBoolean(1, status);
        pstmt.setInt(2, userId);
        int rowsAffected = pstmt.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        System.err.println("Error updating user status: " + e.getMessage());
        return false;
    }
}

public void updateUserRole(int userId, String newRole) {
    String query = "UPDATE Users SET role = ? WHERE user_id = ?";
    try (Connection conn = databaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setString(1, newRole);
        pstmt.setInt(2, userId);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.err.println("Error updating user role: " + e.getMessage());
    }
}


public User getUserById(int userId) {
    try {
        if (connection == null || connection.isClosed()) {
            connection = databaseConnection.getConnection();
        }
        
        String sql = "SELECT * FROM users WHERE user_id = ?";
        System.out.println("Executing SQL query: " + sql + " with ID: " + userId);
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                return user;
            }
            System.out.println("No user found with ID: " + userId);
        }
    } catch (SQLException e) {
        System.out.println("SQL Error: " + e.getMessage());
        e.printStackTrace();
    }
    return null;
}


}
