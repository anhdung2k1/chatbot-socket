package com.chatbot.ui;

import com.chatbot.services.ChatBotClientService;
import com.chatbot.utils.Constants;
import com.chatbot.utils.Log;
import com.chatbot.utils.PatternChatBot;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class SignUpPage {
    private final JFrame frame;
    private final JTextField usernameField;
    private final JPasswordField passwordField, confirmPasswordField;
    private final ChatBotClientService client;
    private final JLabel statusLabel;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SignUpPage() {
        client = ChatBotClientService.getInstance();

        frame = new JFrame("Sign Up");
        frame.setSize(450, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        statusLabel = new JLabel("", SwingConstants.CENTER);
        frame.add(statusLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(5, 2, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(usernameLabel);
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(34, 193, 195), 2));
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(34, 193, 195), 2));
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(confirmPasswordLabel);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 16));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(new Color(34, 193, 195), 2));
        panel.add(confirmPasswordField);

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.setBackground(new Color(34, 193, 195));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        signUpButton.setBorder(BorderFactory.createLineBorder(new Color(34, 193, 195), 2));
        signUpButton.setContentAreaFilled(true);
        panel.add(signUpButton);

        JButton signInRedirectButton = new JButton("Sign In??");
        signInRedirectButton.setFont(new Font("Arial", Font.PLAIN, 14));
        signInRedirectButton.setBackground(new Color(255, 153, 51));
        signInRedirectButton.setForeground(Color.WHITE);
        signInRedirectButton.setFocusPainted(false);
        signInRedirectButton.setBorder(BorderFactory.createLineBorder(new Color(255, 153, 51), 2));
        signInRedirectButton.setContentAreaFilled(true);
        panel.add(signInRedirectButton);

        frame.add(panel, BorderLayout.CENTER);

        signUpButton.addActionListener(this::handleSignUp);
        signInRedirectButton.addActionListener(e -> {
            frame.dispose();
            new SignInPage();
        });

        frame.setVisible(true);
    }

    private void handleSignUp(ActionEvent event) {
        String userName = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(frame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JsonNode payload = objectMapper.createObjectNode()
                    .put("userName", userName)
                    .put("password", password);
        final String jsonPayload;
        try {
            jsonPayload = objectMapper.writeValueAsString(payload);
        } catch (IOException e) {
            statusLabel.setText("Error preparing signup data.");
            statusLabel.setForeground(Color.RED);
            return;
        }

        new Thread(() -> {
            Log.info("Sign up sendMessage with Pattern {}, jsonPayload: {}", PatternChatBot.SIGN_UP, jsonPayload);
            client.sendMessage(PatternChatBot.SIGN_UP, jsonPayload);
            try {
                String response = client.receiveMessage(PatternChatBot.SIGN_UP);
                Log.info("SignUp Response: {}", response);
                if (response.equalsIgnoreCase(Constants.SUCCESS)) {
                    JOptionPane.showMessageDialog(frame, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    SwingUtilities.invokeLater(() -> {
                        frame.dispose();
                        new MedicalChatbotUI();
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("Register failed: " + response);
                        statusLabel.setForeground(Color.RED);
                    });
                }
            } catch (IOException ex) {
                SwingUtilities.invokeLater(() -> statusLabel.setText("Error: " + ex.getMessage()));
            }
        }).start();
    }
}