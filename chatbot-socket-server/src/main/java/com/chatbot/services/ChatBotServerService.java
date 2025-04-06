package com.chatbot.services;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;

import com.chatbot.utils.Constants;
import com.chatbot.utils.HTTPConnect;
import com.chatbot.utils.Log;
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
    private static final String SECRET_KEY = "655368566D597133743677397A244326452948404D635166546A576E5A723475"; // HEX encoded

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
}
