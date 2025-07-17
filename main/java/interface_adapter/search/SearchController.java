package interface_adapter.search;

import interface_adapter.ViewManagerModel;

/**
 * The controller for the Search View.
 */
public class SearchController {

    private final ViewManagerModel viewManagerModel;

    public SearchController(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Executes the search functionality.
     * @param searchQuery the search query entered by the user
     */
    public void execute(String searchQuery) {
        // TODO: Implement search logic
        System.out.println("Searching for: " + searchQuery);
    }

    /**
     * Navigates back to the previous view.
     */
    public void navigateBack() {
        viewManagerModel.popViewOrClose();
    }
} 