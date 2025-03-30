package com.chatbot;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.chatbot.utils.Constants;
import com.chatbot.utils.Log;

public class MedicalChatbotClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JTextArea chatArea;

    public MedicalChatbotClient() {
        try {
            socket = new Socket(Constants.SERVER_HOST, Constants.SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            chatArea = new JTextArea();
            Log.info("Connected to Medical Chatbot Server at {}:{}", Constants.SERVER_HOST, Constants.SERVER_PORT);

            // Read and display the welcome message
            String welcomeMessage = in.readLine();
            if (welcomeMessage != null && !welcomeMessage.isEmpty()) {
                Log.info("Received welcome message: {}", welcomeMessage);
                chatArea.append("Chatbot: " + welcomeMessage + "\n\n");
            }
        } catch (IOException e) {
            Log.error("Failed to connect to the server: {}", e.getMessage());
            return;
        }

        // Initialize GUI
        JFrame frame = new JFrame("Medical Chatbot");
        frame.setSize(1200, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        chatArea.setEditable(false);
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1));

        List<String> questions = MedicalChatbot.getAllQuestions();
        for (String question : questions) {
            JButton button = new JButton(question);
            button.addActionListener((ActionEvent e) -> sendQuestion(question));
            buttonPanel.add(button);
        }

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void sendQuestion(String question) {
        // Update UI immediately to show the question
        SwingUtilities.invokeLater(() -> chatArea.append("You: " + question + "\n"));
    
        // Send question and receive response in a background thread
        new Thread(() -> {
            if (socket == null || socket.isClosed() || !socket.isConnected() || out == null) {
                SwingUtilities.invokeLater(() -> 
                    chatArea.append("Error: Not connected to server.\n\n"));
                return;
            }
    
            out.println(question);
            Log.info("Sent question to server: {}", question);
    
            try {
                // Set a timeout for the socket if not already set
                try {
                    if (socket.getSoTimeout() == 0) {
                        socket.setSoTimeout(30000); // 30 seconds timeout
                    }
                } catch (IOException e) {
                    Log.warn("Failed to set socket timeout: {}", e.getMessage());
                }
    
                String response = in.readLine();
                if (response != null) {
                    SwingUtilities.invokeLater(() -> {
                        chatArea.append("Chatbot: " + response + "\n\n");
                        // Scroll to the bottom to show the latest message
                        chatArea.setCaretPosition(chatArea.getDocument().getLength());
                    });
                    Log.info("Received response from server: {}", response);
                } else {
                    Log.warn("Received null response from server.");
                    SwingUtilities.invokeLater(() -> 
                        chatArea.append("Error: Server disconnected.\n\n"));
                }
            } catch (SocketTimeoutException e) {
                Log.error("Timeout waiting for server response: {}", e.getMessage());
                SwingUtilities.invokeLater(() -> 
                    chatArea.append("Error: Server response timeout.\n\n"));
            } catch (IOException e) {
                Log.error("Error while communicating with the server: {}", e.getMessage());
                SwingUtilities.invokeLater(() -> 
                    chatArea.append("Error: Unable to communicate with the server.\n\n"));
            }
        }).start();
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MedicalChatbotClient::new);
    }
}