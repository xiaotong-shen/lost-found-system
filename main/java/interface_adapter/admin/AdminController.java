package interface_adapter.admin;

import interface_adapter.ViewManagerModel;
import use_case.admin.AdminInputBoundary;
import use_case.admin.AdminInputData;
import use_case.admin.AdminInteractor;
import use_case.admin.AdminOutputData;

import java.util.List;

/**
 * The controller for the Dashboard View.
 */
public class AdminController {

    private final AdminInputBoundary adminInteractor;
    private final ViewManagerModel viewManagerModel;
    private String currentUser; // Add current user tracking

    public AdminController(AdminInputBoundary adminInteractor, ViewManagerModel viewManagerModel) {
        this.adminInteractor = adminInteractor;
        this.viewManagerModel = viewManagerModel;
        this.currentUser = "anonymous"; // Default user
    }

    /**
     * Sets the current logged-in user.
     * @param username the username of the current user
     */
    public void setCurrentUser(String username) {
        this.currentUser = username;
    }

    /**
     * Loads all posts for the dashboard.
     */
    public void loadPosts() {
        AdminInputData adminInputData = new AdminOutputData("load_posts");
        AdminInteractor.execute(adminInputData);
    }

    /**
     * Searches posts by query.
     * @param searchQuery the search query
     */
    public void searchPosts(String searchQuery) {
        AdminInputData adminInputData = new AdminInputData("search_posts", searchQuery);
        adminInteractor.execute(adminInputData);
    }

    /**
     * Adds a new post.
     * @param title the post title
     * @param content the post content
     * @param tags the post tags
     * @param location the post location
     * @param isLost whether it's a lost item
     */
    public void addPost(String title, String content, List<String> tags, String location, boolean isLost) {
        AdminInputData adminInputData = new AdminInputData("add_post", title, content, tags, location, isLost, currentUser);
        adminInteractor.execute(adminInputData);
    }

    /**
     * Navigates back to the previous view.
     */
    public void navigateBack() {
        viewManagerModel.popViewOrClose();
    }
}
