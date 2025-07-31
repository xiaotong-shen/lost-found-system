package interface_adapter.search;

import interface_adapter.ViewManagerModel;
import use_case.search.SearchInputBoundary;
import use_case.search.SearchInputData;

import java.util.List;

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
     * Executes a basic search with query and fuzzy toggle.
     * @param searchQuery the search query entered by the user
     * @param isFuzzy whether to use fuzzy search
     */
    public void execute(String searchQuery, boolean isFuzzy) {
        SearchInputData searchInputData = new SearchInputData(searchQuery, isFuzzy);
        searchInteractor.execute(searchInputData);
    }

    /**
     * Executes advanced search with specific criteria.
     * @param title title to search for
     * @param location location to search for
     * @param tags tags to search for
     * @param isLost whether to search for lost or found items
     * @param isFuzzy whether to enable fuzzy search
     */
    public void executeAdvancedSearch(String title, String location, List<String> tags, Boolean isLost, boolean isFuzzy) {
        System.out.println("\n=== DEBUG: SearchController.executeAdvancedSearch() called ===");
        System.out.println("DEBUG: Parameters received:");
        System.out.println("  - Title: '" + title + "'");
        System.out.println("  - Location: '" + location + "'");
        System.out.println("  - Tags: " + tags);
        System.out.println("  - IsLost: " + isLost);
        System.out.println("  - isFuzzy: " + isFuzzy);

        SearchInputData searchInputData = new SearchInputData(title, location, tags, isLost, isFuzzy);

        System.out.println("DEBUG: Created SearchInputData with:");
        System.out.println("  - Title: '" + searchInputData.getTitle() + "'");
        System.out.println("  - Location: '" + searchInputData.getLocation() + "'");
        System.out.println("  - Tags: " + searchInputData.getTags());
        System.out.println("  - IsLost: " + searchInputData.getIsLost());
        System.out.println("  - isFuzzy: " + searchInputData.isFuzzy());

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
