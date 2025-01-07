package com.example.dao;

import com.example.database.databaseConnection;
import com.example.models.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

    public class ResourceDAO {
        private Connection connection;
        
        
        
                public ResourceDAO(Connection connection) {
                    this.connection = connection;
        }
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

        public Resource getResourceById(int resourceId) {
            String query = "SELECT * FROM resources WHERE resource_id = ?";
            try (Connection conn = databaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                
                stmt.setInt(1, resourceId);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    Resource resource = new Resource();
                    resource.setResource_id(rs.getInt("resource_id"));
                    resource.setName(rs.getString("name"));
                    resource.setResource_type(rs.getString("resource_type"));
                    resource.setCapacity(rs.getInt("capacity"));
                    resource.setAvailability(rs.getBoolean("availability"));
                    return resource;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public boolean createResource(Resource resource) {
            String sql = "INSERT INTO Resources (name, resource_type, capacity, availability, description) VALUES (?, ?, ?, ?, ?)";
            
            try (Connection conn = databaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, resource.getName());
                pstmt.setString(2, resource.getResource_type());
                pstmt.setInt(3, resource.getCapacity());
                pstmt.setBoolean(4, resource.getAvailability());
                pstmt.setString(5, resource.getDescription());
                
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
                
            } catch (SQLException e) {
                System.err.println("Error creating resource: " + e.getMessage());
                return false;
            }
        }
        


        public void save(Resource resource) {
            String sql = "INSERT INTO resources (resource_type, name, description, capacity, availability) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, resource.getResource_type());
                stmt.setString(2, resource.getName());
                stmt.setString(3, resource.getDescription());
                stmt.setInt(4, resource.getCapacity());
                stmt.setBoolean(5, resource.getAvailability());
                stmt.executeUpdate();
                
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    resource.setResource_id(rs.getInt(1));
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error saving resource", e);
            }
        }
    
        
    }
    
