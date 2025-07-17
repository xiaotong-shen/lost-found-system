package view;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.search.SearchController;
import interface_adapter.search.SearchState;
import interface_adapter.search.SearchViewModel;

/**
 * The View for the Search functionality.
 */
public class SearchView extends JPanel implements PropertyChangeListener {

    private final String viewName = "search";
    private final SearchViewModel searchViewModel;
    private final JTextField searchInputField = new JTextField(15);
    private final JLabel searchErrorField = new JLabel();
    private final JButton searchButton = new JButton("Search");
    private final JButton backButton = new JButton("Back");
    
    private SearchController searchController;

    public SearchView(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
        this.searchViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Search Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel searchInfo = new LabelTextPanel(
                new JLabel("Search Query"), searchInputField);

        final JPanel buttons = new JPanel();
        buttons.add(searchButton);
        buttons.add(backButton);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add document listener to update state when text changes
        searchInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SearchState currentState = searchViewModel.getState();
                currentState.setSearchQuery(searchInputField.getText());
                searchViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        // Add action listeners
        searchButton.addActionListener(evt -> {
            if (evt.getSource().equals(searchButton)) {
                final SearchState currentState = searchViewModel.getState();
                searchController.execute(currentState.getSearchQuery());
            }
        });

        backButton.addActionListener(evt -> {
            if (evt.getSource().equals(backButton)) {
                searchController.navigateBack();
            }
        });

        // Add components to panel
        this.add(title);
        this.add(searchInfo);
        this.add(searchErrorField);
        this.add(buttons);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final SearchState state = (SearchState) evt.getNewValue();
            searchInputField.setText(state.getSearchQuery());
            searchErrorField.setText(state.getSearchError());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }
} 