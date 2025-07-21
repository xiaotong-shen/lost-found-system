package use_case.dashboard;

import entity.Post;
import java.util.List;

/**
 * Output data for the dashboard use case.
 */
public class DashboardOutputData {
    private final List<Post> posts;
    private final Post selectedPost;
    private final String error;
    private final String successMessage;

    public DashboardOutputData(List<Post> posts) {
        this.posts = posts;
        this.selectedPost = null;
        this.error = null;
        this.successMessage = null;
    }

    public DashboardOutputData(Post selectedPost) {
        this.posts = null;
        this.selectedPost = selectedPost;
        this.error = null;
        this.successMessage = null;
    }

    public DashboardOutputData(String error) {
        this.posts = null;
        this.selectedPost = null;
        this.error = error;
        this.successMessage = null;
    }

    public DashboardOutputData(String successMessage, boolean isSuccess) {
        this.posts = null;
        this.selectedPost = null;
        this.error = null;
        this.successMessage = successMessage;
    }

    // Getters
    public List<Post> getPosts() { return posts; }
    public Post getSelectedPost() { return selectedPost; }
    public String getError() { return error; }
    public String getSuccessMessage() { return successMessage; }
    public boolean hasError() { return error != null; }
    public boolean hasSuccess() { return successMessage != null; }
}