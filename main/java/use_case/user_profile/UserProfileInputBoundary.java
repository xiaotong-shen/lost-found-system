package use_case.user_profile;

/**
 * Input boundary for the user profile use case.
 */
public interface UserProfileInputBoundary {
    /**
     * Loads a user's profile information.
     * @param userProfileInputData the input data containing the username
     */
    void execute(UserProfileInputData userProfileInputData);
}

