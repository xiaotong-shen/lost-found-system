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
        System.out.println("\n=== DEBUG: SearchInteractor.execute() called ===");
        System.out.println("DEBUG: Input data:");
        System.out.println("  - Title: '" + searchInputData.getTitle() + "'");
        System.out.println("  - Location: '" + searchInputData.getLocation() + "'");
        System.out.println("  - Tags: " + searchInputData.getTags());
        System.out.println("  - IsLost: " + searchInputData.getIsLost());
        
        try {
            System.out.println("DEBUG: Calling searchDataAccessObject.searchPostsByCriteria()...");
            List<Post> posts = searchDataAccessObject.searchPostsByCriteria(
                searchInputData.getTitle(),
                searchInputData.getLocation(),
                searchInputData.getTags(),
                searchInputData.getIsLost()
            );
            
            System.out.println("DEBUG: searchPostsByCriteria() returned " + posts.size() + " posts");
            
            // Create output data
            SearchOutputData searchOutputData = new SearchOutputData(posts);
            System.out.println("DEBUG: Created SearchOutputData with " + posts.size() + " posts");
            
            // Present results
            if (posts.isEmpty()) {
                System.out.println("DEBUG: No posts found, calling prepareFailView");
                searchOutputBoundary.prepareFailView(new SearchOutputData("No posts found matching your search criteria."));
            } else {
                System.out.println("DEBUG: Posts found, calling prepareSuccessView");
                searchOutputBoundary.prepareSuccessView(searchOutputData);
            }
            
        } catch (Exception e) {
            System.out.println("DEBUG: Exception caught in SearchInteractor:");
            System.out.println("DEBUG: Exception type: " + e.getClass().getSimpleName());
            System.out.println("DEBUG: Exception message: '" + e.getMessage() + "'");
            System.out.println("DEBUG: Exception cause: " + (e.getCause() != null ? e.getCause().getClass().getSimpleName() : "null"));
            System.out.println("DEBUG: Exception stack trace:");
            e.printStackTrace();
            
            String errorMessage = e.getMessage();
            if (errorMessage == null || errorMessage.isEmpty()) {
                errorMessage = "Unknown error occurred during search";
                System.out.println("DEBUG: Using fallback error message: '" + errorMessage + "'");
            }
            searchOutputBoundary.prepareFailView(new SearchOutputData("An error occurred while searching: " + errorMessage));
        }
    }
} 