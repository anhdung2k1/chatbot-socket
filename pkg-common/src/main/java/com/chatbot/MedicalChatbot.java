package com.chatbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chatbot.connector.GenericDatabaseManager;
import com.chatbot.utils.Constants;
import com.chatbot.utils.Log;

public class MedicalChatbot {

    /**
     * Fetches the answer from the database based on the user's question.
     *
     * @param question The question asked by the user.
     * @return The chatbot's response, or a default message if not found.
     */
    public static String getAnswer(String question) {
        if (question == null || question.trim().isEmpty()) {
            return Constants.MSG_NOT_FOUND;
        }

        // Normalize input to lowercase for better matching
        String lowerQuestion = question.toLowerCase().trim();

        // Retrieve matching record from database
        Map<String, Object> record = GenericDatabaseManager.get("questions", "question", lowerQuestion);

        if (record != null && record.containsKey("answer")) {
            return record.get("answer").toString();
        }
        return Constants.MSG_NOT_FOUND;
    }

    /**
     * Retrieves all stored questions from the database.
     *
     * @return List of all questions available in the database.
     */
    public static List<String> getAllQuestions() {
        List<String> questions = new ArrayList<>();
        List<Map<String, Object>> records = GenericDatabaseManager.getAll("questions");

        if (records.isEmpty()) {
            Log.warn("No questions found in the database.");
            return questions;
        }

        for (Map<String, Object> record : records) {
            if (record.containsKey("question")) {
                questions.add(record.get("question").toString());
            }
        }
        return questions;
    }

    /**
     * Adds a new chatbot question and answer to the database.
     *
     * @param question The question to add.
     * @param answer   The corresponding chatbot response.
     * @return true if the operation is successful, false otherwise.
     */
    public static boolean addQuestion(String question, String answer) {
        if (question == null || question.trim().isEmpty() || answer == null || answer.trim().isEmpty()) {
            Log.warn("Invalid question or answer. Cannot add to database.");
            return false;
        }

        Map<String, Object> insertData = new HashMap<>();
        insertData.put("question", question.trim());
        insertData.put("answer", answer.trim());

        return GenericDatabaseManager.insert("questions", insertData);
    }

    /**
     * Updates an existing chatbot answer.
     *
     * @param question The question whose answer needs to be updated.
     * @param newAnswer The new answer to update.
     * @return true if the update was successful, false otherwise.
     */
    public static boolean updateAnswer(String question, String newAnswer) {
        if (question == null || question.trim().isEmpty() || newAnswer == null || newAnswer.trim().isEmpty()) {
            Log.warn("Invalid question or answer. Cannot update database.");
            return false;
        }

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("answer", newAnswer.trim());

        return GenericDatabaseManager.update("questions", updateData, "question", question.trim());
    }

    /**
     * Deletes a chatbot question and answer from the database.
     *
     * @param question The question to remove.
     * @return true if the deletion was successful, false otherwise.
     */
    public static boolean deleteQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            Log.warn("Invalid question. Cannot delete from database.");
            return false;
        }

        return GenericDatabaseManager.delete("questions", "question", question.trim());
    }
}
