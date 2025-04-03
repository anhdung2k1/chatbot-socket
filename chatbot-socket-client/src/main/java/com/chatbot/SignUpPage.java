package com.chatbot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpPage {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton signUpButton;
    private JButton signInRedirectButton;

    public SignUpPage() {
        frame = new JFrame("Sign Up");
        frame.setSize(450, 350); // Tăng kích thước cửa sổ
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Tạo một panel trung tâm chứa các thành phần đăng ký
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tạo các Label và TextField
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(usernameLabel);
        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(34, 193, 195), 2)); // Border với màu xanh
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(34, 193, 195), 2)); // Border với màu xanh
        panel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(confirmPasswordLabel);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 16));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(new Color(34, 193, 195), 2)); // Border với màu xanh
        panel.add(confirmPasswordField);

        // Nút Đăng ký với màu sắc gradient và bo tròn góc
        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.setBackground(new Color(34, 193, 195)); // Màu đẹp cho nút
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        signUpButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        signUpButton.setBorderPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setOpaque(true);
        signUpButton.setBorder(BorderFactory.createLineBorder(new Color(34, 193, 195), 2)); // Bo tròn góc cho button
        panel.add(signUpButton);

        // Nút chuyển sang đăng nhập
        signInRedirectButton = new JButton("Sign In??");
        signInRedirectButton.setFont(new Font("Arial", Font.PLAIN, 14));
        signInRedirectButton.setBackground(new Color(255, 153, 51)); // Màu cam cho nút chuyển trang
        signInRedirectButton.setForeground(Color.WHITE);
        signInRedirectButton.setFocusPainted(false);
        signInRedirectButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        signInRedirectButton.setBorderPainted(false);
        signInRedirectButton.setContentAreaFilled(false);
        signInRedirectButton.setOpaque(true);
        signInRedirectButton.setBorder(BorderFactory.createLineBorder(new Color(255, 153, 51), 2)); // Bo tròn góc cho button
        panel.add(signInRedirectButton);

        // Thêm panel vào frame
        frame.add(panel, BorderLayout.CENTER);

        // Căn giữa các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Sự kiện cho nút Đăng ký
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });

        // Sự kiện chuyển sang trang đăng nhập
        signInRedirectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Đóng cửa sổ đăng ký
                new SignInPage(); // Mở cửa sổ đăng nhập
            }
        });

        // Đặt frame visible
        frame.setVisible(true);
    }

    private void handleSignUp() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(frame, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Gửi yêu cầu đăng ký
            URL url = new URL("http://localhost:9090/api/v1/authentications/signup");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format("{\"userName\":\"%s\", \"password\":\"%s\"}", username, password);

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"))) {
                writer.write(jsonInputString);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                JOptionPane.showMessageDialog(frame, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Tự động đăng nhập ngay sau khi đăng ký thành công
                if (authenticate(username, password)) {
                    JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose(); // Đóng cửa sổ đăng ký
                    new MedicalChatbotClient(); // Mở trang chính sau khi đăng nhập thành công
                } else {
                    JOptionPane.showMessageDialog(frame, "Login failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to create account.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức đăng nhập
    private boolean authenticate(String username, String password) {
        try {
            URL url = new URL("http://localhost:9090/api/v1/authentications/signin");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = String.format("{\"userName\":\"%s\", \"password\":\"%s\"}", username, password);

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"))) {
                writer.write(jsonInputString);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                return true; // Đăng nhập thành công
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpPage::new);
    }
}
