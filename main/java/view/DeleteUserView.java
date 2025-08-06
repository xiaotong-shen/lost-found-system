package view;

import interface_adapter.delete_user.DeleteUserController;
import interface_adapter.delete_user.DeleteUserViewModel;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class DeleteUserView extends JPanel implements ActionListener, PropertyChangeListener {
    public final String viewName = "delete users";
    private final DeleteUserViewModel deleteUserViewModel;
    private DeleteUserController deleteUserController;
    private final ViewManagerModel viewManagerModel;
    
    private final JPanel usersPanel;
    private final JButton backButton;
    private final JLabel titleLabel;

    public DeleteUserView(DeleteUserViewModel deleteUserViewModel,
                         DeleteUserController deleteUserController,
                         ViewManagerModel viewManagerModel) {
        System.out.println("DEBUG: DeleteUserView constructor called");
        this.deleteUserViewModel = deleteUserViewModel;
        this.deleteUserController = deleteUserController;
        this.viewManagerModel = viewManagerModel;
        this.deleteUserViewModel.addPropertyChangeListener(this);

        // Set up the main layout
        setLayout(new BorderLayout());

        // Create title
        titleLabel = new JLabel("All Users", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Create users panel with scroll
        usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(usersPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        // Create back button
        backButton = new JButton("Back");
        backButton.addActionListener(evt -> {
            if (evt.getSource().equals(backButton)) {
                viewManagerModel.pushView("admin logged in");
            }
        });


        // Add components to the panel
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);

        // Initial update of users list
        updateUsersList(deleteUserViewModel.getState().getUsersList());

        // Add a component listener to load users when the view becomes visible
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                System.out.println("DEBUG: DeleteUserView became visible");
                if (deleteUserController != null) {
                    System.out.println("DEBUG: Calling loadUsers from componentShown");
                    deleteUserController.loadUsers();
                } else {
                    System.out.println("DEBUG: deleteUserController is null in componentShown");
                }
            }
        });

        System.out.println("DEBUG: DeleteUserView constructor completed");
    }

    private void updateUsersList(List<String> users) {
        System.out.println("DEBUG: updateUsersList called with users: " + (users != null ? users.size() : "null"));
        System.out.println("DEBUG: Current thread: " + Thread.currentThread().getName()); // Add thread info
        usersPanel.removeAll();

        if (users != null && !users.isEmpty()) {
            System.out.println("DEBUG: Users to display: " + users);
            System.out.println("DEBUG: First user in list: " + users.get(0)); // Show first user for verification
            for (String username : users) {
                JPanel userPanel = createUserPanel(username);
                usersPanel.add(userPanel);
                usersPanel.add(Box.createVerticalStrut(5));
                System.out.println("DEBUG: Added user panel for: " + username);
            }
        } else {
            System.out.println("DEBUG: No users to display. Users list is " + (users == null ? "null" : "empty"));
            JLabel noUsersLabel = new JLabel("No users found", SwingConstants.CENTER);
            noUsersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            usersPanel.add(noUsersLabel);
        }

        System.out.println("DEBUG: Finished updating users list panel");
        usersPanel.revalidate();
        usersPanel.repaint();
    }

    private JPanel createUserPanel(String username) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Username label
        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        // Delete button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand("delete-" + username);
        deleteButton.addActionListener(this);

        // Add components with spacing
        panel.add(usernameLabel);
        panel.add(Box.createHorizontalGlue());
        panel.add(deleteButton);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.startsWith("delete-")) {
            String username = command.substring(7);
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete user: " + username + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                deleteUserController.execute(username);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("DEBUG: PropertyChange event received: " + evt.getPropertyName());
        if (evt.getPropertyName().equals("state")) {
            List<String> usersList = deleteUserViewModel.getState().getUsersList();
            System.out.println("DEBUG: PropertyChange updating users list: " + usersList);
            updateUsersList(usersList);
            
            String errorMessage = deleteUserViewModel.getState().getError();
            if (errorMessage != null && !errorMessage.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        errorMessage,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                deleteUserViewModel.getState().setError("");
            }

            String successMessage = deleteUserViewModel.getState().getSuccessMessage();
            if (successMessage != null && !successMessage.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        successMessage,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                deleteUserViewModel.getState().setSuccessMessage("");
            }
        }
    }

    public void setDeleteUserController(DeleteUserController deleteUserController) {
        System.out.println("DEBUG: Setting deleteUserController: " + (deleteUserController != null));
        this.deleteUserController = deleteUserController;
    }

    public String getViewName() {
        return viewName;
    }
}