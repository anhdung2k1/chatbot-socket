package com.chatbot;

import javax.swing.SwingUtilities;

import com.chatbot.ui.SignInPage;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignInPage::new);
    }
}