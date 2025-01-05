package com.example.dao;

import com.example.database.databaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    private Connection connection;

    public NotificationDAO() {
        connection = databaseConnection.getConnection();
    }

    // Get recent notifications for a specific user
    public List<String> getRecentNotifications() {
        List<String> notifications = new ArrayList<>();
        String query = "SELECT message FROM Notifications WHERE is_read = FALSE ORDER BY created_at DESC LIMIT 5";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                notifications.add(resultSet.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }
}
