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

    public SearchInputData(String query) {
        System.out.println("DEBUG: SearchInputData(String query) constructor called with: '" + query + "'");
        this.query = query;
        this.title = null;
        this.location = null;
        this.tags = null;
        this.isLost = null;
    }

    public SearchInputData(String title, String location, List<String> tags, Boolean isLost) {
        System.out.println("DEBUG: SearchInputData(String title, String location, List<String> tags, Boolean isLost) constructor called");
        System.out.println("DEBUG:   - Title: '" + title + "'");
        System.out.println("DEBUG:   - Location: '" + location + "'");
        System.out.println("DEBUG:   - Tags: " + tags);
        System.out.println("DEBUG:   - IsLost: " + isLost);
        
        // Handle null values gracefully
        this.query = null;
        this.title = title != null ? title : "";
        this.location = location != null ? location : "";
        this.tags = tags; // Keep as null if null, this is valid
        this.isLost = isLost; // Keep as null if null, this is valid
        
        System.out.println("DEBUG: SearchInputData created successfully");
    }

    // Getters
    public String getQuery() { return query; }
    public String getTitle() { return title; }
    public String getLocation() { return location; }
    public List<String> getTags() { return tags; }
    public Boolean getIsLost() { return isLost; }
} 