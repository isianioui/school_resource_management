package com.example.database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseTest {

    @Test
    public void testConnection() {
        Connection connection = databaseConnection.getConnection();
        assertNotNull(connection, "❌ Connection should not be null");
    }
}
