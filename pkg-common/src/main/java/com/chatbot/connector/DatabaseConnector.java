package com.chatbot.connector;

import com.chatbot.model.Questions;
import com.chatbot.utils.Constants;
import com.chatbot.utils.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database Connector class for managing database connections.
 */
public class DatabaseConnector {
    private static final int MAX_RETRIES = 10;

    static {
        initializeDatabase();
    }

    /**
     * Establishes a new database connection.
     *
     * @return Connection object if successful, null otherwise.
     */
    public static Connection getConnection() {
        int attempts = 0;
        while (attempts < MAX_RETRIES) {
            try {
                Connection conn = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USERNAME, Constants.DB_PASSWORD);
                Log.info("Database connection established successfully.");
                return conn;
            } catch (SQLException e) {
                attempts++;
                Log.warn("Database connection attempt {} failed: {}", attempts, e.getMessage());
                if (attempts == MAX_RETRIES) {
                    Log.error("Maximum database connection attempts reached. Unable to connect.");
                }
                try {
                    Thread.sleep(2000); // Wait before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return null;
    }

    /**
     * Closes the database connection
     *
     * @param connection The connection object to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                Log.warn("Failed to close the database connection {}.", e.getMessage());
            }
        }
    }

    private static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USERNAME, Constants.DB_PASSWORD); Statement stmt = conn.createStatement()) {
            Log.info("Initializing database...");
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + Constants.DB_NAME);
            Log.info("Database `{}` is ready.", Constants.DB_NAME);
        } catch (SQLException e) {
            Log.error("Failed to initialize database {}.", e.getMessage());
        }
        ModelManager.createTableIfNotExists(Questions.class);
    }
}
