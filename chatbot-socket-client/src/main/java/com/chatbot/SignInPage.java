package com.chatbot;

import javax.swing.*;

import com.chatbot.utils.Log;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInPage {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;

    public SignInPage() {
        frame = new JFrame("Sign In");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10)); // Add spacing between elements

        // Panel containing the sign-in form
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2, 10, 10));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding to the entire panel

        // Username
        JLabel usernameLabel = new JLabel("Username: ");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        loginPanel.add(usernameLabel);
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(34, 193, 195), 2)); // Border with blue color
        loginPanel.add(usernameField);

        // Password
        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        loginPanel.add(passwordLabel);
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(34, 193, 195), 2)); // Border with blue color
        loginPanel.add(passwordField);

        // Sign In button
        JButton loginButton = new JButton("Sign In");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(34, 193, 195));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setOpaque(true);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(34, 193, 195), 2));
        loginButton.addActionListener(new SignInAction());

        // Sign Up button
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 14));
        signUpButton.setBackground(new Color(255, 153, 51));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        signUpButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        signUpButton.setBorderPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setOpaque(true);
        signUpButton.setBorder(BorderFactory.createLineBorder(new Color(255, 153, 51), 2));
        signUpButton.addActionListener(new SignUpAction());

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);

        frame.add(loginPanel, BorderLayout.CENTER); // Add login panel to CENTER
        frame.add(buttonPanel, BorderLayout.SOUTH); // Add button panel to SOUTH

        // Status label
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        frame.add(statusLabel, BorderLayout.NORTH); // Place statusLabel at the top

        // Set frame visible
        frame.setVisible(true);
    }

    private class SignInAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticate(username, password)) {
                frame.dispose(); // Close the sign-in window if successful
                runMedicalChatbotClient(); // Call MedicalChatbotClient after successful sign-in
            } else {
                statusLabel.setText("Invalid username or password");
                statusLabel.setForeground(Color.RED);
            }
        }
    }

    private class SignUpAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispose(); // Close the sign-in window
            new SignUpPage(); // Open the sign-up page
        }
    }

    private boolean authenticate(String username, String password) {
        try {
            String response = ChatBotBase.signInRequest(username, password);
            Log.info("Sign In Response: {}", response);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void runMedicalChatbotClient() {
        SwingUtilities.invokeLater(() -> {
            // Run MedicalChatbotClient directly after successful sign-in
            new MedicalChatbotClient();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignInPage::new);
    }
}
