package com.chatbot.connector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chatbot.utils.Log;

/**
 * Generic Database Manager to perform CRUD operations on any table.
 */
public class GenericDatabaseManager {
    /**
     * Inserts a new record into any table.
     *
     * @param tableName The table name.
     * @param columnValues A map where keys are column names and values are the respective values.
     * @return true if the insert was successful, false otherwise.
     */
    public static boolean insert(String tableName, Map<String, Object> columnValues) {
        if (columnValues.isEmpty()) {
            Log.error("Cannot insert into table {} with empty column values.", tableName);
            return false;
        }

        String columns = String.join(", ", columnValues.keySet());
        String placeholders = String.join(", ", Collections.nCopies(columnValues.size(), "?"));
        String query = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

        try (Connection conn = DatabaseConnector.getConnection()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                int index = 1;
                for (Object value : columnValues.values()) {
                    stmt.setObject(index++, value);
                }

                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            Log.error("Error inserting into table {}: {}", tableName, e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a record from any table by a specific column value.
     *
     * @param tableName The table name.
     * @param whereColumn The column used for filtering.
     * @param whereValue The value to match.
     * @return A map representing the record, or null if not found.
     * To retrieve all information from table
     * Eg: SELECT * FROM questions WHERE question = "How can I prevent catching a cold?"
     */
    public static Map<String, Object> get(String tableName, String whereColumn, Object whereValue) {
        String query = "SELECT * FROM " + tableName + " WHERE " + whereColumn + " = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setObject(1, whereValue);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return extractRow(rs);
                    }
                }
            }
        } catch (SQLException e) {
            Log.error("Error retrieving from table {}: {}", tableName, e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves a record from any table by a specific column value.
     *
     * @param tableName The table name.
     * @param attributes The attributes name
     * @param whereColumn The column used for filtering.
     * @param whereValue The value to match.
     * @return A map representing the record, or null if not found.
     * To retrieve attributes information from table
     * Eg: SELECT question FROM questions WHERE question = "How can I prevent catching a cold?"
     */
    public static Map<String, Object> get(String tableName, String attributes, String whereColumn, Object whereValue) {
        String query = "SELECT" + attributes + "FROM " + tableName + " WHERE " + whereColumn + " = ? LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setObject(1, whereValue);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return extractRow(rs);
                    }
                }
            }
        } catch (SQLException e) {
            Log.error("Error retrieving from table {}: {}", tableName, e.getMessage());
        }
        return null;
    }


    /**
     * Updates a record in any table.
     *
     * @param tableName The table name.
     * @param columnValues The columns and values to update.
     * @param whereColumn The column used for filtering.
     * @param whereValue The value to match.
     * @return true if the update was successful, false otherwise.
     */
    public static boolean update(String tableName, Map<String, Object> columnValues, String whereColumn, Object whereValue) {
        if (columnValues.isEmpty()) {
            Log.error("Cannot update table {} with empty column values.", tableName);
            return false;
        }

        String setClause = String.join(", ", columnValues.keySet().stream().map(col -> col + " = ?").toList());
        String query = "UPDATE " + tableName + " SET " + setClause + " WHERE " + whereColumn + " = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int index = 1;
            for (Object value : columnValues.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setObject(index, whereValue);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.error("Error updating table {}: {}", tableName, e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a record from any table.
     *
     * @param tableName The table name.
     * @param whereColumn The column used for filtering.
     * @param whereValue The value to match.
     * @return true if the deletion was successful, false otherwise.
     */
    public static boolean delete(String tableName, String whereColumn, Object whereValue) {
        String query = "DELETE FROM " + tableName + " WHERE " + whereColumn + " = ?";

        try (Connection conn = DatabaseConnector.getConnection()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setObject(1, whereValue);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            Log.error("Error deleting from table {}: {}", tableName, e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all records from a table.
     *
     * @param tableName The table name.
     * @return A list of maps, each representing a record.
     */
    public static List<Map<String, Object>> getAll(String tableName) {
        List<Map<String, Object>> results = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;

        try (Connection conn = DatabaseConnector.getConnection()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    results.add(extractRow(rs));
                }
            }
        } catch (SQLException e) {
            Log.error("Error retrieving all records from table {}: {}", tableName, e.getMessage());
        }
        return results;
    }

    /**
     * Extracts a row from a ResultSet into a Map.
     *
     * @param rs The ResultSet object.
     * @return A map containing column names as keys and values as values.
     * @throws SQLException If an error occurs while extracting.
     */
    private static Map<String, Object> extractRow(ResultSet rs) throws SQLException {
        Map<String, Object> row = new HashMap<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            row.put(metaData.getColumnName(i), rs.getObject(i));
        }
        return row;
    }
}