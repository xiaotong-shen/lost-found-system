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

import interface_adapter.change_password.ChangePasswordController;
import interface_adapter.change_password.LoggedInState;
import interface_adapter.change_password.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.ViewManagerModel;

/**
 * The View for when the user is logged into the program.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
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


    public LoggedInView(LoggedInViewModel loggedInViewModel, ViewManagerModel viewManagerModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

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

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        dashboardButton.addActionListener(evt -> {
            if (evt.getSource().equals(dashboardButton)) {
                viewManagerModel.pushView("dashboard");
            }
        });

        searchButton.addActionListener(evt -> {
            if (evt.getSource().equals(searchButton)) {
                viewManagerModel.pushView("search");
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

        this.add(title);
        this.add(usernameInfo);
        this.add(username);
        this.add(buttons);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            username.setText(state.getUsername());
        }
        else if (evt.getPropertyName().equals("password")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
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
