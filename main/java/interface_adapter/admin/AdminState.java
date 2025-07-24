package interface_adapter.admin;

import entity.Post;
import java.util.List;

/**
 * The state for the Dashboard View Model.
 */
public class AdminState {
    private List<Post> posts = null;
    private Post selectedPost = null;
    private String searchQuery = "";
    private String error = "";
    private String successMessage = "";
    private boolean isLoading = false;

    public List<Post> getPosts() { return posts; }
    public Post getSelectedPost() { return selectedPost; }
    public String getSearchQuery() { return searchQuery; }
    public String getError() { return error; }
    public String getSuccessMessage() { return successMessage; }
    public boolean isLoading() { return isLoading; }

    public void setPosts(List<Post> posts) { this.posts = posts; }
    public void setSelectedPost(Post selectedPost) { this.selectedPost = selectedPost; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }
    public void setError(String error) { this.error = error; }
    public void setSuccessMessage(String successMessage) { this.successMessage = successMessage; }
    public void setLoading(boolean loading) { this.isLoading = loading; }
}
