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
    private String chatId; // Link to the chat this message belongs to
    private String sender; // Now a username string instead of User object
    private String content;
    private String sentAt; // Store as string for Firebase compatibility
    private boolean isRead;

    // Firebase requires a no-arg constructor for deserialization
    private Message() {}

    /**
     * Constructs a Message entity.
     * @param messageId Unique identifier for the message
     * @param chatId The ID of the chat this message belongs to
     * @param sender The username of the user who sent the message
     * @param content The message content
     * @param sentAt The date and time the message was sent
     * @param isRead Whether the message has been read
     */
    public Message(String messageId, String chatId, String sender, String content, LocalDateTime sentAt, boolean isRead) {
        this.messageId = messageId;
        this.chatId = chatId;
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

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
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