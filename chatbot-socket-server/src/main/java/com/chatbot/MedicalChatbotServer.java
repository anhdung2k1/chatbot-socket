package com.chatbot;

import com.chatbot.handler.SocketHandler;

public class MedicalChatbotServer {
    public static void main(String[] args) {
       SocketHandler socketHandler = new SocketHandler();
       socketHandler.startServer();
    }
}