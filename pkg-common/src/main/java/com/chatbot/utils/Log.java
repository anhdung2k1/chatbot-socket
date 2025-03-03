package com.chatbot.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Log {
    private static final Logger logger = Logger.getLogger(Log.class.getName());

    static {
        Formatter customFormatter = new Formatter() {
            private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public String format(LogRecord record) {
                String timestamp = dateFormat.format(new Date(record.getMillis()));
                String level = record.getLevel().getName();
                String message = record.getMessage();
                return String.format("%s - %s - %s%n", timestamp, level, message);
            }
        };

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(customFormatter);
        consoleHandler.setLevel(Level.ALL);

        logger.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false); // Prevent duplicate logs
    }

    private static String formatMessage(String message, Object... args) {
        if (args.length == 0) {
            return message;
        }
    
        // Convert '{}' placeholders while preserving escaped '{{}}'
        StringBuilder result = new StringBuilder();
        int lastPos = 0, argIndex = 0;
    
        for (int i = 0; i < message.length() - 1; i++) {
            if (message.charAt(i) == '{' && message.charAt(i + 1) == '}') {
                // Replace '{}' with the corresponding argument
                result.append(message, lastPos, i);
                if (argIndex < args.length) {
                    result.append(args[argIndex] != null ? args[argIndex] : "null");
                    argIndex++;
                } else {
                    // If no arguments are left, keep '{}' as it is
                    result.append("{}");
                }
                lastPos = i + 2;
            } else if (message.charAt(i) == '{' && message.charAt(i + 1) == '{') {
                // Preserve escaped '{{' (convert '{{' to '{')
                result.append(message, lastPos, i).append('{');
                lastPos = i + 2;
            } else if (message.charAt(i) == '}' && message.charAt(i + 1) == '}') {
                // Preserve escaped '}}' (convert '}}' to '}')
                result.append(message, lastPos, i).append('}');
                lastPos = i + 2;
            }
        }
        // Append the rest of the message
        result.append(message.substring(lastPos));
    
        return result.toString();
    }    

    public static void info(String message, Object... args) {
        logger.info(formatMessage(message, args));
    }

    public static void warn(String message, Object... args) {
        logger.warning(formatMessage(message, args));
    }

    public static void error(String message, Object... args) {
        logger.severe(formatMessage(message, args));
    }
}
