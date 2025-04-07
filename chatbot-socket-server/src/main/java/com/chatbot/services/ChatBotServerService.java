package com.chatbot.services;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;

import com.chatbot.utils.Constants;
import com.chatbot.utils.HTTPConnect;
import com.chatbot.utils.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;

public class ChatBotServerService {
    private static final HTTPConnect httpConnect = new HTTPConnect();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // This SECRET_KEY match with Spring Boot API because it used symmetric keys
    // So the secret key must be identical to decrypt the token key.
    private static final String SECRET_KEY = "655368566D597133743677397A244326452948404D635166546A576E5A723475"; // Base64 encoded

    public static String authRequest(String request, Boolean isSignIn) throws Exception {
        try {
            String apiUrl = Constants.API_URL;
            apiUrl += isSignIn ? "/signin" : "/signup";
            String token = httpConnect.authenticate(apiUrl, request);
            // Verify token
            boolean isValid = verifyToken(token);
            if (isValid) {
                Log.info("Token is valid!");
                return Constants.SUCCESS;
            } else {
                Log.warn("Token is invalid!");
                return Constants.FAILED;
            }
        } catch (IOException e) {
            Log.error("Error in authRequest: {}", e.getMessage());
            return Constants.FAILED;
        }
    }

    public static String signOut() throws Exception {
        httpConnect.clearToken();
        if (httpConnect.getToken() != null) {
            return Constants.FAILED;
        }
        return Constants.SUCCESS;
    }

    public static boolean verifyToken(String token) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
            Key key = Keys.hmacShaKeyFor(keyBytes);

            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Log.info("Token subject: {}", claims.getBody().getSubject());
            Log.info("Token expiration: {}", claims.getBody().getExpiration());

            return true;
        } catch (JwtException e) {
            Log.error("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

    // This function will list all subjects and questions in each subject
    public static String getAllSubjects(String request) throws Exception {
        try {
            String apiUrl = Constants.API_URL + "/subject";
            Log.info("getAllSubjects request: {}", request);
            String response = httpConnect.sendGet(apiUrl);
            Log.info("getAllSubjects response: {}", response);
            return response;
        } catch (Exception e) {
            Log.error("Error in authRequest: {}", e.getMessage());
            return "";
        }
    }

    public static String getQuestionAndAnswerById(String request) throws Exception {
        try {
            JsonNode node = objectMapper.readTree(request);
            long questionId = node.get("questionsId").asLong();
            String apiUrl = Constants.API_URL + "/question/" + questionId;

            Log.info("Fetching answer for question ID: {}", questionId);
            String response = httpConnect.sendGet(apiUrl);
            Log.info("Response for question ID {}: {}", questionId, response);
            return response;
        } catch (Exception e) {
            Log.error("Error retrieving question and answer: {}", e.getMessage());
            return Constants.MSG_NOT_FOUND;
        }
    }

    public static String createNewSubject(String request) throws Exception {
        try {
            String apiUrl = Constants.API_URL + "/subject";
            String response = httpConnect.sendPost(apiUrl, request);
            Log.info("Response for createNewSubject: {}", response);
            if (response.isEmpty()) {
                Log.error("Failed to create new Subject {}", request);
                return Constants.FAILED;
            }
            return Constants.SUCCESS;
        } catch (Exception e) {
            Log.error("Error to create new Subject: {}", e.getMessage());
            return Constants.FAILED;
        }
    }

    public static String updateSubject(String request) throws Exception {
        try {
            JsonNode node = objectMapper.readTree(request);
            long subjectId = node.get("subjectId").asLong();
            String subjectName = node.get("subjectName").asText();
            String subjectDescription = node.get("subjectDescription").asText();
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("subjectName", subjectName);
            payload.put("subjectDescription", subjectDescription);

            String jsonPayload = objectMapper.writeValueAsString(payload);

            String apiUrl = Constants.API_URL + "/subject/" + subjectId;
            String response = httpConnect.sendPut(apiUrl, jsonPayload);
            Log.info("Response for updateSubject: {}", response);
            if (response.isEmpty()) {
                Log.error("Failed to updateSubject {}", request);
                return Constants.FAILED;
            }
            return Constants.SUCCESS;
        } catch (Exception e) {
            Log.error("Error to update Subject: {}", e.getMessage());
            return Constants.FAILED;
        }
    }

    public static String deleteSubject(String request) throws Exception {
        try {
            JsonNode node = objectMapper.readTree(request);
            long subjectId = node.get("subjectId").asLong();
            String apiUrl = Constants.API_URL + "/subject/" + subjectId;

            String response = httpConnect.sendDelete(apiUrl);
            Log.info("Response for subject ID {}: {}", subjectId, response);
            if (response.isEmpty()) {
                Log.error("Failed to deleteSubject {}", request);
                return Constants.FAILED;
            }
            return Constants.SUCCESS;
        } catch (Exception e) {
            Log.error("Error to deleteSubject: {}", e.getMessage());
            return Constants.FAILED;
        }
    }

    public static String createNewQuestion(String request) throws Exception {
        try {
            String apiUrl = Constants.API_URL + "/question";
            String response = httpConnect.sendPost(apiUrl, request);
            Log.info("Response for createNewQuestion: {}", response);
            if (response.isEmpty()) {
                Log.error("Failed to create new Question {}", request);
                return Constants.FAILED;
            }
            return Constants.SUCCESS;
        } catch (Exception e) {
            Log.error("Error to create new Question: {}", e.getMessage());
            return Constants.FAILED;
        }
    }

    public static String updateQuestion(String request) throws Exception {
        try {
            JsonNode node = objectMapper.readTree(request);
            long questionId = node.get("questionsId").asLong();
            String question = node.get("question").asText();
            String answer = node.get("answer").asText();
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("question", question);
            payload.put("answer", answer);

            String jsonPayload = objectMapper.writeValueAsString(payload);

            String apiUrl = Constants.API_URL + "/question/" + questionId;
            String response = httpConnect.sendPut(apiUrl, jsonPayload);
            Log.info("Response for updateQuestion: {}", response);
            if (response.isEmpty()) {
                Log.error("Failed to updateQuestion: {}", request);
                return Constants.FAILED;
            }
            return Constants.SUCCESS;
        } catch (Exception e) {
            Log.error("Error to updateQuestion: {}", e.getMessage());
            return Constants.FAILED;
        }
    }

    public static String deleteQuestion(String request) throws Exception {
        try {
            JsonNode node = objectMapper.readTree(request);
            long questionId = node.get("questionsId").asLong();
            String apiUrl = Constants.API_URL + "/question/" + questionId;

            String response = httpConnect.sendDelete(apiUrl);
            Log.info("Response for question ID {}: {}", questionId, response);
            if (response.isEmpty()) {
                Log.error("Failed to deleteQuestion {}", request);
                return Constants.FAILED;
            }
            return Constants.SUCCESS;
        } catch (Exception e) {
            Log.error("Error to deleteQuestion: {}", e.getMessage());
            return Constants.FAILED;
        }
    }
}
