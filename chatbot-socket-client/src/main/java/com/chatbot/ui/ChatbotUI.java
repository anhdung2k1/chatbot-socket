package com.chatbot.ui;

import com.chatbot.services.ChatBotClientService;
import com.chatbot.utils.Constants;
import com.chatbot.utils.Log;
import com.chatbot.utils.PatternChatBot;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
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
    private final Map<String, Integer> subjectNameToId = new HashMap<>();

    private final Color PRIMARY_COLOR = new Color(30, 144, 255); // Dodger Blue
    private final Color HOVER_COLOR = new Color(220, 235, 255);  // Light Blue Hover
    private final Color BACKGROUND_COLOR = new Color(245, 250, 255);
    private final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public ChatbotUI() {
        client = ChatBotClientService.getInstance();

        JFrame frame = new JFrame("AI Medical Chatbot");
        frame.setSize(1300, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(FONT_REGULAR);
        chatArea.setBackground(BACKGROUND_COLOR);
        chatArea.setForeground(Color.DARK_GRAY);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Chat Area"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane chatScrollPane = new JScrollPane(chatArea);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        statusLabel = new JLabel("Select or manage a subject", SwingConstants.LEFT);
        statusLabel.setFont(FONT_BOLD);
        statusLabel.setForeground(new Color(60, 60, 60));
        topPanel.add(statusLabel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        styleButton(logoutButton, new Color(220, 53, 69));
        logoutButton.addActionListener(e -> performLogout(frame));
        topPanel.add(logoutButton, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);

        subjectListModel = new DefaultListModel<>();
        subjectList = new JList<>(subjectListModel);
        subjectList.setFont(FONT_REGULAR);
        subjectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectList.setBorder(BorderFactory.createTitledBorder("Subjects"));
        subjectList.setBackground(Color.WHITE);
        subjectList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedSubject = subjectList.getSelectedValue();
                chatArea.setText("");
                displayQuestionsForSubject(selectedSubject);
            }
        });
        JScrollPane subjectScrollPane = new JScrollPane(subjectList);

        JButton createSubjectButton = new JButton("Add Subject");
        JButton editSubjectButton = new JButton("Edit Subject");
        JButton deleteSubjectButton = new JButton("Delete Subject");
        styleButton(createSubjectButton);
        styleButton(editSubjectButton);
        styleButton(deleteSubjectButton);

        JPanel subjectButtonPanel = new JPanel();
        subjectButtonPanel.setLayout(new BoxLayout(subjectButtonPanel, BoxLayout.Y_AXIS));
        subjectButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        subjectButtonPanel.setBackground(BACKGROUND_COLOR);
        subjectButtonPanel.add(createSubjectButton);
        subjectButtonPanel.add(Box.createVerticalStrut(5));
        subjectButtonPanel.add(editSubjectButton);
        subjectButtonPanel.add(Box.createVerticalStrut(5));
        subjectButtonPanel.add(deleteSubjectButton);

        JPanel subjectPanel = new JPanel(new BorderLayout());
        subjectPanel.add(subjectScrollPane, BorderLayout.CENTER);
        subjectPanel.add(subjectButtonPanel, BorderLayout.SOUTH);

        questionPanel = new JPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setMinimumSize(new Dimension(300, 400));
        questionPanel.setPreferredSize(new Dimension(350, 500));
        questionPanel.setBorder(BorderFactory.createTitledBorder("Questions"));

        JScrollPane questionScrollPane = new JScrollPane(questionPanel);
        questionScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        questionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        questionScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JSplitPane leftSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, subjectPanel, chatScrollPane);
        leftSplit.setResizeWeight(0.2);
        leftSplit.setDividerSize(6);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, questionScrollPane);
        mainSplit.setResizeWeight(0.75);
        mainSplit.setDividerSize(6);

        frame.add(mainSplit, BorderLayout.CENTER);

        loadSubjectsAndQuestions();
        frame.setVisible(true);
    }

    private void performLogout(JFrame frame) {
        int confirm = JOptionPane.showConfirmDialog(null, "Do you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String response = client.sendAndReceive(PatternChatBot.SIGN_OUT, "");
                if (response.equalsIgnoreCase(Constants.SUCCESS)) {
                    JOptionPane.showMessageDialog(null, "Logged out successfully.");
                    frame.dispose();
                    SwingUtilities.invokeLater(SignInPage::new);
                } else {
                    JOptionPane.showMessageDialog(null, "Logout failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                Log.error("Logout failed: {}", e.getMessage());
                JOptionPane.showMessageDialog(null, "An error occurred during logout.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void styleButton(JButton btn) {
        styleButton(btn, PRIMARY_COLOR);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(FONT_REGULAR);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
    private JPanel getJPanel(Integer id, String question) {
        JPanel questionItem = new JPanel(new BorderLayout());
        questionItem.setBackground(Color.WHITE);
        questionItem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JButton qButton = new JButton(question);
        qButton.setFocusPainted(false);
        qButton.setContentAreaFilled(false);
        qButton.setBorderPainted(false);
        qButton.setFont(FONT_REGULAR);
        qButton.setHorizontalAlignment(SwingConstants.LEFT);
        qButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        qButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                qButton.setBackground(HOVER_COLOR);
                qButton.setOpaque(true);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                qButton.setOpaque(false);
                qButton.setBackground(null);
            }
        });

        qButton.addActionListener(e -> sendQuestion(id, question));

        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        styleButton(editBtn);
        styleButton(deleteBtn);

        editBtn.addActionListener(e -> showEditQuestionDialog(id, question));
        deleteBtn.addActionListener(e -> deleteQuestion(id));

        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);

        questionItem.add(qButton, BorderLayout.CENTER);
        questionItem.add(actionPanel, BorderLayout.EAST);
        return questionItem;
    }

    private void loadSubjectsAndQuestions() {
        String selectedSubject = subjectList.getSelectedValue();

        subjectListModel.clear();
        subjectToQuestionIds.clear();
        questionIdToText.clear();
        subjectNameToId.clear();

        client.sendMessage(PatternChatBot.GET_ALL_SUBJECTS, "");
        try {
            String subjectResponse = client.receiveMessage(PatternChatBot.GET_ALL_SUBJECTS);
            JsonNode root = objectMapper.readTree(subjectResponse);

            for (JsonNode subjectNode : root) {
                String subjectName = subjectNode.get("subjectName").asText().trim();
                int subjectId = subjectNode.get("subjectId").asInt();

                subjectNameToId.put(subjectName, subjectId);
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

            if (selectedSubject != null && subjectNameToId.containsKey(selectedSubject)) {
                subjectList.setSelectedValue(selectedSubject, true);
            }

        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> statusLabel.setText("Error: " + e.getMessage()));
        }
    }

    private void displayQuestionsForSubject(String subject) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<Integer> questionIds = subjectToQuestionIds.getOrDefault(subject, new ArrayList<>());
        for (Integer id : questionIds) {
            String question = questionIdToText.get(id);
            JPanel item = getJPanel(id, question);
            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            wrapper.add(item);
            wrapper.add(Box.createVerticalStrut(10));
        }

        wrapper.add(Box.createVerticalGlue());

        JButton addQuestionBtn = new JButton("Add Question");
        addQuestionBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addQuestionBtn.addActionListener(e -> showCreateQuestionDialog(subject));
        wrapper.add(addQuestionBtn);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        questionPanel.removeAll();
        questionPanel.add(scrollPane, BorderLayout.CENTER);
        questionPanel.revalidate();
        questionPanel.repaint();
    }

    private void showCreateSubjectDialog() {
        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        Object[] fields = {"Subject Name:", nameField, "Description:", descField};
        int option = JOptionPane.showConfirmDialog(null, fields, "Add New Subject", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            ObjectNode subject = objectMapper.createObjectNode();
            subject.put("subjectName", nameField.getText());
            subject.put("subjectDescription", descField.getText());
            try {
                String response = client.sendAndReceive(PatternChatBot.CREATE_SUBJECT, objectMapper.writeValueAsString(subject));
                if (response != null) loadSubjectsAndQuestions();
            } catch (Exception ex) {
                Log.error("Failed to create subject: {}", ex.getMessage());
            }
        }
    }

    private void showEditSubjectDialog() {
        String subject = subjectList.getSelectedValue();
        if (subject == null) return;

        JTextField nameField = new JTextField(subject);
        JTextField descField = new JTextField();
        Object[] fields = {"Subject Name:", nameField, "Description:", descField};
        int option = JOptionPane.showConfirmDialog(null, fields, "Edit Subject", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            ObjectNode subjectJson = objectMapper.createObjectNode();
            subjectJson.put("subjectId", subjectNameToId.get(subject));
            subjectJson.put("subjectName", nameField.getText());
            subjectJson.put("subjectDescription", descField.getText());
            try {
                String response = client.sendAndReceive(PatternChatBot.UPDATE_SUBJECT, objectMapper.writeValueAsString(subjectJson));
                if (response != null) loadSubjectsAndQuestions();
            } catch (Exception ex) {
                Log.error("Failed to update subject: {}", ex.getMessage());
            }
        }
    }

    private void deleteSelectedSubject() {
        String subject = subjectList.getSelectedValue();
        if (subject == null) return;

        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to delete subject?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("subjectId", subjectNameToId.get(subject));
            try {
                String response = client.sendAndReceive(PatternChatBot.DELETE_SUBJECT, objectMapper.writeValueAsString(payload));
                if (response != null) loadSubjectsAndQuestions();
            } catch (Exception ex) {
                Log.error("Failed to delete subject: {}", ex.getMessage());
            }
        }
    }

    private void showCreateQuestionDialog(String subject) {
        JTextField questionField = new JTextField();
        JTextField answerField = new JTextField();
        Object[] fields = {"Question:", questionField, "Answer:", answerField};
        int option = JOptionPane.showConfirmDialog(null, fields, "Add New Question", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            ObjectNode questionJson = objectMapper.createObjectNode();
            questionJson.put("question", questionField.getText());
            questionJson.put("answer", answerField.getText());
            questionJson.put("subjectId", subjectNameToId.get(subject));
            try {
                String response = client.sendAndReceive(PatternChatBot.CREATE_QUESTION, objectMapper.writeValueAsString(questionJson));
                if (response != null) loadSubjectsAndQuestions();
            } catch (Exception ex) {
                Log.error("Failed to create question: {}", ex.getMessage());
            }
        }
    }

    private void showEditQuestionDialog(int questionId, String oldText) {
        JTextField questionField = new JTextField(oldText);
        JTextField answerField = new JTextField();
        Object[] fields = {"Edit Question:", questionField, "Answer:", answerField};
        int option = JOptionPane.showConfirmDialog(null, fields, "Edit Question", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            ObjectNode json = objectMapper.createObjectNode();
            json.put("questionsId", questionId);
            json.put("question", questionField.getText());
            json.put("answer", answerField.getText());
            try {
                String response = client.sendAndReceive(PatternChatBot.UPDATE_QUESTION, objectMapper.writeValueAsString(json));
                if (response != null) loadSubjectsAndQuestions();
            } catch (Exception ex) {
                Log.error("Failed to update question: {}", ex.getMessage());
            }
        }
    }

    private void deleteQuestion(int questionId) {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure to delete this question?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ObjectNode json = objectMapper.createObjectNode();
            json.put("questionsId", questionId);
            try {
                String response = client.sendAndReceive(PatternChatBot.DELETE_QUESTION, objectMapper.writeValueAsString(json));
                if (response != null) loadSubjectsAndQuestions();
            } catch (Exception ex) {
                Log.error("Failed to delete question: {}", ex.getMessage());
            }
        }
    }

    private void sendQuestion(int questionId, String questionText) {
        SwingUtilities.invokeLater(() -> chatArea.append("You: " + questionText + "\n"));
        new Thread(() -> {
            ObjectNode payload = objectMapper.createObjectNode().put("questionsId", questionId);
            try {
                String response = client.sendAndReceive(PatternChatBot.ASK_QUESTION, objectMapper.writeValueAsString(payload));
                if (response == null) return;
                JsonNode answerNode = objectMapper.readTree(response);
                String answer = answerNode.has("answer") ? answerNode.get("answer").asText() : response;
                SwingUtilities.invokeLater(() -> {
                    chatArea.append("Chatbot: " + answer + "\n\n");
                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                });
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> chatArea.append("Error: Unable to get response\n\n"));
            }
        }).start();
    }
}
