package com.chatbot.connector;

import com.chatbot.utils.Log;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ModelManager {
    /**
     * Creates a database table for the given model class if it does not already exist.
     *
     * @param modelClass The class representing the database table.
     */
    public static void createTableIfNotExists(Class<?> modelClass) {
        String tableName = modelClass.getSimpleName().toLowerCase();
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        sql.append("id INT AUTO_INCREMENT PRIMARY KEY, ");

        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
            String columnName = field.getName();
            String columnType = getSQLType(field.getType());
            sql.append(columnName).append(" ").append(columnType).append(", ");
        }

        // Remove the last comma and space, then add closing parenthesis
        sql.setLength(sql.length() - 2);
        sql.append(");");

        // Execute the SQL statement
        try (Connection conn = DatabaseConnector.getConnection()) {
            assert conn != null;
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(sql.toString());
                Log.info("✅ Table `{}` is ready.", tableName);
            }
        } catch (SQLException e) {
            Log.error("❌ Error creating table {}: {}", tableName, e.getMessage());
        }
    }

    /**
     * Maps Java data types to SQL data types.
     *
     * @param fieldType Java field type.
     * @return Equivalent SQL data type.
     */
    private static String getSQLType(Class<?> fieldType) {
        if (fieldType == String.class) return "VARCHAR(255) NOT NULL";
        if (fieldType == int.class || fieldType == Integer.class) return "INT";
        if (fieldType == long.class || fieldType == Long.class) return "BIGINT";
        if (fieldType == double.class || fieldType == Double.class) return "DOUBLE";
        if (fieldType == boolean.class || fieldType == Boolean.class) return "BOOLEAN";
        return "VARCHAR(255)"; // Default for unknown types
    }
}
