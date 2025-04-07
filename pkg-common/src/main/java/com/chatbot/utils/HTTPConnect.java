package com.chatbot.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class HTTPConnect {
    private ObjectMapper objectMapper = new ObjectMapper();
    private String token;

    // Method to perform the POST request for authentication (sign-in/sign-up)
    public String authenticate(String url, String jsonPayload) throws IOException {
        String response = sendRequest(url, "POST", jsonPayload);
        JsonNode jsonNode = objectMapper.readTree(response);
        token = jsonNode.get("token").asText();
        return token;
    }

    public void clearToken() throws IOException {
        setToken(null);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Helper method to send HTTP requests (GET, POST, PUT, DELETE)
    private String sendRequest(String url, String method, String jsonPayload) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        DataOutputStream writer = null;

        try {
            // Create a URL object and open a connection
            URI uri = URI.create(url);
            URL obj = uri.toURL();
            connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");

            if (token != null && !token.isEmpty()) {
                connection.setRequestProperty("Authorization", "Bearer " + token);  // Add token to the header
            }

            // Write the request body (for POST, PUT, and PATCH)
            if (jsonPayload != null && !jsonPayload.isEmpty()) {
                connection.setDoOutput(true);
                writer = new DataOutputStream(connection.getOutputStream());
                writer.writeBytes(jsonPayload);
                writer.flush();
            }

            // Read the response
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } finally {
            closeResources(connection, writer, reader);
        }

        return response.toString();
    }

    // Helper method to close resources (connections, readers, writers)
    private void closeResources(HttpURLConnection connection, DataOutputStream writer, BufferedReader reader) throws IOException {
        if (connection != null) {
            connection.disconnect();
        }
        if (writer != null) {
            writer.close();
        }
        if (reader != null) {
            reader.close();
        }
    }

    // Method to send GET request
    public String sendGet(String url) throws IOException {
        return sendRequest(url, "GET", null);  // No body for GET requests
    }

    // Method to send POST request
    public String sendPost(String url, String jsonPayload) throws IOException {
        return sendRequest(url, "POST", jsonPayload);
    }

    // Method to send PUT request
    public String sendPut(String url, String jsonPayload) throws IOException {
        return sendRequest(url, "PUT", jsonPayload);
    }

    // Method to send DELETE request
    public String sendDelete(String url) throws IOException {
        return sendRequest(url, "DELETE", null);  // No body for DELETE requests
    }
}
