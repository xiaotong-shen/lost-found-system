package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Represents a chat between users.
 */
@IgnoreExtraProperties
public class Chat {


    private String chatId;
    private List<User> participants;
    private List<Message> messages;
    private String createdAt; // Store as string for Firebase compatibility

    // Firebase requires a no-arg constructor for deserialization
    public Chat() {
        this.chatId = "";
        this.participants = null;
        this.messages = null;
        this.createdAt = "";
    }

    /**
     * Constructs a Chat entity.
     * @param chatId Unique identifier for the chat
     * @param participants List of users in the chat
     * @param messages List of messages in the chat
     * @param createdAt Creation time of the chat
     */
    public Chat(String chatId, List<User> participants, List<Message> messages, LocalDateTime createdAt) {
        this.chatId = chatId;
        this.participants = participants;
        this.messages = messages;
        this.createdAt = createdAt.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAtString() {
        return createdAt;
    }

    public void setCreatedAtString(String createdAt) {
        this.createdAt = createdAt;
    }
}