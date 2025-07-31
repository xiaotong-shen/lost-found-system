package use_case.admin;

import entity.Post;
import java.util.List;

/**
 * Interactor for the admin use case.
 * Implements the business logic for admin operations.
 */
public class AdminInteractor implements AdminInputBoundary {
    private final AdminUserDataAccessInterface adminDataAccessObject;
    private final AdminOutputBoundary adminOutputBoundary;

    public AdminInteractor(AdminUserDataAccessInterface adminDataAccessObject,
                               AdminOutputBoundary adminOutputBoundary) {
        this.adminDataAccessObject = adminDataAccessObject;
        this.adminOutputBoundary = adminOutputBoundary;
    }

    @Override
    public void execute(AdminInputData adminInputData) {
        try {
            switch (adminInputData.getAction()) {
                case "load_posts":
                    List<Post> posts = adminDataAccessObject.getAllPosts();
                    AdminOutputData outputData = new AdminOutputData(posts);
                    adminOutputBoundary.prepareSuccessView(outputData);
                    break;

                case "search_posts":
                    if (adminInputData.getSearchQuery() != null && !adminInputData.getSearchQuery().trim().isEmpty()) {
                        List<Post> searchResults = adminDataAccessObject.searchPosts(adminInputData.getSearchQuery().trim());
                        AdminOutputData searchOutputData = new AdminOutputData(searchResults);
                        adminOutputBoundary.prepareSuccessView(searchOutputData);
                    } else {
                        // If search query is blank, return all posts sorted alphabetically by title
                        List<Post> allPosts = adminDataAccessObject.getAllPosts();
                        allPosts.sort(java.util.Comparator.comparing(Post::getTitle, String.CASE_INSENSITIVE_ORDER));
                        AdminOutputData allPostsOutputData = new AdminOutputData(allPosts);
                        adminOutputBoundary.prepareSuccessView(allPostsOutputData);
                    }
                    break;

                case "add_post":
                    if (adminInputData.getPostTitle() != null && !adminInputData.getPostTitle().trim().isEmpty() &&
                            adminInputData.getPostContent() != null && !adminInputData.getPostContent().trim().isEmpty()) {

                        Post newPost = adminDataAccessObject.addPost(
                                adminInputData.getPostTitle().trim(),
                                adminInputData.getPostContent().trim(),
                                adminInputData.getPostTags(),
                                adminInputData.getPostLocation(),
                                adminInputData.isLost(),
                                adminInputData.getAuthor() != null ? adminInputData.getAuthor() : "anonymous"
                        );

                        AdminOutputData addPostOutputData = new AdminOutputData("Post created successfully!", true);
                        adminOutputBoundary.prepareSuccessView(addPostOutputData);
                    } else {
                        adminOutputBoundary.prepareFailView(new AdminOutputData("Post title and content are required."));
                    }
                    break;

                case "edit_post":
                    boolean editSuccess = adminDataAccessObject.editPost(
                            adminInputData.getPostId(),
                            adminInputData.getPostTitle(),
                            adminInputData.getPostContent(),
                            adminInputData.getPostLocation(),
                            adminInputData.getPostTags(),
                            adminInputData.isLost()
                    );

                    if (editSuccess) {
                        // Get updated post list after edit
                        List<Post> updatedPosts = adminDataAccessObject.getAllPosts();
                        AdminOutputData editOutputData = new AdminOutputData("Post edited successfully!", true);
                        adminOutputBoundary.prepareSuccessView(editOutputData);
                    } else {
                        adminOutputBoundary.prepareFailView(new AdminOutputData("Failed to edit post"));
                    }
                    break;

                default:
                    adminOutputBoundary.prepareFailView(new AdminOutputData("Invalid action."));
                    break;
            }
        } catch (Exception e) {
            adminOutputBoundary.prepareFailView(new AdminOutputData("An error occurred: " + e.getMessage()));
        }
    }
}