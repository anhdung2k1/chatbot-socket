package com.chatbot.ui;

import com.chatbot.services.ChatBotClientService;
import com.chatbot.utils.Log;
import com.chatbot.utils.PatternChatBot;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatbotUI {
    private final JTextArea chatArea;
    private final ChatBotClientService client;
    private final JLabel statusLabel;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JList<String> subjectList;
    private final DefaultListModel<String> subjectListModel;
    private final JPanel questionPanel;
    private final Map<String, List<Integer>> subjectToQuestionIds = new HashMap<>();
    private final Map<Integer, String> questionIdToText = new HashMap<>();

    public ChatbotUI() {
        client = ChatBotClientService.getInstance();

        JFrame frame = new JFrame("Chatbot");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        statusLabel = new JLabel("Select a subject to view questions", SwingConstants.CENTER);
        frame.add(statusLabel, BorderLayout.NORTH);

        subjectListModel = new DefaultListModel<>();
        subjectList = new JList<>(subjectListModel);
        subjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedSubject = subjectList.getSelectedValue();
                displayQuestionsForSubject(selectedSubject);
            }
        });
        JScrollPane subjectScrollPane = new JScrollPane(subjectList);
        subjectScrollPane.setPreferredSize(new Dimension(200, 0));
        frame.add(subjectScrollPane, BorderLayout.WEST);

        questionPanel = new JPanel();
        questionPanel.setLayout(new GridLayout(0, 1));
        JScrollPane questionScrollPane = new JScrollPane(questionPanel);
        questionScrollPane.setPreferredSize(new Dimension(300, 0));
        frame.add(questionScrollPane, BorderLayout.EAST);

        loadSubjectsAndQuestions();

        frame.setVisible(true);
    }

    private void loadSubjectsAndQuestions() {
        client.sendMessage(PatternChatBot.GET_ALL_SUBJECTS, "");
        try {
            String subjectResponse = client.receiveMessage(PatternChatBot.GET_ALL_SUBJECTS);
            Log.info("questionStr: {}", subjectResponse);

            JsonNode root = objectMapper.readTree(subjectResponse);
            for (JsonNode subjectNode : root) {
                String subjectName = subjectNode.get("subjectName").asText().trim();
                subjectListModel.addElement(subjectName);

                List<Integer> questionIds = new ArrayList<>();
                for (JsonNode q : subjectNode.get("questions")) {
                    int questionId = q.get("questionsId").asInt();
                    String questionText = q.get("question").asText();
                    questionIds.add(questionId);
                    questionIdToText.put(questionId, questionText);
                }
                subjectToQuestionIds.put(subjectName, questionIds);
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> statusLabel.setText("Error: " + e.getMessage()));
        }
    }

    private void displayQuestionsForSubject(String subject) {
        Log.info("Selected subject: {}", subject);
        questionPanel.removeAll();
        List<Integer> questionIds = subjectToQuestionIds.getOrDefault(subject, new ArrayList<>());
        Log.info("Found {} questions", questionIds.size());

        for (Integer id : questionIds) {
            String question = questionIdToText.get(id);
            Log.info("Adding question: {}", question);
            JButton button = new JButton("<html><body style='width: 200px'>" + question + "</body></html>");
            button.addActionListener(e -> sendQuestion(id, question));
            questionPanel.add(button);
        }

        questionPanel.revalidate();
        questionPanel.repaint();
    }

    private void sendQuestion(int questionId, String questionText) {
        SwingUtilities.invokeLater(() -> chatArea.append("You: " + questionText + "\n"));
        new Thread(() -> {
            JsonNode payload = objectMapper.createObjectNode()
                    .put("questionsId", questionId);
            final String jsonPayload;
            try {
                jsonPayload = objectMapper.writeValueAsString(payload);
            } catch (IOException e) {
                statusLabel.setText("Error preparing question payload.");
                statusLabel.setForeground(Color.RED);
                return;
            }
            client.sendMessage(PatternChatBot.ASK_QUESTION, jsonPayload);

            try {
                String response = client.receiveMessage(PatternChatBot.ASK_QUESTION);
                JsonNode answerNode = objectMapper.readTree(response);
                String answer = answerNode.has("answer") ? answerNode.get("answer").asText() : response;
                SwingUtilities.invokeLater(() -> {
                    chatArea.append("Chatbot: " + answer + "\n\n");
                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                });
            } catch (IOException e) {
                Log.error("Error receiving response: {}", e.getMessage());
                SwingUtilities.invokeLater(() -> chatArea.append("Error: Unable to read from server.\n\n"));
            }
        }).start();
    }
}