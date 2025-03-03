package com.chatbot.handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.chatbot.utils.Constants;
import com.chatbot.utils.Log;

/**
 * Handles the Medical Chatbot Server operations, including socket communication.
 */
public class SocketHandler {
    private ServerSocket serverSocket;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public SocketHandler() {
        try {
            serverSocket = new ServerSocket(Constants.SERVER_PORT);
            // Add shutdown hook for graceful termination
            Runtime.getRuntime().addShutdownHook(new Thread(this::stopServer));
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
        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                Log.info("New client connected: {}", clientSocket.getInetAddress());
                handleClient(clientSocket);
            } catch (IOException e) {
                Log.error("Error accepting client connection: {}", e.getMessage());
                // If the socket was closed intentionally (during shutdown), break the loop
                if (serverSocket.isClosed()) {
                    break;
                }
            }
        }
    }

    /**
     * Handles a new client connection in a separate thread.
     */
    private void handleClient(Socket clientSocket) {
        threadPool.execute(new ClientHandler(clientSocket));
    }

    /**
     * Cleans up resources when shutting down the server
     */
    private void cleanup() {
        if (threadPool != null) {
            threadPool.shutdown();
            try {
                // Wait a while for existing tasks to terminate
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                    if (!threadPool.awaitTermination(60, TimeUnit.SECONDS))
                        Log.error("Thread pool did not terminate");
                }
            } catch (InterruptedException e) {
                // (Re-)Cancel if current thread also interrupted
                threadPool.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }
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
            // Call the cleanup method to handle thread pool shutdown
            cleanup();
        } catch (IOException e) {
            Log.error("Error closing server: {}", e.getMessage());
        }
    }
}