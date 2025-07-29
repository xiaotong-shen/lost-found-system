package interface_adapter.search;

import entity.Post;
import java.util.List;

/**
 * The state for the Search View Model.
 */
public class SearchState {
    private String title = "";
    private String location = "";
    private List<String> tags = null;
    private Boolean isLost = null;
    private String searchError = "";
    private List<Post> searchResults = null;
    private boolean isLoading = false;

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public List<String> getTags() {
        return tags;
    }

    public Boolean getIsLost() {
        return isLost;
    }

    public String getSearchError() {
        return searchError;
    }

    public List<Post> getSearchResults() {
        return searchResults;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setIsLost(Boolean isLost) {
        this.isLost = isLost;
    }

    public void setSearchError(String searchError) {
        this.searchError = searchError;
    }

    public void setSearchResults(List<Post> searchResults) {
        this.searchResults = searchResults;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }
} 