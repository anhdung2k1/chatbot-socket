package com.chatbot.services;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;

import com.chatbot.utils.Constants;
import com.chatbot.utils.HTTPConnect;
import com.chatbot.utils.Log;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
