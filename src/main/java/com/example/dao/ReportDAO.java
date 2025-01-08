package com.example.dao;

import com.example.database.databaseConnection;
import com.example.models.Report;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    private Connection connection;

    public ReportDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean uploadReport(Report report, File file) {
        String sql = "INSERT INTO Reports (title, description, file_path, file_type, uploaded_by, related_event_id, is_public) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Create reports directory if it doesn't exist
            String storageDir = "reports";
            Files.createDirectories(Path.of(storageDir));
            
            String fileName = System.currentTimeMillis() + "_" + file.getName();
            Path targetPath = Path.of(storageDir, fileName);
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
    
            stmt.setString(1, report.getTitle());
            stmt.setString(2, report.getDescription());
            stmt.setString(3, fileName);
            stmt.setString(4, report.getFileType());
            stmt.setInt(5, report.getUploadedBy());
            if (report.getRelatedEventId() != null) {
                stmt.setInt(6, report.getRelatedEventId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setBoolean(7, report.isPublic());
    
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    

    public List<Report> getAllReports() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM Reports ORDER BY upload_date DESC";
        
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Report report = new Report();
                report.setReportId(rs.getInt("report_id"));
                report.setTitle(rs.getString("title"));
                report.setDescription(rs.getString("description"));
                report.setFilePath(rs.getString("file_path"));
                report.setFileType(rs.getString("file_type"));
                report.setUploadedBy(rs.getInt("uploaded_by"));
                report.setUploadDate(rs.getTimestamp("upload_date"));
                report.setPublic(rs.getBoolean("is_public"));
                reports.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }
   

    public boolean deleteReport(int reportId) {
        String sql = "DELETE FROM Reports WHERE report_id = ?";
        
        try (Connection conn = databaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            // First get the file path to delete the physical file
            String filePath = getReportFilePath(reportId);
            if (filePath != null) {
                Files.deleteIfExists(Path.of("reports/" + filePath));
            }
            
            // Then delete the database record
            stmt.setInt(1, reportId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private String getReportFilePath(int reportId) {
        String sql = "SELECT file_path FROM Reports WHERE report_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("file_path");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
