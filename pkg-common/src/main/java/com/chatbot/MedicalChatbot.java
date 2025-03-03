package com.chatbot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.chatbot.model.Conversation;
import com.chatbot.model.ConversationsWrapper;
import com.chatbot.utils.Constants;
import com.chatbot.utils.Log;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MedicalChatbot {
    private static ConversationsWrapper conversationsData;

    static {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = MedicalChatbot.class.getClassLoader().getResourceAsStream(Constants.CHATBOT_MEDICAL_FILE);

            if (inputStream == null) {
                throw new IOException("File not found in resources.");
            }

            conversationsData = objectMapper.readValue(inputStream, ConversationsWrapper.class);
        } catch (IOException e) {
            Log.error("Error reading JSON file: {}", e.getMessage());
        }
    }

    public static String getAnswer(String question) {
        if (conversationsData == null || conversationsData.getConversations().isEmpty()) {
            return Constants.MSG_NOT_FOUND;
        }
    
        // Normalize user input for better matching
        String lowerQuestion = question.toLowerCase().trim();
    
        for (Conversation conversation : conversationsData.getConversations()) {
            // Allow partial matching for better accuracy
            if (lowerQuestion.contains(conversation.getQuestion().toLowerCase())) {
                return conversation.getAnswer();
            }
        }
        return Constants.MSG_NOT_FOUND;
    }    

    public static List<String> getAllQuestions() {
        if (conversationsData == null || conversationsData.getConversations() == null) {
            Log.warn("Conversation data is null when trying to get all questions");
            return new ArrayList<String>();
        }
        return conversationsData.getConversations().stream().map(c -> c.getQuestion()).toList();
    }
}
