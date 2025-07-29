package use_case.login;

import entity.User;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
        System.out.println("DEBUG: LoginInteractor initialized with DAO: " + userDataAccessInterface.getClass().getSimpleName());
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        System.out.println("\n=== DEBUG: LoginInteractor.execute() called ===");
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();
        final boolean admin = loginInputData.getAdmin();
        
        System.out.println("DEBUG: Login attempt:");
        System.out.println("DEBUG:   - Username: '" + username + "'");
        System.out.println("DEBUG:   - Password length: " + password.length());
        System.out.println("DEBUG:   - Password: '" + password + "'");
        
        if (!userDataAccessObject.existsByName(username)) {
            System.out.println("DEBUG: ❌ User does not exist in database");
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        }
        else {
            System.out.println("DEBUG: ✅ User exists, checking password...");
            final String pwd = userDataAccessObject.get(username).getPassword();
            System.out.println("DEBUG: Password comparison:");
            System.out.println("DEBUG:   - Input password: '" + password + "'");
            System.out.println("DEBUG:   - Stored password: '" + pwd + "'");
            System.out.println("DEBUG:   - Passwords match: " + password.equals(pwd));
            
            if (!password.equals(pwd)) {
                System.out.println("DEBUG: ❌ Password mismatch!");
                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }
            else {
                System.out.println("DEBUG: ✅ Login successful!");
                final User user = userDataAccessObject.get(loginInputData.getUsername());

                userDataAccessObject.setCurrentUsername(user.getName());
                final LoginOutputData loginOutputData = new LoginOutputData(user.getName(), false);
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }
}
