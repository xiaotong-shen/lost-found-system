package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.adminloggedIn.AdminLoggedInState;
import interface_adapter.adminloggedIn.AdminLoggedInViewModel;
import interface_adapter.change_password.LoggedInState;
import interface_adapter.change_password.LoggedInViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;
import view.AdminLoggedInView;
import interface_adapter.adminloggedIn.AdminLoggedInState;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final AdminLoggedInView adminloggedInView;
    private final AdminLoggedInViewModel adminLoggedInViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel, AdminLoggedInView adminloggedInView, AdminLoggedInViewModel adminLoggedInViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.adminloggedInView = adminloggedInView;
        this.adminLoggedInViewModel = adminLoggedInViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // Update the logged in state

        // Direct to different views based on admin status
        if (response.isAdmin()) {
            AdminLoggedInState adminloggedInState = adminLoggedInViewModel.getState();
            adminloggedInState.setUsername(response.getUsername());
            adminloggedInState.setAdmin(response.isAdmin());
            adminLoggedInViewModel.setState(adminloggedInState);
            adminLoggedInViewModel.firePropertyChanged();
            this.viewManagerModel.pushView("admin logged in");  // Use the viewName from AdminLoggedInView
        } else {
            LoggedInState loggedInState = loggedInViewModel.getState();
            loggedInState.setUsername(response.getUsername());
            loggedInState.setAdmin(response.isAdmin());
            loggedInViewModel.setState(loggedInState);
            loggedInViewModel.firePropertyChanged();
            System.out.println(loggedInViewModel.getViewName());
            this.viewManagerModel.pushView(loggedInViewModel.getViewName());
        }
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChanged();
    }
}