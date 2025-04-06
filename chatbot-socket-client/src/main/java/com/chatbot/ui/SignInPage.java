package com.chatbot.ui;

import com.chatbot.services.ChatBotClientService;
import com.chatbot.utils.Log;
import com.chatbot.utils.PatternChatBot;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import com.chatbot.utils.Constants;
import com.fasterxml.jackson.databind.JsonNode;

public class SignInPage {
    private final JFrame frame;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel statusLabel;
    private final ChatBotClientService client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SignInPage() {
        client = ChatBotClientService.getInstance();

        frame = new JFrame("Sign In");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        loginPanel.add(new JLabel("Username: "));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password: "));
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Sign In");
        loginButton.addActionListener(this::handleSignIn);
        loginPanel.add(loginButton);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(e -> {
            frame.dispose();
            new SignUpPage();
        });
        loginPanel.add(signUpButton);

        statusLabel = new JLabel("", SwingConstants.CENTER);

        frame.add(statusLabel, BorderLayout.NORTH);
        frame.add(loginPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void handleSignIn(ActionEvent event) {
        String userName = usernameField.getText();
        String password = new String(passwordField.getPassword());
        JsonNode payload = objectMapper.createObjectNode()
                    .put("userName", userName)
                    .put("password", password);
        String jsonPayload;
        try {
            jsonPayload = objectMapper.writeValueAsString(payload);
        } catch (IOException e) {
            statusLabel.setText("Error preparing login data.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        new Thread(() -> {
            Log.info("Sign in sendMessage with Pattern {}, jsonPayload: {}", PatternChatBot.SIGN_IN, jsonPayload);
            client.sendMessage(PatternChatBot.SIGN_IN, jsonPayload);
            try {
                String response = client.receiveMessage(PatternChatBot.SIGN_IN);
                Log.info("SignIn Response: {}", response);
                if ((response.equalsIgnoreCase(Constants.SUCCESS))) {
                    SwingUtilities.invokeLater(() -> {
                        frame.dispose();
                        new MedicalChatbotUI();
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Login failed: " + response);
                        statusLabel.setForeground(Color.RED);
                    });
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> statusLabel.setText("Error: " + e.getMessage()));
            }
        }).start();
    }
}