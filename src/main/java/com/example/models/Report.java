package com.example.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class Report {
    private int id;                // Unique ID of the report
    private int eventId;           // Associated Event ID
    private String description;    // Report description
    private List<String> imagePaths; // List of image paths
    private int createdBy;         // User ID of the creator
    private String createdAt;      // Creation timestamp

    // Constructor
    public Report(int eventId, String description, int createdBy) {
        this.eventId = eventId;
        this.description = description;
        this.createdBy = createdBy;
    }
    public List<String> getImages() {
        return getImagePaths(); 
    }
    
    // Overloaded Constructor (for retrieving data from DB)
    public Report(int id, int eventId, String description, List<String> imagePaths, int createdBy, String createdAt) {
        this.id = id;
        this.eventId = eventId;
        this.description = description;
        this.imagePaths = imagePaths;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

     // Add this constructor
     public Report(int eventId, String description, int createdBy, String images) {
        this.eventId = eventId;
        this.description = description;
        this.createdBy = createdBy;
        this.imagePaths = new ArrayList<>();
        
        // Split comma-separated images into a list
        if (images != null && !images.isEmpty()) {
            String[] paths = images.split(",");
            for (String path : paths) {
                this.imagePaths.add(path.trim());
            }
        }
    }


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", description='" + description + '\'' +
                ", imagePaths=" + imagePaths +
                ", createdBy=" + createdBy +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}


