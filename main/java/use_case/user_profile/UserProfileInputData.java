package use_case.user_profile;

/**
 * Input data for the user profile use case.
 */
public class UserProfileInputData {
    private final String username;

    public UserProfileInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

