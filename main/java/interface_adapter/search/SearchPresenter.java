package interface_adapter.search;

import use_case.search.SearchOutputBoundary;
import use_case.search.SearchOutputData;

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
        SearchState currentState = searchViewModel.getState();
        currentState.setSearchResults(searchOutputData.getPosts());
        currentState.setSearchError("");
        currentState.setLoading(false);
        searchViewModel.setState(currentState);
        searchViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(SearchOutputData searchOutputData) {
        SearchState currentState = searchViewModel.getState();
        currentState.setSearchError(searchOutputData.getError());
        currentState.setSearchResults(null);
        currentState.setLoading(false);
        searchViewModel.setState(currentState);
        searchViewModel.firePropertyChanged();
    }

    /**
     * Prepares the view for loading state.
     */
    public void prepareLoadingView() {
        SearchState currentState = searchViewModel.getState();
        currentState.setLoading(true);
        currentState.setSearchError("");
        searchViewModel.setState(currentState);
        searchViewModel.firePropertyChanged();
    }
} 