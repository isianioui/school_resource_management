package com.example.models;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private int event_id;
    private String name;
    private String description;
    private Date event_date;
    private Time event_time;
    private String location;
    private int created_by;
    private String creator_name;
public String getCreator_name() { return creator_name; }
public void setCreator_name(String creator_name) { this.creator_name = creator_name; }


    // Getters and Setters
    public int getEvent_id() { return event_id; }
    public void setEvent_id(int event_id) { this.event_id = event_id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Date getEvent_date() { return event_date; }
    public void setEvent_date(Date event_date) { this.event_date = event_date; }
    
    public Time getEvent_time() { return event_time; }
    public void setEvent_time(Time event_time) { this.event_time = event_time; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public int getCreated_by() { return created_by; }
    public void setCreated_by(int created_by) { this.created_by = created_by; }
}

