package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.google.firebase.database.IgnoreExtraProperties;


/**
 * Represents a message in a chat.
 */
@IgnoreExtraProperties
public class Message {
    private String messageId;
    private User sender;
    private String content;
    private String sentAt; // Store as string for Firebase compatibility
    private boolean isRead;

    // Firebase requires a no-arg constructor for deserialization
    private Message() {}

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

    public String getSentAt() {
        return this.sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
} 