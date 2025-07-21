package entity;

import java.time.LocalDateTime;

/**
 * Represents a comment on a post.
 */
public class Comment {
    private final int commentID;
    private final String content;
    private final String author;
    private final LocalDateTime timestamp;
    private final int numberOfLikes;

    public Comment(int commentID, String content, String author, LocalDateTime timestamp, int numberOfLikes) {
        this.commentID = commentID;
        this.content = content;
        this.author = author;
        this.timestamp = timestamp;
        this.numberOfLikes = numberOfLikes;
    }

    // Getters
    public int getCommentID() { return commentID; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getNumberOfLikes() { return numberOfLikes; }

    @Override
    public String toString() {
        return String.format("Comment{id=%d, author='%s', content='%s'}", 
                           commentID, author, content.substring(0, Math.min(content.length(), 50)));
    }
} 