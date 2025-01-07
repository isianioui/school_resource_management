package com.example.models;

public class Resource {
    private int resource_id;
    private String resource_type;
    private String name;
    private String description;
    private int capacity;
    private boolean availability;

    // Getters and Setters
    public int getResource_id() { return resource_id; }
    public void setResource_id(int resource_id) { this.resource_id = resource_id; }
    
    public String getResource_type() { return resource_type; }
    public void setResource_type(String resource_type) { this.resource_type = resource_type; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    public boolean getAvailability() { return availability; }
    public void setAvailability(boolean availability) { this.availability = availability; }
    @Override
public String toString() {
    return this.getName();
}

}

