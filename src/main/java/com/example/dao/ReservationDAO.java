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

    public List<Reservation> getUserReservations(int userId, String userRole) {
        List<Reservation> reservations = new ArrayList<>();
        String query;
        
        if (userRole.equals("admin")) {
            query = "SELECT reservation_id, user_id, resource_type, resource_id, reservation_date, reservation_time, status FROM Reservations";
        } else {
            query = "SELECT reservation_id, user_id, resource_type, resource_id, reservation_date, reservation_time, status FROM Reservations WHERE user_id = ?";
        }
        
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            if (!userRole.equals("admin")) {
                pstmt.setInt(1, userId);
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservation_id(rs.getInt("reservation_id"));
                reservation.setUser_id(rs.getInt("user_id"));
                reservation.setResource_type(rs.getString("resource_type"));
                reservation.setResource_id(rs.getInt("resource_id"));
                reservation.setReservation_date(rs.getDate("reservation_date"));
                reservation.setReservation_time(rs.getTime("reservation_time"));
                reservation.setStatus(rs.getString("status"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reservations: " + e.getMessage());
        }
        return reservations;
    }
    
    

}
