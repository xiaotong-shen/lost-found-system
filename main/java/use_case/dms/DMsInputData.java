package use_case.dms;

import entity.User;
import java.util.List;

/**
 * Data class for chat use case input operations.
 */
public class DMsInputData {
    private String username;
    private String chatId;
    private String messageContent;
    private List<User> participants;

    /**
     * Constructor for loading chats.
     * @param username the username to load chats for
     */
    public DMsInputData(String username) {
        this.username = username;
    }

    /**
     * Constructor for creating a chat.
     * @param participants the list of participants for the new chat
     */
    public DMsInputData(List<User> participants) {
        this.participants = participants;
    }

    /**
     * Constructor for sending a message.
     * @param chatId the ID of the chat to send the message to
     * @param username the username of the sender
     * @param messageContent the content of the message
     */
    public DMsInputData(String chatId, String username, String messageContent) {
        this.chatId = chatId;
        this.username = username;
        this.messageContent = messageContent;
    }

    /**
     * Constructor for loading messages.
     * @param chatId the ID of the chat to load messages for
     */
    public DMsInputData(String chatId, String username) {
        this.chatId = chatId;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
}