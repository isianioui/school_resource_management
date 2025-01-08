package com.example.dao;

import com.example.database.databaseConnection;
import com.example.models.Report;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    private Connection connection;

    public ReportDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean createReport(Report report) {
        String sql = "INSERT INTO reports (event_id, description, images, created_by) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, report.getEventId());
            stmt.setString(2, report.getDescription());
            stmt.setString(3, String.join(",", report.getImagePaths())); // Convert list to comma-separated string
            stmt.setInt(4, report.getCreatedBy());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
