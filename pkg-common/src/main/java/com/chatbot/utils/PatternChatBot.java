package com.chatbot.utils;

/*
 * Class to handler Pattern connection between client and server socket
 * Eg: SIGN_IN|{"userName": "admin", "password": "Admin@123"}
 */
public class PatternChatBot {
    // Pattern for Authentication
    public static final String SIGN_IN = "SIGN_IN";
    public static final String SIGN_UP = "SIGN_UP";
    public static final String ASK_QUESTION = "ASK_QUESTION";
    public static final String GET_ALL_QUESTIONS = "GET_ALL_QUESTIONS";

    public static String getPattern(String pattern) {
        int posPattern = pattern.indexOf("|");
        return pattern.substring(0, posPattern);
    }

    public static String getValuePattern(String pattern) {
        int posPattern = pattern.indexOf("|");
        // Get all the substring and ignore '|'
        return pattern.substring(posPattern + 1);
    }
}
