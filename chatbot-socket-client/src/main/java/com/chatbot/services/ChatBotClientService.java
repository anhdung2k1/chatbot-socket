package com.chatbot.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.chatbot.utils.Constants;
import com.chatbot.utils.Log;
import com.chatbot.utils.PatternChatBot;

public class ChatBotClientService {
    private static ChatBotClientService instance;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private ChatBotClientService() {
        try {
            if (!isConnected()) {
                socket = new Socket(Constants.SERVER_HOST, Constants.SERVER_PORT);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                Log.info("Connected to server at {}:{}", Constants.SERVER_HOST, Constants.SERVER_PORT);
            }
        } catch (IOException e) {
            Log.error("Connection error: {}", e.getMessage());
        }
    }

    public static synchronized ChatBotClientService getInstance() {
        if (instance == null) {
            instance = new ChatBotClientService();
        }
        return instance;
    }

    public void sendMessage(String pattern, String jsonValue) {
        String message = pattern + "|";
        if (!jsonValue.isEmpty()) {
            message += jsonValue;
        }
        out.println(message);
        Log.info("Sent: {}", message);
    }

    public String receiveMessage(String pattern) throws IOException {
        String response = in.readLine();
        if (response.contains(pattern)) {
            Log.info("Received: {}", response);
            return PatternChatBot.getValuePattern(response);
        }
        return "";
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public String sendAndReceive(String pattern, String jsonValue) {
        try {
            sendMessage(pattern, jsonValue);
            return receiveMessage(pattern);
        } catch (IOException e) {
            Log.error("sendAndReceive error ({}): {}", pattern, e.getMessage());
            return null;
        }
    }
}