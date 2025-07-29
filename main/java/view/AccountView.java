package view;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import interface_adapter.ViewManagerModel;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.LoggedInViewModel;
import interface_adapter.change_password.LoggedInState;
import interface_adapter.logout.LogoutController;
import interface_adapter.change_username.ChangeUsernameController;
import interface_adapter.change_username.ChangeUsernameViewModel;
import interface_adapter.change_username.ChangeUsernameState;

public class AccountView extends JPanel implements PropertyChangeListener {
    private final String viewName = "account";
    private final ViewManagerModel viewManagerModel;

    private final JLabel usernameLabel = new JLabel();
    private final JTextField usernameInputField = new JTextField(15);
    private final JButton changeUsernameButton = new JButton("Change Username");
    private ChangeUsernameController changeUsernameController;
    private ChangeUsernameViewModel changeUsernameViewModel;

    private final JButton changePasswordButton = new JButton("Change Password");
    private final JTextField passwordInputField = new JTextField(15);
    private ChangePasswordController changePasswordController;
    private final JLabel passwordErrorField = new JLabel();

    private final JButton logoutButton = new JButton("Log Out");
    private LogoutController logoutController;
    private LoggedInViewModel loggedInViewModel;

    private final JButton backButton = new JButton("Back");

    public AccountView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Account Page");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(10));

        add(new JLabel("Username:"));
        add(usernameLabel);
        add(Box.createVerticalStrut(10));

        // Change Username UI
        JPanel usernamePanel = new JPanel();
        usernamePanel.add(new JLabel("New Username:"));
        usernamePanel.add(usernameInputField);
        add(usernamePanel);
        add(changeUsernameButton);

        // Change Password UI
        JPanel passwordPanel = new JPanel();
        passwordPanel.add(new JLabel("New Password:"));
        passwordPanel.add(passwordInputField);
        add(passwordPanel);
        add(passwordErrorField);
        add(changePasswordButton);

        add(logoutButton);
        add(backButton);

        changeUsernameButton.addActionListener(e -> {
            if (changeUsernameController != null && loggedInViewModel != null) {
                String oldUsername = loggedInViewModel.getState().getUsername();
                String newUsername = usernameInputField.getText();
                changeUsernameController.execute(oldUsername, newUsername);
            }
        });

        changePasswordButton.addActionListener(e -> {
            if (changePasswordController != null && loggedInViewModel != null) {
                LoggedInState state = loggedInViewModel.getState();
                String username = state.getUsername();
                String password = passwordInputField.getText();
                boolean admin = state.getAdmin();
                changePasswordController.execute(username, password, admin);
            }
        });

        logoutButton.addActionListener(e -> {
            if (logoutController != null && loggedInViewModel != null) {
                LoggedInState state = loggedInViewModel.getState();
                String username = state.getUsername();
                logoutController.execute(username);
            }
        });

        backButton.addActionListener(e -> viewManagerModel.popViewOrClose());
    }

    public void setChangePasswordController(ChangePasswordController controller) {
        this.changePasswordController = controller;
    }
    public void setLogoutController(LogoutController controller) {
        this.logoutController = controller;
    }
    public void setLoggedInViewModel(LoggedInViewModel viewModel) {
        this.loggedInViewModel = viewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);
    }
    public void setChangeUsernameController(ChangeUsernameController controller) {
        this.changeUsernameController = controller;
    }
    public void setChangeUsernameViewModel(ChangeUsernameViewModel viewModel) {
        this.changeUsernameViewModel = viewModel;
        this.changeUsernameViewModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            usernameLabel.setText(state.getUsername());
        } else if (evt.getPropertyName().equals("password")) {
            final LoggedInState state = loggedInViewModel.getState();
            JOptionPane.showMessageDialog(this, "Password updated for " + state.getUsername());
        } else if (evt.getPropertyName().equals("usernameChanged")) {
            ChangeUsernameState state = changeUsernameViewModel.getState();
            JOptionPane.showMessageDialog(this, "Username changed to " + state.getNewUsername());
            usernameInputField.setText("");
        } else if (evt.getPropertyName().equals("usernameChangeError")) {
            ChangeUsernameState state = changeUsernameViewModel.getState();
            JOptionPane.showMessageDialog(this, state.getError(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getViewName() {
        return viewName;
    }
} 