package interface_adapter.user_profile;

import use_case.resolve_post.ResolvePostInputBoundary;
import use_case.resolve_post.ResolvePostInputData;
import use_case.user_profile.UserProfileInputBoundary;
import use_case.user_profile.UserProfileInputData;

/**
 * Controller for the user profile feature.
 */
public class UserProfileController {
    private final UserProfileInputBoundary userProfileInteractor;
    private final ResolvePostInputBoundary resolvePostInteractor;

    public UserProfileController(UserProfileInputBoundary userProfileInteractor,
                               ResolvePostInputBoundary resolvePostInteractor) {
        this.userProfileInteractor = userProfileInteractor;
        this.resolvePostInteractor = resolvePostInteractor;
    }

    /**
     * Loads a user's profile information.
     * @param username the username to load profile for
     */
    public void loadUserProfile(String username) {
        UserProfileInputData inputData = new UserProfileInputData(username);
        userProfileInteractor.execute(inputData);
    }

    /**
     * Resolves a post and credits a user.
     * @param postId the ID of the post to resolve
     * @param creditedUsername the username to credit
     * @param resolvedByUsername the username of the person resolving the post
     */
    public void resolvePost(String postId, String creditedUsername, String resolvedByUsername) {
        ResolvePostInputData inputData = new ResolvePostInputData(postId, creditedUsername, resolvedByUsername);
        resolvePostInteractor.execute(inputData);
    }

    /**
     * Navigates back to the dashboard.
     */
    public void navigateBack() {
        // This will be handled by the ViewManager
        // The controller just needs to exist for the view to call
    }
}

