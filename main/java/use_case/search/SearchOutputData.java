package use_case.search;

import entity.Post;
import java.util.List;

/**
 * Output data for the search use case.
 */
public class SearchOutputData {
    private final List<Post> posts;
    private final String error;

    public SearchOutputData(List<Post> posts) {
        this.posts = posts;
        this.error = null;
    }

    public SearchOutputData(String error) {
        this.posts = null;
        this.error = error;
    }

    // Getters
    public List<Post> getPosts() { return posts; }
    public String getError() { return error; }
    public boolean hasError() { return error != null; }
} 