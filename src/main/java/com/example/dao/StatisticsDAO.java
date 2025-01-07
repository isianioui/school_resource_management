package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.database.databaseConnection;

public class StatisticsDAO {
    private Connection conn;
    
   

    // Move all other statistical methods here
    public int getResourceCount(String type) {
        String query = "SELECT COUNT(*) FROM Resources WHERE resource_type = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting resources: " + e.getMessage());
        }
        return 0;
    }
    public int getActiveUsers() {
        String query = "SELECT COUNT(*) FROM Users WHERE account_status = true";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting active users: " + e.getMessage());
        }
        return 0;
    }
    
    public int getInactiveUsers() {
        String query = "SELECT COUNT(*) FROM Users WHERE account_status = false";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting inactive users: " + e.getMessage());
        }
        return 0;
    }
    
    
    public int getReservationCount(String status) {
        String query = "SELECT COUNT(*) FROM Reservations WHERE status = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting reservations: " + e.getMessage());
        }
        return 0;
    }
    
    public int getTotalUsers() {
        String query = "SELECT COUNT(*) FROM Users";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting users: " + e.getMessage());
        }
        return 0;
    }
    
    public int getActiveReservations() {
        String query = "SELECT COUNT(*) FROM Reservations WHERE status = 'confirmed'";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting active reservations: " + e.getMessage());
        }
        return 0;
    }
    
    public int getAvailableResources() {
        String query = "SELECT COUNT(*) FROM Resources WHERE availability = true";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting available resources: " + e.getMessage());
        }
        return 0;
    }
    
}

