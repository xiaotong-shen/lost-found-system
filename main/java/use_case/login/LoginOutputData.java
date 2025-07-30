package use_case.login;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String username;
    private final boolean useCaseFailed;
    private final boolean admin;

    public LoginOutputData(String username, boolean useCaseFailed, boolean admin) {
        this.username = username;
        this.useCaseFailed = useCaseFailed;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return admin;
    }

}
