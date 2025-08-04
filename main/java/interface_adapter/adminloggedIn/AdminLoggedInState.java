package interface_adapter.adminloggedIn;

import java.util.ArrayList;
import java.util.List;

/**
 * The State information representing the logged-in user.
 */
public class AdminLoggedInState {
    private String username = "";

    private String password = "";
    private boolean admin = false;
    private String passwordError;
    private String deleteUserMessage = "";
    private String deleteUserError = "";
    private List<String> usersList = new ArrayList<>();


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

    public String getDeleteUserMessage() {
        return deleteUserMessage;
    }

    public void setDeleteUserMessage(String deleteUserMessage) {
        this.deleteUserMessage = deleteUserMessage;
    }

    public String getDeleteUserError() {
        return deleteUserError;
    }

    public void setDeleteUserError(String deleteUserError) {
        this.deleteUserError = deleteUserError;
    }

    public List<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<String> usersList) {
        this.usersList = usersList;
    }


}
