package com.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5433/school_resource_management";
    private static final String USER = "postgres";
    private static final String PASSWORD = "123";

    private static Connection connection = null;

    // Method to establish connection
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            } catch (SQLException e) {
                System.err.println("❌ Database connection failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }

    // Method to close connection
    // public static void closeConnection() {
    //     if (connection != null) {
    //         try {
    //             connection.close();
    //             System.out.println("🔌 Database connection closed.");
    //         } catch (SQLException e) {
    //             System.err.println("❌ Failed to close connection: " + e.getMessage());
    //         }
    //     }
    // }
}
