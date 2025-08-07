package use_case.dashboard;

import entity.Post;
import java.util.List;

/**
 * Interface for data access operations related to dashboard functionality.
 */
public interface DashboardUserDataAccessInterface {
    /**
     * Gets all posts for the dashboard.
     * @return List of all posts
     */
    List<Post> getAllPosts();

    /**
     * Searches posts by query string.
     * @param query the search query
     * @return List of matching posts
     */
    List<Post> searchPosts(String query);

    /**
     * Performs fuzzy search on posts.
     * @param query the search query
     * @return List of matching posts using fuzzy logic
     */
    List<Post> fuzzySearch(String query);

    /**
     * Gets a specific post by ID.
     * @param postID the ID of the post
     * @return the post, or null if not found
     */
    Post getPostById(int postID);

    /**
     * Adds a new post.
     * @param title the post title
     * @param content the post content
     * @param tags the post tags
     * @param location the post location
     * @param isLost whether it's a lost item
     * @param author the post author
     * @return the created post
     */
    Post addPost(String title, String content, List<String> tags, String location, boolean isLost, String author);
    
    /**
     * Updates an existing post.
     * @param post the post to update
     * @return true if update was successful, false otherwise
     */
    boolean updatePost(Post post);
    
    /**
     * Deletes a post.
     * @param postId the ID of the post to delete
     * @return true if deletion was successful, false otherwise
     */
}