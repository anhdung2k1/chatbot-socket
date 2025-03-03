package com.chatbot;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.chatbot.model.Conversation;
import com.chatbot.model.ConversationsWrapper;
import com.chatbot.utils.Log;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MedicalChatbot {
    private static ConversationsWrapper conversationsData;
    private final static String MSG_NOT_FOUND = "Sorry, I don't have an answer for that question.";

    static {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = MedicalChatbot.class.getClassLoader().getResourceAsStream("chatbot-medical.json");

            if (inputStream == null) {
                throw new IOException("File not found in resources.");
            }

            conversationsData = objectMapper.readValue(inputStream, ConversationsWrapper.class);
        } catch (IOException e) {
            Log.error("Error reading JSON file: {}", e.getMessage());
        }
    }

    public static String getAnswer(String question) {
        if (conversationsData == null || conversationsData.conversations.isEmpty()) {
            return MSG_NOT_FOUND;
        }
    
        // Normalize user input for better matching
        String lowerQuestion = question.toLowerCase().trim();
    
        for (Conversation conversation : conversationsData.conversations) {
            // Allow partial matching for better accuracy
            if (lowerQuestion.contains(conversation.question.toLowerCase())) {
                return conversation.answer;
            }
        }
        return MSG_NOT_FOUND;
    }    

    public static List<String> getAllQuestions() {
        return conversationsData.conversations.stream().map(c -> c.question).toList();
    }
}
