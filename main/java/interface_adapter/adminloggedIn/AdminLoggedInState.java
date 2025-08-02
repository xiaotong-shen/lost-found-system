package interface_adapter.adminloggedIn;

/**
 * The State information representing the logged-in user.
 */
public class AdminLoggedInState {
    private String username = "";

    private String password = "";
    private boolean admin = false;
    private String passwordError;

    public AdminLoggedInState(AdminLoggedInState copy) {
        username = copy.username;
        password = copy.password;
        passwordError = copy.passwordError;
        admin = copy.admin;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public AdminLoggedInState() {

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

}
