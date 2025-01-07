package com.example.dao;

import com.example.database.databaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.example.models.Event;


public class EventDAO {

    private Connection connection;

    public EventDAO() {
        connection = databaseConnection.getConnection();
    }

    // Get count of upcoming events
    public int getUpcomingEventCount() {
        String query = "SELECT COUNT(*) FROM Events WHERE event_date >= CURRENT_DATE";
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

    // Get the latest upcoming event details
    public String getUpcomingEventsDetails() {
        String query = "SELECT name, event_date, event_time FROM Events WHERE event_date >= CURRENT_DATE LIMIT 5";
        StringBuilder events = new StringBuilder();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                events.append(resultSet.getString("name"))
                      .append(" - ")
                      .append(resultSet.getDate("event_date"))
                      .append(" at ")
                      .append(resultSet.getTime("event_time"))
                      .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events.toString();
    }


    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT event_id, name, description, event_date, event_time, location, created_by FROM Events";
        
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Event event = new Event();
                event.setEvent_id(rs.getInt("event_id"));
                event.setName(rs.getString("name"));
                event.setDescription(rs.getString("description"));
                event.setEvent_date(rs.getDate("event_date"));
                event.setEvent_time(rs.getTime("event_time"));
                event.setLocation(rs.getString("location"));
                event.setCreated_by(rs.getInt("created_by"));
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching events: " + e.getMessage());
        }
        return events;
    }
}
