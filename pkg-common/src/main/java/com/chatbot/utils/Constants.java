package com.chatbot.utils;

public class Constants {
    // Server Configuration
    public static final String SERVER_HOST = getEnv("SERVER_HOST", "localhost");
    public static final int SERVER_PORT = getEnvInt("SERVER_PORT", 8000);
    public static final int THREAD_POOL_ALIVE = getEnvInt("THREAD_POOL_ALIVE", 5);
    public static final int THREAD_POOL_MAX = getEnvInt("THREAD_POOL_MAX", 100);
    public static final long THREAD_IDLE_TIME = getEnvLong("THREAD_IDLE_TIME", 300L);
    // API Config
    public static final String API_HOST = getEnv("API_HOST", "localhost");
    public static final int API_PORT = getEnvInt("API_PORT", 9090);
    public static final String API_URL = String.format("http://%s:%s/api/v1/authentication", API_HOST, API_PORT);
    public final static String MSG_NOT_FOUND = "Sorry, I don't have an answer for that question.";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

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
