package com.example.dao;

import com.example.database.databaseConnection;
import com.example.models.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    private Connection connection;

    public ReservationDAO() {
        connection = databaseConnection.getConnection();
    }

    // Get total number of reservations
    public int getReservationCount() {
        String query = "SELECT COUNT(*) FROM Reservations";
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

    // Get the count of reservations with each status
    public int getPendingReservationCount() {
        String query = "SELECT COUNT(*) FROM Reservations WHERE status = 'pending'";
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

    public int getConfirmedReservationCount() {
        String query = "SELECT COUNT(*) FROM Reservations WHERE status = 'confirmed'";
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

    public int getRejectedReservationCount() {
        String query = "SELECT COUNT(*) FROM Reservations WHERE status = 'rejected'";
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

    public List<Reservation> getUserReservations(int userId, String role) {
        List<Reservation> reservations = new ArrayList<>();
        String query = role.equals("admin") ?
            "SELECT reservation_id, user_id, resource_id, resource_type, reservation_date, reservation_time, status, reminder_sent FROM Reservations" :
            "SELECT reservation_id, user_id, resource_id, resource_type, reservation_date, reservation_time, status, reminder_sent FROM Reservations WHERE user_id = ?";
    
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (!role.equals("admin")) {
                stmt.setInt(1, userId);
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation res = new Reservation();
                res.setReservation_id(rs.getInt("reservation_id"));
                res.setUser_id(rs.getInt("user_id"));
                res.setResource_id(rs.getInt("resource_id"));
                res.setResource_type(rs.getString("resource_type"));
                res.setReservation_date(rs.getDate("reservation_date"));
                res.setReservation_time(rs.getTime("reservation_time"));
                res.setStatus(rs.getString("status"));
                res.setReminder_sent(rs.getBoolean("reminder_sent"));
                reservations.add(res);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public boolean updateReservationStatus(int reservationId, String status) {
        String query = "UPDATE Reservations SET status = ? WHERE reservation_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public boolean deleteReservation(int reservation_id) {
        String query = "DELETE FROM reservations WHERE reservation_id = ?";
        return executeUpdate(query, reservation_id);
    }
    
    public boolean updateReminderStatus(int reservationId, boolean reminderSent) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = databaseConnection.getConnection();
            }
            
            String sql = "UPDATE reservations SET reminder_sent = ? WHERE reservation_id = ?";
            System.out.println("Executing update query: " + sql);
            System.out.println("Parameters - reminderSent: " + reminderSent + ", reservationId: " + reservationId);
            
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setBoolean(1, reminderSent);
                stmt.setInt(2, reservationId);
                int rowsAffected = stmt.executeUpdate();
                System.out.println("Rows affected by update: " + rowsAffected);
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error updating reminder status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    public boolean updateReservation(Reservation reservation) {
        String query = "UPDATE reservations SET resource_id = ?, reservation_date = ?, reservation_time = ?, status = ? WHERE reservation_id = ?";
        return executeUpdate(query, 
            reservation.getResource_id(),
            reservation.getReservation_date(),
            reservation.getReservation_time(),
            reservation.getStatus(),
            reservation.getReservation_id()
        );
    }
    
    
    public boolean createReservation(Reservation reservation) {
        String query = "INSERT INTO reservations (resource_id, user_id, reservation_date, reservation_time, status, resource_type) VALUES (?, ?, ?, ?, ?, ?)";
    
        return executeUpdate(query,
            reservation.getResource_id(),
            reservation.getUser_id(),
            reservation.getReservation_date(),
            reservation.getReservation_time(),
            "pending",
            reservation.getResource_type()
        );
    }
    
    
    private boolean executeUpdate(String query, Object... params) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    

}
