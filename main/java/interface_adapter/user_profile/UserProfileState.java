package interface_adapter.user_profile;

import entity.Post;
import entity.User;

import java.util.List;

/**
 * State for the user profile feature.
 */
public class UserProfileState {
    private User user;
    private List<Post> resolvedPosts;
    private String error = "";
    private String successMessage = "";

    public UserProfileState() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Post> getResolvedPosts() {
        return resolvedPosts;
    }

    public void setResolvedPosts(List<Post> resolvedPosts) {
        this.resolvedPosts = resolvedPosts;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}

