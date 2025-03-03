package com.chatbot.handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.chatbot.utils.Constants;
import com.chatbot.utils.Log;

/**
 * Handles the Medical Chatbot Server operations, including socket communication.
 */
public class SocketHandler {
    private ServerSocket serverSocket;

    public SocketHandler() {
        try {
            serverSocket = new ServerSocket(Constants.SERVER_PORT);
        } catch (IOException e) {
            Log.error("Failed to start server on port {}: {}", Constants.SERVER_PORT, e.getMessage());
            System.exit(1); // Exit if the server cannot start
        }
    }

    /**
     * Starts the Medical Chatbot Server and listens for client connections.
     */
    public void startServer() {
        try {
            Log.info("Medical Chatbot Server is running on port {}...", Constants.SERVER_PORT);
            acceptClients();
        } catch (Exception e) {
            Log.error("Server encountered an error: {}", e.getMessage());
        } finally {
            stopServer();
        }
    }

    /**
     * Accepts client connections in a loop.
     */
    private void acceptClients() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                Log.info("New client connected: {}", clientSocket.getInetAddress());
                handleClient(clientSocket);
            } catch (IOException e) {
                Log.error("Error accepting client connection: {}", e.getMessage());
            }
        }
    }

    /**
     * Handles a new client connection in a separate thread.
     */
    private void handleClient(Socket clientSocket) {
        new Thread(new ClientHandler(clientSocket)).start();
    }

    /**
     * Stops the server gracefully.
     */
    private void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                Log.info("Server stopped.");
            }
        } catch (IOException e) {
            Log.error("Error closing server: {}", e.getMessage());
        }
    }
}