package use_case.dms;

import entity.Chat;
import entity.Message;

import java.util.List;

/**
 * Data class for chat use case output operations.
 */
public class DMsOutputData {
    private List<Chat> chats;
    private List<Message> messages;
    private Chat currentChat;
    private String error;

    public DMsOutputData(List<Chat> chats, List<Message> messages, Chat currentChat, String error) {
        this.chats = chats;
        this.messages = messages;
        this.currentChat = currentChat;
        this.error = error;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Chat getCurrentChat() {
        return currentChat;
    }

    public void setCurrentChat(Chat currentChat) {
        this.currentChat = currentChat;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}