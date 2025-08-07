package view;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.adminloggedIn.AdminLoggedInState;
import interface_adapter.adminloggedIn.AdminLoggedInViewModel;
import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.LoggedInState;
import interface_adapter.change_password.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.ViewManagerModel;

/**
 * The View for when the user is logged into the program.
 */
public class AdminLoggedInView extends JPanel implements PropertyChangeListener {

    private final String viewName = "admin logged in";
    private final AdminLoggedInViewModel adminloggedInViewModel;
    private final JLabel passwordErrorField = new JLabel();
    private ChangePasswordController changePasswordController;
    private LogoutController logoutController;
    private ViewManagerModel viewManagerModel;

    private final JLabel username;

    private final JButton dashboardButton = new JButton("Dashboard");
    private final JButton searchButton = new JButton("Search");
    private final JButton accountButton = new JButton("Account");
    private final JButton dmsButton = new JButton("DMs");
    private final JButton adminButton = new JButton("AdminDashboard");
    private final JButton deleteUsersButton = new JButton("Delete Users");

    public AdminLoggedInView(AdminLoggedInViewModel adminloggedInViewModel, ViewManagerModel viewManagerModel) {
        this.adminloggedInViewModel = adminloggedInViewModel;
        this.viewManagerModel = viewManagerModel;
        this.adminloggedInViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Logged In Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel usernameInfo = new JLabel("Currently logged in: ");
        username = new JLabel();

        final JPanel buttons = new JPanel();
        buttons.add(dashboardButton);
        buttons.add(searchButton);
        buttons.add(accountButton);
        buttons.add(dmsButton);
        buttons.add(adminButton);
        buttons.add(deleteUsersButton);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        dashboardButton.addActionListener(evt -> {
            if (evt.getSource().equals(dashboardButton)) {
                viewManagerModel.pushView("dashboard");
            }
        });

        searchButton.addActionListener(evt -> {
            if (evt.getSource().equals(searchButton)) {
                viewManagerModel.pushView("advanced_search");
            }
        });

        accountButton.addActionListener(evt -> {
            if (evt.getSource().equals(accountButton)) {
                viewManagerModel.pushView("account");
            }
        });

        dmsButton.addActionListener(evt -> {
            if (evt.getSource().equals(dmsButton)) {
                viewManagerModel.pushView("dms");
            }
        });

        adminButton.addActionListener(evt -> {
            if (evt.getSource().equals(adminButton)) {
                viewManagerModel.pushView("admin");
            }
        });

        deleteUsersButton.addActionListener(evt -> {
            if (evt.getSource().equals(deleteUsersButton)) {
                viewManagerModel.pushView("delete users");
            }
        });

        this.add(title);
        this.add(usernameInfo);
        this.add(username);
        this.add(buttons);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final AdminLoggedInState state = (AdminLoggedInState) evt.getNewValue();
            username.setText(state.getUsername());
        }
        else if (evt.getPropertyName().equals("password")) {
            final AdminLoggedInState state = (AdminLoggedInState) evt.getNewValue();
        }

    }

    public String getViewName() {
        return viewName;
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }
}