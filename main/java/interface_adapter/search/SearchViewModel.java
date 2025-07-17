package interface_adapter.search;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the Search View.
 */
public class SearchViewModel extends ViewModel<SearchState> {

    public SearchViewModel() {
        super("search");
        this.setState(new SearchState());
    }

    @Override
    public void firePropertyChanged() {
        super.firePropertyChanged();
    }

    @Override
    public void firePropertyChanged(String propertyName) {
        super.firePropertyChanged(propertyName);
    }
} 