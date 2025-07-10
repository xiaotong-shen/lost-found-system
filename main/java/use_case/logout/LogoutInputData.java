package use_case.logout;

/**
 * The Input Data for the Logout Use Case.
 */
public class LogoutInputData {
    private final String username;

    public LogoutInputData(String username) {
        this.username = username;
        // : save the current username in an instance variable and add a getter.
    }

    public String getUsername() {
        return username;
    }

}
