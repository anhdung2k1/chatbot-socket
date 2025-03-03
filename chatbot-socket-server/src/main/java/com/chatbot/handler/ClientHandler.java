package com.chatbot.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.chatbot.MedicalChatbot;
import com.chatbot.utils.Log;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Log.info("Handling new client: {}", socket.getInetAddress());

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("Welcome to the Medical Chatbot! Select a question.");
            out.flush(); // Ensures the welcome message is sent immediately

            String question;
            while ((question = in.readLine()) != null) {
                if (question.equalsIgnoreCase("exit")) {
                    out.println("Goodbye!");
                    out.flush();
                    break;
                }

                String answer = MedicalChatbot.getAnswer(question);
                out.println(answer);
                out.flush(); // Ensure the answer is immediately sent to the client
                Log.info("Client asked: \"{}\" | Responded: \"{}\"", question, answer);
            }
        } catch (IOException e) {
            Log.warn("Error handling client: {}", socket.getInetAddress(), e);
        } finally {
            try {
                socket.close();
                Log.info("Client disconnected: {}:{}", socket.getInetAddress(), socket.getPort());
            } catch (IOException e) {
                Log.warn("Error closing client socket: {}", socket.getInetAddress(), e);
            }
        }
    }
}