package com.chatbot.model;

import java.util.ArrayList;
import java.util.List;

public class ConversationsWrapper {
    private List<Conversation> conversations;

    public ConversationsWrapper() {
        this.conversations = new ArrayList<>();
    }

    public ConversationsWrapper(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    public List<Conversation> getConversations() {
        return conversations;
    }

    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations != null ? conversations : new ArrayList<>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((conversations == null) ? 0 : conversations.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ConversationsWrapper other = (ConversationsWrapper) obj;
        if (conversations == null) {
            if (other.conversations != null)
                return false;
        } else if (!conversations.equals(other.conversations))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ConversationsWrapper [conversations=" + (conversations != null ? conversations.toString() : "null") + "]";
    }
}