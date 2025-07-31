package use_case.search;

import java.util.List;

/**
 * Input data for the search use case.
 */
public class SearchInputData {
    private final String query;
    private final String title;
    private final String location;
    private final List<String> tags;
    private final Boolean isLost;
    private final boolean isFuzzy;

    // Constructor for basic search (query only)
    public SearchInputData(String query, boolean isFuzzy) {
        this.query = query;
        this.title = null;
        this.location = null;
        this.tags = null;
        this.isLost = null;
        this.isFuzzy = isFuzzy;
    }

    // Constructor for advanced search with multiple fields
    public SearchInputData(String title, String location, List<String> tags, Boolean isLost) {
        this.query = null;
        this.title = title;
        this.location = location;
        this.tags = tags;
        this.isLost = isLost;
        this.isFuzzy = false; // default to false in advanced search
    }

    // Getters
    public String getQuery() { return query; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public List<String> getTags() { return tags; }
    public Boolean getIsLost() { return isLost; }
    public boolean isFuzzy() { return isFuzzy; }
}
