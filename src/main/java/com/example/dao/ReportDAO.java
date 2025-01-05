package com.example.dao;

import com.example.database.databaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    private Connection connection;

    public ReportDAO() {
        connection = databaseConnection.getConnection();
    }

    // Get recent reports generated
    public List<String> getRecentReports() {
        List<String> reports = new ArrayList<>();
        String query = "SELECT report_type FROM Reports ORDER BY generated_at DESC LIMIT 5";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                reports.add(resultSet.getString("report_type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }
}
