package view;

import entity.Post;
import entity.User;
import interface_adapter.user_profile.UserProfileController;
import interface_adapter.user_profile.UserProfileState;
import interface_adapter.user_profile.UserProfileViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * View for displaying user profiles with credibility information.
 */
public class UserProfileView extends JPanel implements PropertyChangeListener {
    private final String viewName = "user_profile";
    private final UserProfileViewModel userProfileViewModel;
    private final UserProfileController userProfileController;

    private JLabel usernameLabel;
    private JLabel credibilityScoreLabel;
    private JLabel resolvedPostsCountLabel;
    private JPanel resolvedPostsPanel;
    private JScrollPane resolvedPostsScrollPane;
    private JButton backButton;

    public UserProfileView(UserProfileViewModel userProfileViewModel, UserProfileController userProfileController) {
        this.userProfileViewModel = userProfileViewModel;
        this.userProfileController = userProfileController;
        this.userProfileViewModel.addPropertyChangeListener(this);

        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        // Username display
        usernameLabel = new JLabel();
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        usernameLabel.setForeground(new Color(33, 37, 41));

        // Credibility score
        credibilityScoreLabel = new JLabel();
        credibilityScoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        credibilityScoreLabel.setForeground(new Color(0, 123, 255));

        // Resolved posts count
        resolvedPostsCountLabel = new JLabel();
        resolvedPostsCountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resolvedPostsCountLabel.setForeground(new Color(108, 117, 125));

        // Resolved posts panel
        resolvedPostsPanel = new JPanel();
        resolvedPostsPanel.setLayout(new BoxLayout(resolvedPostsPanel, BoxLayout.Y_AXIS));
        resolvedPostsPanel.setBackground(Color.WHITE);
        resolvedPostsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        resolvedPostsScrollPane = new JScrollPane(resolvedPostsPanel);
        resolvedPostsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        resolvedPostsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resolvedPostsScrollPane.setPreferredSize(new Dimension(600, 400));
        resolvedPostsScrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230), 1));

        // Back button
        backButton = new JButton("← Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> userProfileController.navigateBack());
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(222, 226, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);

        userInfoPanel.add(usernameLabel);
        userInfoPanel.add(Box.createVerticalStrut(10));
        userInfoPanel.add(credibilityScoreLabel);
        userInfoPanel.add(Box.createVerticalStrut(5));
        userInfoPanel.add(resolvedPostsCountLabel);

        headerPanel.add(userInfoPanel, BorderLayout.CENTER);
        headerPanel.add(backButton, BorderLayout.EAST);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(248, 249, 250));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel resolvedPostsTitle = new JLabel("Resolved Posts");
        resolvedPostsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        resolvedPostsTitle.setForeground(new Color(33, 37, 41));

        contentPanel.add(resolvedPostsTitle, BorderLayout.NORTH);
        contentPanel.add(resolvedPostsScrollPane, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            UserProfileState state = (UserProfileState) evt.getNewValue();
            updateView(state);
        }
    }

    private void updateView(UserProfileState state) {
        if (state.getUser() != null) {
            User user = state.getUser();
            
            // Update user information
            usernameLabel.setText("👤 " + user.getName());
            credibilityScoreLabel.setText("🏆 Credibility Score: " + user.getCredibilityScore() + " points");
            resolvedPostsCountLabel.setText("✅ Resolved Posts: " + user.getResolvedPosts().size());

            // Update resolved posts
            updateResolvedPosts(state.getResolvedPosts());
        }

        // Show error messages
        if (!state.getError().isEmpty()) {
            JOptionPane.showMessageDialog(this, state.getError(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Show success messages
        if (!state.getSuccessMessage().isEmpty()) {
            JOptionPane.showMessageDialog(this, state.getSuccessMessage(), "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateResolvedPosts(List<Post> resolvedPosts) {
        resolvedPostsPanel.removeAll();

        if (resolvedPosts != null && !resolvedPosts.isEmpty()) {
            for (Post post : resolvedPosts) {
                JPanel postPanel = createResolvedPostPanel(post);
                resolvedPostsPanel.add(postPanel);
                resolvedPostsPanel.add(Box.createVerticalStrut(8));
            }
        } else {
            JLabel noPostsLabel = new JLabel("No resolved posts yet.");
            noPostsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            noPostsLabel.setForeground(Color.BLACK);
            noPostsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            resolvedPostsPanel.add(noPostsLabel);
        }

        resolvedPostsPanel.revalidate();
        resolvedPostsPanel.repaint();
    }

    private JPanel createResolvedPostPanel(Post post) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Title
        JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLACK);

        // Resolution info
        JLabel resolutionLabel = new JLabel("Resolved by: " + post.getResolvedBy());
        resolutionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resolutionLabel.setForeground(new Color(40, 167, 69));

        // Type badge
        JLabel typeLabel = new JLabel(post.isLost() ? "LOST" : "FOUND");
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        typeLabel.setForeground(post.isLost() ? new Color(220, 53, 69) : new Color(40, 167, 69));
        typeLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));

        // Top row: title and type
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(titleLabel, BorderLayout.WEST);
        topRow.add(typeLabel, BorderLayout.EAST);

        // Bottom row: resolution info
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);
        bottomRow.add(resolutionLabel, BorderLayout.WEST);

        panel.add(topRow, BorderLayout.NORTH);
        panel.add(bottomRow, BorderLayout.SOUTH);

        return panel;
    }

    public String getViewName() {
        return viewName;
    }
}

