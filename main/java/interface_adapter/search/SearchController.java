package interface_adapter.search;

import interface_adapter.ViewManagerModel;
import use_case.search.SearchInputBoundary;
import use_case.search.SearchInputData;

/**
 * The controller for the Search View.
 */
public class SearchController {

    private final SearchInputBoundary searchInteractor;
    private final ViewManagerModel viewManagerModel;

    public SearchController(SearchInputBoundary searchInteractor, ViewManagerModel viewManagerModel) {
        this.searchInteractor = searchInteractor;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Executes advanced search with specific criteria.
     * @param title title to search for
     * @param location location to search for
     * @param tags tags to search for
     * @param isLost whether to search for lost or found items
     */
    public void executeAdvancedSearch(String title, String location, java.util.List<String> tags, Boolean isLost) {
        System.out.println("\n=== DEBUG: SearchController.executeAdvancedSearch() called ===");
        System.out.println("DEBUG: Parameters received:");
        System.out.println("  - Title: '" + title + "'");
        System.out.println("  - Location: '" + location + "'");
        System.out.println("  - Tags: " + tags);
        System.out.println("  - IsLost: " + isLost);
        
        SearchInputData searchInputData = new SearchInputData(title, location, tags, isLost);
        System.out.println("DEBUG: Created SearchInputData with:");
        System.out.println("  - Title: '" + searchInputData.getTitle() + "'");
        System.out.println("  - Location: '" + searchInputData.getLocation() + "'");
        System.out.println("  - Tags: " + searchInputData.getTags());
        System.out.println("  - IsLost: " + searchInputData.getIsLost());
        
        System.out.println("DEBUG: Calling searchInteractor.execute()...");
        searchInteractor.execute(searchInputData);
        System.out.println("DEBUG: searchInteractor.execute() completed");
    }

    /**
     * Navigates back to the previous view.
     */
    public void navigateBack() {
        viewManagerModel.popViewOrClose();
    }
} 