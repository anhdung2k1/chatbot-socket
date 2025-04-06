package com.chatbot.ui;

import com.chatbot.services.ChatBotClientService;
import com.chatbot.utils.Log;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.chatbot.utils.PatternChatBot;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MedicalChatbotUI {
    private final JTextArea chatArea;
    private final ChatBotClientService client;
    private final JLabel statusLabel;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MedicalChatbotUI() {
        client = ChatBotClientService.getInstance();

        JFrame frame = new JFrame("Medical Chatbot");
        frame.setSize(1200, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        statusLabel = new JLabel("", SwingConstants.CENTER);
        frame.add(statusLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        client.sendMessage(PatternChatBot.GET_ALL_QUESTIONS, "");
        List<String> questions = new ArrayList<>();
        try {
            String questionStr = client.receiveMessage(PatternChatBot.GET_ALL_QUESTIONS);
            Log.info("questionStr: {}", questionStr);
            // TODO(): Need to extract questionStr to List<String>
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> statusLabel.setText("Error: " + e.getMessage()));
        }
        for (String question : questions) {
            JButton button = new JButton(question);
            button.addActionListener((ActionEvent e) -> sendQuestion(question));
            buttonPanel.add(button);
        }

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void sendQuestion(String question) {
        SwingUtilities.invokeLater(() -> chatArea.append("You: " + question + "\n"));
        new Thread(() -> {
            JsonNode payload = objectMapper.createObjectNode()
                    .put("question", question);
            final String jsonPayload;
            try {
                jsonPayload = objectMapper.writeValueAsString(payload);
            } catch (IOException e) {
                statusLabel.setText("Error preparing questions data.");
                statusLabel.setForeground(Color.RED);
                return;
            }
            client.sendMessage(PatternChatBot.ASK_QUESTION, jsonPayload);

            try {
                String response = client.receiveMessage(PatternChatBot.ASK_QUESTION);
                SwingUtilities.invokeLater(() -> {
                    chatArea.append("Chatbot: " + response + "\n\n");
                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                });
            } catch (IOException e) {
                Log.error("Error receiving response: {}", e.getMessage());
                SwingUtilities.invokeLater(() -> chatArea.append("Error: Unable to read from server.\n\n"));
            }
        }).start();
    }
}
