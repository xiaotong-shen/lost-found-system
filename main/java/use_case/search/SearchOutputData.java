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
        System.out.println("DEBUG: SearchOutputData(List<Post> posts) constructor called");
        System.out.println("DEBUG:   - Posts: " + (posts != null ? posts.size() + " posts" : "null"));
        this.posts = posts;
        this.error = null;
        System.out.println("DEBUG: SearchOutputData created with posts successfully");
    }

    public SearchOutputData(String error) {
        System.out.println("DEBUG: SearchOutputData(String error) constructor called");
        System.out.println("DEBUG:   - Error: '" + error + "'");
        this.posts = null;
        this.error = error;
        System.out.println("DEBUG: SearchOutputData created with error successfully");
    }

    // Getters
    public List<Post> getPosts() { 
        System.out.println("DEBUG: getPosts() called, returning: " + (posts != null ? posts.size() + " posts" : "null"));
        return posts; 
    }
    
    public String getError() { 
        System.out.println("DEBUG: getError() called, returning: '" + error + "'");
        return error; 
    }
    
    public boolean hasError() { 
        boolean hasError = error != null;
        System.out.println("DEBUG: hasError() called, returning: " + hasError);
        return hasError; 
    }
} 