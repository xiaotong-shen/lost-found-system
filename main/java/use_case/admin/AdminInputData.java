package use_case.admin;

import java.util.List;

/**
 * Input data for the admin use case.
 */
public class AdminInputData {
    private final String action; // "load_posts", "search_posts", "add_post"
    private final String searchQuery;
    private final String postId;       // Add this field
    private final String postTitle;
    private final String postContent;
    private final List<String> postTags;
    private final String postLocation;
    private final boolean isLost;
    private final String author; // Add author field

    public AdminInputData(String action) {
        this.action = action;
        this.searchQuery = null;
        this.postId = null;        // Initialize postId
        this.postTitle = null;
        this.postContent = null;
        this.postTags = null;
        this.postLocation = null;
        this.isLost = false;
        this.author = null;
    }

    public AdminInputData(String action, String searchQuery) {
        this.action = action;
        this.searchQuery = searchQuery;
        this.postId = null;        // Initialize postId
        this.postTitle = null;
        this.postContent = null;
        this.postTags = null;
        this.postLocation = null;
        this.isLost = false;
        this.author = null;
    }

    public AdminInputData(String action,String postId, String postTitle, String postContent,
                              List<String> postTags, String postLocation, boolean isLost, String postAuthor) {
        this.action = action;
        this.postId = postId;
        this.searchQuery = null;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postTags = postTags;
        this.postLocation = postLocation;
        this.isLost = isLost;
        this.author = null;
    }

    public AdminInputData(String action, String postTitle, String postContent,
                              List<String> postTags, String postLocation, boolean isLost, String author) {
        this.action = action;
        this.searchQuery = null;
        this.postId = null;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postTags = postTags;
        this.postLocation = postLocation;
        this.isLost = isLost;
        this.author = author;
    }

    public AdminInputData(String action, String postId, boolean isDeleted) {
        this.action = action;
        this.searchQuery = null;
        this.postId = postId;
        this.postTitle = null;
        this.postContent = null;
        this.postTags = null;
        this.postLocation = null;
        this.isLost = false;
        this.author = null;
    }


    // Getters
    public String getAction() { return action; }
    public String getSearchQuery() { return searchQuery; }
    public String getPostTitle() { return postTitle; }
    public String getPostContent() { return postContent; }
    public List<String> getPostTags() { return postTags; }
    public String getPostLocation() { return postLocation; }
    public boolean isLost() { return isLost; }
    public String getAuthor() { return author; }
    public String getPostId() { return postId; }
}
