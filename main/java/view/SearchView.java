package view;

import entity.Post;
import interface_adapter.search.SearchController;
import interface_adapter.search.SearchState;
import interface_adapter.search.SearchViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// SESSION CHANGE: Author label now shows just the username in search results. See also: FirebasePostDataAccessObject, DashboardInteractor, DashboardView, LoginPresenter, SignupPresenter, LoggedInView, AppBuilder.

/**
 * The View for the Search functionality.
 */
public class SearchView extends JPanel implements PropertyChangeListener {

    private final String viewName = "search";
    private final SearchViewModel searchViewModel;
    
    // Search input fields
    private final JTextField titleField = new JTextField(15);
    private final JTextField locationField = new JTextField(15);
    private final JTextField tagsField = new JTextField(15);
    private final JComboBox<String> isLostComboBox = new JComboBox<>(new String[]{"All", "Lost", "Found"});
    
    private final JLabel searchErrorField = new JLabel();
    private final JButton searchButton = new JButton("Search");
    private final JButton clearButton = new JButton("Clear");
    private final JButton backButton = new JButton("Back");
    private final JPanel resultsPanel = new JPanel();
    private final JScrollPane scrollPane;
    
    private SearchController searchController;

    public SearchView(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
        this.searchViewModel.addPropertyChangeListener(this);

        // Set up the main layout
        this.setLayout(new BorderLayout());

        // Create top panel for search input
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("Search Lost & Found Posts");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        // Create search criteria panels
        final LabelTextPanel titleInfo = new LabelTextPanel(
                new JLabel("Title"), titleField);
        final LabelTextPanel locationInfo = new LabelTextPanel(
                new JLabel("Location"), locationField);
        final LabelTextPanel tagsInfo = new LabelTextPanel(
                new JLabel("Tags (comma-separated)"), tagsField);
        
        // Create lost/found selection panel
        JPanel lostFoundPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lostFoundPanel.add(new JLabel("Type: "));
        lostFoundPanel.add(isLostComboBox);

        final JPanel buttons = new JPanel();
        buttons.add(searchButton);
        buttons.add(clearButton);
        buttons.add(backButton);

        topPanel.add(title);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(titleInfo);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(locationInfo);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(tagsInfo);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(lostFoundPanel);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(searchErrorField);
        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(buttons);

        // Set up results panel
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Add components to main panel
        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        // Add document listeners to update state when text changes
        titleField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void documentListenerHelper() {
                final SearchState currentState = searchViewModel.getState();
                currentState.setTitle(titleField.getText());
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

        locationField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void documentListenerHelper() {
                final SearchState currentState = searchViewModel.getState();
                currentState.setLocation(locationField.getText());
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

        tagsField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void documentListenerHelper() {
                final SearchState currentState = searchViewModel.getState();
                String tagsText = tagsField.getText().trim();
                if (!tagsText.isEmpty()) {
                    String[] tagsArray = tagsText.split(",");
                    List<String> tagsList = new ArrayList<>();
                    for (String tag : tagsArray) {
                        tagsList.add(tag.trim());
                    }
                    currentState.setTags(tagsList);
                } else {
                    currentState.setTags(null);
                }
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

        // Add action listeners
        searchButton.addActionListener(evt -> {
            if (evt.getSource().equals(searchButton)) {
                final SearchState currentState = searchViewModel.getState();
                
                // Get the isLost value from combo box
                String selectedType = (String) isLostComboBox.getSelectedItem();
                Boolean isLost = null;
                if ("Lost".equals(selectedType)) {
                    isLost = true;
                } else if ("Found".equals(selectedType)) {
                    isLost = false;
                }
                currentState.setIsLost(isLost);
                
                // Execute advanced search with all criteria
                searchController.executeAdvancedSearch(
                    currentState.getTitle(),
                    currentState.getLocation(),
                    currentState.getTags(),
                    currentState.getIsLost()
                );
            }
        });

        clearButton.addActionListener(evt -> {
            if (evt.getSource().equals(clearButton)) {
                titleField.setText("");
                locationField.setText("");
                tagsField.setText("");
                isLostComboBox.setSelectedItem("All");
                
                final SearchState currentState = searchViewModel.getState();
                currentState.setTitle("");
                currentState.setLocation("");
                currentState.setTags(null);
                currentState.setIsLost(null);
                searchViewModel.setState(currentState);
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
            
            // Update input fields with current state
            titleField.setText(state.getTitle());
            locationField.setText(state.getLocation());
            
            if (state.getTags() != null) {
                tagsField.setText(String.join(", ", state.getTags()));
            } else {
                tagsField.setText("");
            }
            
            if (state.getIsLost() == null) {
                isLostComboBox.setSelectedItem("All");
            } else if (state.getIsLost()) {
                isLostComboBox.setSelectedItem("Lost");
            } else {
                isLostComboBox.setSelectedItem("Found");
            }
            
            searchErrorField.setText(state.getSearchError());
            
            // Update results display
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

        // Title
        JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Description
        JLabel descLabel = new JLabel(post.getDescription());
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        descLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        // Details
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        detailsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsPanel.setBackground(Color.WHITE);
        
        // SESSION CHANGE: Author label now shows just the username
        JLabel authorLabel = new JLabel(post.getAuthor());
        JLabel locationLabel = new JLabel("Location: " + post.getLocation());
        JLabel typeLabel = new JLabel(post.isLost() ? "LOST" : "FOUND");
        typeLabel.setForeground(post.isLost() ? Color.RED : Color.GREEN);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JLabel timeLabel = new JLabel("Posted: " + 
            post.getTimestamp());

        detailsPanel.add(authorLabel);
        detailsPanel.add(locationLabel);
        detailsPanel.add(typeLabel);
        detailsPanel.add(timeLabel);

        // Tags
        List<String> tags = post.getTags();
        String tagsText = "Tags: ";
        if (tags != null && !tags.isEmpty()) {
            tagsText += String.join(", ", tags);
        } else {
            tagsText += "No tags";
        }
        JLabel tagsLabel = new JLabel(tagsText);
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