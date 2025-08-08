package entity;

import java.util.List;

/**
 * The representation of a user in our program.
 */
public interface User {

    /**
     * Returns the username of the user.
     * @return the username of the user.
     */
    String getName();

    /**
     * Returns the password of the user.
     * @return the password of the user.
     */
    String getPassword();

    /**
     * Returns if it's an admin
     * @return true if they are admin
     */
    boolean isAdmin();

    /**
     * Returns the credibility score of the user.
     * @return the credibility score.
     */
    int getCredibilityScore();

    /**
     * Returns the list of posts this user has resolved.
     * @return list of resolved post IDs.
     */
    List<String> getResolvedPosts();

    /**
     * Adds a resolved post to the user's list.
     * @param postId the ID of the resolved post.
     */
    void addResolvedPost(String postId);

    /**
     * Increases the user's credibility score.
     * @param points the number of points to add.
     */
    void addCredibilityPoints(int points);
}
