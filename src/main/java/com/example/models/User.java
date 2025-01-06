package com.example.models;


import java.sql.Timestamp;

import org.mindrot.jbcrypt.BCrypt;

public class User {
    private int id;
    private String username;
    private String email;
    private String password; // Hashed password
    private String role; // admin or user
    private Timestamp lastLogin;
    private String ipAddress;
    private Timestamp createdAt;
    private boolean accountStatus;
    // Constructors
    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = hashPassword(password); // Hash password on creation
        this.role = role;
    }

    public User() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) {
         this.password = hashPassword(password); 
        }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Hash password with BCrypt
    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Verify password
   public boolean verifyPassword(String plainPassword) {
        return BCrypt.checkpw(plainPassword, this.password);
 
   }


public boolean getAccountStatus() {
    return accountStatus;
}

public void setAccountStatus(boolean accountStatus) {
    this.accountStatus = accountStatus;
}

public Timestamp getLastLogin() {
    return lastLogin;
}

public void setLastLogin(java.sql.Timestamp lastLogin) {
    this.lastLogin = lastLogin;
}

public String getIpAddress() {
    return ipAddress;
}

public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
}

public Timestamp getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
}



}
