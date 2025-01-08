package com.example.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class Report {
    private int reportId;
    private String title;
    private String description;
    private String filePath;
    private String fileType;
    private int uploadedBy;
    private Integer relatedEventId;
    private Timestamp uploadDate;
    private boolean isPublic;
    
    // Getters, setters, and constructor
    public Report() {
        this.uploadDate = Timestamp.valueOf(LocalDateTime.now());
    }
    
    public int getReportId() {
        return reportId;
    }
    
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public int getUploadedBy() {
        return uploadedBy;
    }
    
    public void setUploadedBy(int uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
    
    public Integer getRelatedEventId() {
        return relatedEventId;
    }
    
    public void setRelatedEventId(Integer relatedEventId) {
        this.relatedEventId = relatedEventId;
    }
    
    public Timestamp getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }
    
    public boolean isPublic() {
        return isPublic;
    }
    
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    
}

