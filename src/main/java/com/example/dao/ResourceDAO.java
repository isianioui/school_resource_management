package com.example.dao;

import com.example.database.databaseConnection;
import com.example.models.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

    public class ResourceDAO {
        public List<Resource> getAllResources() {
            List<Resource> resources = new ArrayList<>();
            String query = "SELECT resource_id, resource_type, name, description, capacity, availability FROM Resources";
            
            try (Connection conn = databaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                
                while (rs.next()) {
                    Resource resource = new Resource();
                    resource.setResource_id(rs.getInt("resource_id"));
                    resource.setResource_type(rs.getString("resource_type"));
                    resource.setName(rs.getString("name"));
                    resource.setDescription(rs.getString("description"));
                    resource.setCapacity(rs.getInt("capacity"));
                    resource.setAvailability(rs.getBoolean("availability"));
                    resources.add(resource);
                }
            } catch (SQLException e) {
                System.err.println("Error fetching resources: " + e.getMessage());
            }
            return resources;
        }
    }
    
