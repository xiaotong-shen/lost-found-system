package view;

import entity.Post;
import interface_adapter.search.SearchController;
import interface_adapter.search.SearchState;
import interface_adapter.search.SearchViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * The View for the Search functionality.
 */
public class SearchView extends JPanel implements PropertyChangeListener {

    private final String viewName = "search";
    private final SearchViewModel searchViewModel;
    private final JTextField searchInputField = new JTextField(15);
    private final JCheckBox fuzzyCheckbox = new JCheckBox("Fuzzy Search");
    private final JLabel searchErrorField = new JLabel();
    private final JButton searchButton = new JButton("Search");
    private final JButton backButton = new JButton("Back");
    private final JPanel resultsPanel = new JPanel();
    private final JScrollPane scrollPane;

    private SearchController searchController;

    public SearchView(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
        this.searchViewModel.addPropertyChangeListener(this);
        this.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("Search Lost & Found Posts");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        final LabelTextPanel searchInfo = new LabelTextPanel(
                new JLabel("Search Query"), searchInputField);

        final JPanel buttons = new JPanel();
        buttons.add(searchButton);
        buttons.add(backButton);

        fuzzyCheckbox.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(title);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(searchInfo);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(fuzzyCheckbox);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(searchErrorField);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(buttons);

        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        searchInputField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void documentListenerHelper() {
                final SearchState currentState = searchViewModel.getState();
                currentState.setSearchQuery(searchInputField.getText());
                searchViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                documentListenerHelper();
            }
        });

        searchButton.addActionListener(evt -> {
            if (evt.getSource().equals(searchButton)) {
                final SearchState currentState = searchViewModel.getState();
                boolean isFuzzy = fuzzyCheckbox.isSelected();
                searchController.execute(currentState.getSearchQuery(), isFuzzy);
            }
        });

        backButton.addActionListener(evt -> {
            if (evt.getSource().equals(backButton)) {
                searchController.navigateBack();
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final SearchState state = (SearchState) evt.getNewValue();
            searchInputField.setText(state.getSearchQuery());
            searchErrorField.setText(state.getSearchError());
            updateResultsDisplay(state.getSearchResults(), state.isLoading());
        }
    }

    private void updateResultsDisplay(List<Post> posts, boolean isLoading) {
        resultsPanel.removeAll();

        if (isLoading) {
            JLabel loadingLabel = new JLabel("Searching...");
            loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(loadingLabel);
        } else if (posts != null && !posts.isEmpty()) {
            JLabel resultsLabel = new JLabel("Search Results (" + posts.size() + " posts found):");
            resultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsLabel.setFont(new Font("Arial", Font.BOLD, 14));
            resultsPanel.add(resultsLabel);
            resultsPanel.add(Box.createVerticalStrut(10));

            for (Post post : posts) {
                resultsPanel.add(createPostPanel(post));
                resultsPanel.add(Box.createVerticalStrut(10));
            }
        } else if (posts != null && posts.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No posts found matching your search criteria.");
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resultsPanel.add(noResultsLabel);
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel createPostPanel(Post post) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel(post.getDescription());
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.setBackground(Color.WHITE);

        JLabel authorLabel = new JLabel("By: " + post.getAuthor());
        JLabel locationLabel = new JLabel("Location: " + post.getLocation());
        JLabel typeLabel = new JLabel(post.isLost() ? "LOST" : "FOUND");
        typeLabel.setForeground(post.isLost() ? Color.RED : Color.GREEN);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel timeLabel = new JLabel("Posted: " + post.getTimestamp());

        detailsPanel.add(authorLabel);
        detailsPanel.add(locationLabel);
        detailsPanel.add(typeLabel);
        detailsPanel.add(timeLabel);

        JLabel tagsLabel = new JLabel("Tags: " + String.join(", ", post.getTags()));
        tagsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tagsLabel.setForeground(Color.BLUE);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(descLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(detailsPanel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(tagsLabel);

        return panel;
    }

    public String getViewName() {
        return viewName;
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }
}
