package use_case.admin;

import entity.Post;
import java.util.List;

/**
 * Interface for data access operations related to admin functionality.
 */
public interface AdminUserDataAccessInterface {
    /**
     * Gets all posts for the admin.
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

    boolean editPost(String postId, String title, String description, String location, List<String> tags, boolean isLost);
}