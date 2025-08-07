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
                // Simple query search
                posts = searchDataAccessObject.searchPosts(searchInputData.getQuery().trim());
            } else {
                // Criteria-based search
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
                searchOutputBoundary.prepareFailView(new SearchOutputData("No posts found matching your search criteria."));
            } else {
                searchOutputBoundary.prepareSuccessView(searchOutputData);
            }
            
        } catch (Exception e) {
            searchOutputBoundary.prepareFailView(new SearchOutputData("An error occurred while searching: " + e.getMessage()));
        }
    }
} 