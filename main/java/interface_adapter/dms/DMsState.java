package interface_adapter.dms;

import entity.Chat;
import entity.Message;

import java.util.List;

/**
 * State class for the DMs view.
 */
public class DMsState {
    private List<Chat> chats;
    private List<Message> messages;
    private Chat currentChat;
    private String currentUsername;
    private String error;

    public DMsState() {
        this.chats = null;
        this.messages = null;
        this.currentChat = null;
        this.currentUsername = null;
        this.error = null;
    }

    public DMsState(DMsState copy) {
        this.chats = copy.chats;
        this.messages = copy.messages;
        this.currentChat = copy.currentChat;
        this.currentUsername = copy.currentUsername;
        this.error = copy.error;
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

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}