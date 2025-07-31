package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a message in a chat.
 */
public class Message {
    private String messageId;
    private User sender;
    private String content;
    private String sentAt; // Store as string for Firebase compatibility
    private boolean isRead;

    // Firebase requires a no-arg constructor for deserialization
    public Message() {
        this.messageId = "";
        this.sender = null;
        this.content = "";
        this.sentAt = "";
        this.isRead = false;
    }

    /**
     * Constructs a Message entity.
     * @param messageId Unique identifier for the message
     * @param sender The user who sent the message
     * @param content The message content
     * @param sentAt The date and time the message was sent
     * @param isRead Whether the message has been read
     */
    public Message(String messageId, User sender, String content, LocalDateTime sentAt, boolean isRead) {
        this.messageId = messageId;
        this.sender = sender;
        this.content = content;
        this.sentAt = sentAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.isRead = isRead;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        if (sentAt == null || sentAt.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(sentAt, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String getSentAtString() {
        return sentAt;
    }

    public void setSentAtString(String sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
} 