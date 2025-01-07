package com.example.models;

import java.sql.Timestamp;

public class Report {
    private int report_id;
    private String report_type;  // Corrected spelling
    private String data;
    private Timestamp generated_at;

    public Report(int id, String report_type, String data, Timestamp generated_at) {
        this.report_id = id;
        this.report_type = report_type;
        this.data = data;
        this.generated_at = generated_at;
    }

    // Use proper JavaBean naming convention
    public int getReportId() {
        return this.report_id;
    }

    public void setReportId(int id) {
        this.report_id = id;
    }

    public String getReportType() {
        return this.report_type;
    }

    public void setReportType(String report_type) {
        this.report_type = report_type;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Timestamp getGeneratedAt() {
        return this.generated_at;
    }

    public void setGeneratedAt(Timestamp generated_at) {
        this.generated_at = generated_at;
    }
}
