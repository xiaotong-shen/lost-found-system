package interface_adapter.search;

import use_case.search.SearchOutputBoundary;
import use_case.search.SearchOutputData;

import java.util.List;

/**
 * Presenter for the Search View.
 * Handles the output from the search use case and updates the view model.
 */
public class SearchPresenter implements SearchOutputBoundary {
    private final SearchViewModel searchViewModel;

    public SearchPresenter(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
    }

    @Override
    public void prepareSuccessView(SearchOutputData searchOutputData) {
        System.out.println("\n=== DEBUG: SearchPresenter.prepareSuccessView() called ===");
        System.out.println("DEBUG: SearchOutputData contains " + (searchOutputData.getPosts() != null ? searchOutputData.getPosts().size() : "null") + " posts");
        
        SearchState currentState = searchViewModel.getState();
        currentState.setSearchResults(searchOutputData.getPosts());
        currentState.setSearchError("");
        currentState.setLoading(false);
        searchViewModel.setState(currentState);
        searchViewModel.firePropertyChanged();
        
        System.out.println("DEBUG: Success view prepared and property change fired");
    }

    @Override
    public void prepareFailView(SearchOutputData searchOutputData) {
        System.out.println("\n=== DEBUG: SearchPresenter.prepareFailView() called ===");
        System.out.println("DEBUG: Error message: '" + searchOutputData.getError() + "'");
        
        SearchState currentState = searchViewModel.getState();
        currentState.setSearchError(searchOutputData.getError());
        currentState.setSearchResults(null);
        currentState.setLoading(false);
        searchViewModel.setState(currentState);
        searchViewModel.firePropertyChanged();
        
        System.out.println("DEBUG: Fail view prepared and property change fired");
    }

    /**
     * Prepares the view for loading state.
     */
    public void prepareLoadingView() {
        System.out.println("\n=== DEBUG: SearchPresenter.prepareLoadingView() called ===");
        SearchState currentState = searchViewModel.getState();
        currentState.setLoading(true);
        currentState.setSearchError("");
        searchViewModel.setState(currentState);
        searchViewModel.firePropertyChanged();
        
        System.out.println("DEBUG: Loading view prepared and property change fired");
    }

    /**
     * Updates the search criteria in the state.
     */
    public void updateSearchCriteria(String title, String location, List<String> tags, Boolean isLost) {
        SearchState currentState = searchViewModel.getState();
        currentState.setTitle(title);
        currentState.setLocation(location);
        currentState.setTags(tags);
        currentState.setIsLost(isLost);
        searchViewModel.setState(currentState);
        searchViewModel.firePropertyChanged();
    }
} 