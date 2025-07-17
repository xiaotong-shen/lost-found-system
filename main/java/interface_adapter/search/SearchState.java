package interface_adapter.search;

/**
 * The state for the Search View Model.
 */
public class SearchState {
    private String searchQuery = "";
    private String searchError = "";

    public String getSearchQuery() {
        return searchQuery;
    }

    public String getSearchError() {
        return searchError;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public void setSearchError(String searchError) {
        this.searchError = searchError;
    }
} 