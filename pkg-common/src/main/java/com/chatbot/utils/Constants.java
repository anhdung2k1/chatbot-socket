package com.chatbot.utils;

public class Constants {
    // Server Configuration
    public static final String SERVER_HOST = getEnv("SERVER_HOST", "localhost");
    public static final int SERVER_PORT = getEnvInt("SERVER_PORT", 8000);
    public static final int THREAD_POOL_ALIVE = getEnvInt("THREAD_POOL_ALIVE", 5);
    public static final int THREAD_POOL_MAX = getEnvInt("THREAD_POOL_MAX", 100);
    public static final long THREAD_IDLE_TIME = getEnvLong("THREAD_IDLE_TIME", 300L);
    // File Paths
    public static final String CHATBOT_MEDICAL_FILE = "conversations/chatbot-medical.json";
    public final static String MSG_NOT_FOUND = "Sorry, I don't have an answer for that question.";
    // DB Config
    public static final String DB_HOST = getEnv("DB_HOST", "localhost");
    public static final int DB_PORT = getEnvInt("DB_PORT", 3306);
    public static final String DB_NAME = getEnv("DB_NAME", "chatbot");
    public static final String DB_USERNAME = getEnv("DB_USERNAME", "chatbot");
    public static final String DB_PASSWORD = getEnv("DB_PASSWORD", "chatbot");
    public static final String DB_URL = String.format("jdbc:mysql://%s:%s/%s", DB_HOST, DB_PORT, DB_NAME);
    /**
     * Helper method to get environment variable as int
     */
    private static int getEnvInt(String envVar, int defaultValue) {
        String value = System.getenv(envVar);
        try {
            return (value != null) ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Helper method to get environment variable as long
     */
    private static long getEnvLong(String envVar, long defaultValue) {
        String value = System.getenv(envVar);
        try {
            return (value != null) ? Long.parseLong(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Helper method to get environment variable as String.
     */
    private static String getEnv(String envVar, String defaultValue) {
        String value = System.getenv(envVar);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }
}
