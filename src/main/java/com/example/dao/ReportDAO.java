package com.example.dao;

import com.example.database.databaseConnection;
import com.example.models.Report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    private Connection connection;

    // public ReportDAO() {

    // }

    // Get recent reports generated
    public List<String> getRecentReports() {
        List<String> reports = new ArrayList<>();
        String query = "SELECT report_type FROM Reports ORDER BY generated_at DESC LIMIT 5";
        try (
                Connection connection = databaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                reports.add(resultSet.getString("report_type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public ObservableList<Report> getAllReports() {
        ObservableList<Report> reports = FXCollections.observableArrayList();
        String query = "SELECT report_id, report_type , data , generated_at FROM Reports ORDER BY generated_at DESC"; // No LIMIT clause
        try (Connection connection = databaseConnection.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("report_id");
                String type = resultSet.getString("report_type");
                String data = resultSet.getString("data");
                Timestamp generatedAt = resultSet.getTimestamp("generated_at");
                reports.add(new Report(id, type, data, generatedAt));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public boolean saveReport(String reportType,String reportData, Date reportDate) {
        String query = "INSERT INTO reports (report_type,data, generated_at) VALUES (?,?, ?)";

        try (Connection connection = databaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, reportType); // Set the report type
            preparedStatement.setString(2, reportData);
            preparedStatement.setDate(3, reportDate); // Set the report generation date

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0; // Return true if the report was successfully inserted
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Return false if there was an error
    }
}
