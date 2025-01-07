package com.example.models;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private int reservation_id;
    private int user_id;
    private String resource_type;
    private int resource_id;
    private Date reservation_date;
    private Time reservation_time;
    private String status;
    private boolean reminder_sent;


    // Getters and Setters
    public int getReservation_id() { return reservation_id; }
    public void setReservation_id(int reservation_id) { this.reservation_id = reservation_id; }
    public void setReminder_sent(boolean reminder_sent) { 
        this.reminder_sent = reminder_sent; 
    }
    
    public int getUser_id() { return user_id; }
    public void setUser_id(int user_id) { this.user_id = user_id; }
    
    public String getResource_type() { return resource_type; }
    public void setResource_type(String resource_type) { this.resource_type = resource_type; }
    
    public int getResource_id() { return resource_id; }
    public void setResource_id(int resource_id) { this.resource_id = resource_id; }
    
    public Date getReservation_date() { return reservation_date; }
    public void setReservation_date(Date reservation_date) { this.reservation_date = reservation_date; }
    
    public Time getReservation_time() { return reservation_time; }
    public void setReservation_time(Time reservation_time) { this.reservation_time = reservation_time; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean getReminder_sent() { return reminder_sent; }
}
