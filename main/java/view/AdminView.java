package view;

import entity.Post;
import interface_adapter.admin.AdminController;
import interface_adapter.admin.AdminState;
import interface_adapter.admin.AdminViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class AdminView extends JPanel implements PropertyChangeListener {
    public final String viewName = "admin";
    private final AdminViewModel adminViewModel;
    private final JPanel postsPanel;
    private final JPanel postDetailPanel;
    private final JButton addPostButton;
    private final JButton logoutButton;
    private AdminController adminController;

    public AdminView(AdminViewModel adminViewModel) {
        this.adminViewModel = adminViewModel;
        this.adminViewModel.addPropertyChangeListener(this);
        setLayout(new BorderLayout());

        // Create top panel with title, add button, and logout button
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addPostButton = new JButton("Add Post");
        logoutButton = new JButton("Logout");
        buttonPanel.add(addPostButton);
        buttonPanel.add(logoutButton);

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Create main content panel with posts list and details
        JPanel contentPanel = new JPanel(new BorderLayout());

        // Posts list on the left
        postsPanel = new JPanel();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(postsPanel);
        scrollPane.setPreferredSize(new Dimension(400, 600));

        // Post details on the right
        postDetailPanel = new JPanel(new BorderLayout());
        postDetailPanel.setBorder(BorderFactory.createTitledBorder("Post Details"));
        postDetailPanel.setPreferredSize(new Dimension(500, 600));

        contentPanel.add(scrollPane, BorderLayout.WEST);
        contentPanel.add(postDetailPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createPostListItem(Post post) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        panel.setBackground(Color.WHITE);

        // Left side: post content
        JPanel postContent = new JPanel();
        postContent.setLayout(new BoxLayout(postContent, BoxLayout.Y_AXIS));
        postContent.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        String contentPreview = post.getDescription();
        if (contentPreview.length() > 100) {
            contentPreview = contentPreview.substring(0, 100) + "...";
        }
        JLabel contentLabel = new JLabel(contentPreview);

        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.add(new JLabel("By: " + post.getAuthor()));
        JLabel typeLabel = new JLabel(post.isLost() ? "LOST" : "FOUND");
        typeLabel.setForeground(post.isLost() ? Color.RED : Color.GREEN);
        detailsPanel.add(typeLabel);
        detailsPanel.add(new JLabel("Posted: " + post.getTimestamp()));

        postContent.add(titleLabel);
        postContent.add(Box.createVerticalStrut(5));
        postContent.add(contentLabel);
        postContent.add(Box.createVerticalStrut(5));
        postContent.add(detailsPanel);

        // Right side: three-dot menu
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        menuPanel.setBackground(Color.WHITE);
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit");
        JMenuItem deleteItem = new JMenuItem("Delete");

        editItem.addActionListener(e -> adminController.editPost(post.getPostID()));
        deleteItem.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this post?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                adminController.deletePost(post.getPostID());
            }
        });

        popupMenu.add(editItem);
        popupMenu.add(deleteItem);

        JButton menuButton = new JButton("⋮");
        menuButton.setFont(new Font("Arial", Font.BOLD, 20));
        menuButton.setBorderPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setFocusPainted(false);
        menuButton.addActionListener(e -> popupMenu.show(menuButton, 0, menuButton.getHeight()));

        menuPanel.add(menuButton);

        // Add components to main panel
        panel.add(postContent, BorderLayout.CENTER);
        panel.add(menuPanel, BorderLayout.EAST);

        // Add click listener for showing details
        postContent.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showPostDetails(post);
            }
        });

        return panel;
    }

    private void showPostDetails(Post post) {
        // Similar to DashboardView's showPostDetails
        // ... (implementation similar to DashboardView)
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            AdminState state = (AdminState) evt.getNewValue();
            updatePostsList(state.getPosts());

            if (state.getError() != null && !state.getError().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        state.getError(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updatePostsList(List<Post> posts) {
        postsPanel.removeAll();

        if (posts != null && !posts.isEmpty()) {
            for (Post post : posts) {
                JPanel postItem = createPostListItem(post);
                postsPanel.add(postItem);
                postsPanel.add(Box.createVerticalStrut(5));
            }
        } else {
            JLabel noPostsLabel = new JLabel("No posts found");
            noPostsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            postsPanel.add(noPostsLabel);
        }

        postsPanel.revalidate();
        postsPanel.repaint();
    }

    public void setAdminController(AdminController controller) {
        this.adminController = controller;
        addPostButton.addActionListener(e -> adminController.addPost());
        logoutButton.addActionListener(e -> adminController.logout());
    }

    public String getViewName() {
        return viewName;
    }
}