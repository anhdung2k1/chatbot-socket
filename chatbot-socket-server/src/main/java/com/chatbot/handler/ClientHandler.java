package com.chatbot.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.chatbot.services.ChatBotServerService;
import com.chatbot.utils.Constants;
import com.chatbot.utils.Log;
import com.chatbot.utils.PatternChatBot;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Log.info("Handling new client: {}", socket.getInetAddress());

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String request, response;

            while ((request = in.readLine()) != null) {
                Log.info("Received request: {}", request);
                try {
                    String pattern = PatternChatBot.getPattern(request);
                    String patternValue = PatternChatBot.getValuePattern(request);

                    response = switch (pattern.toUpperCase()) {
                        case PatternChatBot.SIGN_IN ->
                                PatternChatBot.SIGN_IN + "|" + ChatBotServerService.authRequest(patternValue, true);
                        case PatternChatBot.SIGN_UP ->
                                PatternChatBot.SIGN_UP + "|" + ChatBotServerService.authRequest(patternValue, false);
                        case PatternChatBot.SIGN_OUT ->
                                PatternChatBot.SIGN_OUT + "|" + ChatBotServerService.signOut();
                        case PatternChatBot.GET_ALL_SUBJECTS ->
                                PatternChatBot.GET_ALL_SUBJECTS + "|" + ChatBotServerService.getAllSubjects(patternValue);
                        case PatternChatBot.CREATE_SUBJECT ->
                                PatternChatBot.CREATE_SUBJECT + "|" + ChatBotServerService.createNewSubject(patternValue);
                        case PatternChatBot.UPDATE_SUBJECT ->
                                PatternChatBot.UPDATE_SUBJECT + "|" + ChatBotServerService.updateSubject(patternValue);
                        case PatternChatBot.DELETE_SUBJECT ->
                                PatternChatBot.DELETE_SUBJECT + "|" + ChatBotServerService.deleteSubject(patternValue);
                        case PatternChatBot.CREATE_QUESTION ->
                                PatternChatBot.CREATE_QUESTION + "|" + ChatBotServerService.createNewQuestion(patternValue);
                        case PatternChatBot.UPDATE_QUESTION ->
                                PatternChatBot.UPDATE_QUESTION + "|" + ChatBotServerService.updateQuestion(patternValue);
                        case PatternChatBot.DELETE_QUESTION ->
                                PatternChatBot.DELETE_QUESTION + "|" + ChatBotServerService.deleteQuestion(patternValue);
                        case PatternChatBot.ASK_QUESTION ->
                                PatternChatBot.ASK_QUESTION + "|" + ChatBotServerService.getQuestionAndAnswerById(patternValue);
                        default -> {
                            Log.warn("Unknown pattern received: {}", pattern);
                            yield "ERROR|Unknown request pattern: " + pattern;
                        }
                    };

                    Log.info("Server Response to Client: {}", response);
                    out.println(response);
                } catch (Exception e) {
                    Log.error("Error processing request '{}'", request, e);
                    out.println("ERROR|Failed to process request");
                }
            }
        } catch (IOException e) {
            Log.warn("Error handling client: {}", socket.getInetAddress(), e);
        } finally {
            try {
                socket.close();
                Log.info("Client disconnected: {}:{}", socket.getInetAddress(), socket.getPort());
            } catch (IOException e) {
                Log.error("Error closing client socket: {}", socket.getInetAddress(), e);
            }
        }
    }
}