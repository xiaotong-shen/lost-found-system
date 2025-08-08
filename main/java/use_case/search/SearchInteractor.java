package use_case.search;

import entity.Post;
import java.util.List;

/**
 * Interactor for the search use case.
 * Implements the business logic for searching posts.
 */
public class SearchInteractor implements SearchInputBoundary {
    private final SearchUserDataAccessInterface searchDataAccessObject;
    private final SearchOutputBoundary searchOutputBoundary;

    public SearchInteractor(SearchUserDataAccessInterface searchDataAccessObject,
                           SearchOutputBoundary searchOutputBoundary) {
        this.searchDataAccessObject = searchDataAccessObject;
        this.searchOutputBoundary = searchOutputBoundary;
    }

    @Override
    public void execute(SearchInputData searchInputData) {
        try {
            List<Post> posts;
            
            // Check if we're doing a simple query search or criteria search
            if (searchInputData.getQuery() != null && !searchInputData.getQuery().trim().isEmpty()) {
                String query = searchInputData.getQuery().trim();
                
                // Determine whether to use fuzzy search based on the input flag
                if (searchInputData.isFuzzy()) {
                    System.out.println("DEBUG: Executing fuzzy search for query: " + query);
                    posts = searchDataAccessObject.fuzzySearch(query);
                } else {
                    System.out.println("DEBUG: Executing regular search for query: " + query);
                    posts = searchDataAccessObject.searchPosts(query);
                }
            } else {
                // Criteria-based search
                System.out.println("DEBUG: Executing criteria-based search");
                posts = searchDataAccessObject.searchPostsByCriteria(
                    searchInputData.getTitle(),
                    searchInputData.getLocation(),
                    searchInputData.getTags(),
                    searchInputData.getIsLost()
                );
            }

            // Create output data
            SearchOutputData searchOutputData = new SearchOutputData(posts);
            
            // Present results
            if (posts.isEmpty()) {
                String message = searchInputData.isFuzzy() ? 
                    "No posts found matching your fuzzy search criteria." :
                    "No posts found matching your search criteria.";
                searchOutputBoundary.prepareFailView(new SearchOutputData(message));
            } else {
                String resultType = searchInputData.isFuzzy() ? "fuzzy" : "regular";
                System.out.println("DEBUG: Found " + posts.size() + " posts using " + resultType + " search");
                searchOutputBoundary.prepareSuccessView(searchOutputData);
            }
            
        } catch (Exception e) {
            String searchType = searchInputData.isFuzzy() ? "fuzzy search" : "search";
            searchOutputBoundary.prepareFailView(
                new SearchOutputData("An error occurred while performing " + searchType + ": " + e.getMessage())
            );
        }
    }
}