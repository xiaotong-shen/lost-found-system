package use_case.admin;

import data_access.FirebasePostDataAccessObject;
import entity.Post;

import java.util.List;

/**
 * Interactor for the admin use case.
 * Implements the business logic for admin operations.
 */
public class AdminInteractor implements AdminInputBoundary {
    private final FirebasePostDataAccessObject adminDataAccessObject;
    private final AdminOutputBoundary adminOutputBoundary;

    public AdminInteractor(FirebasePostDataAccessObject adminDataAccessObject,
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
                    System.out.println("AdminInteractor: Processing edit_post action");
                    System.out.println("AdminInteractor: Post ID: " + adminInputData.getPostId());
                    
                    boolean editSuccess = adminDataAccessObject.editPost(
                            adminInputData.getPostId(),
                            adminInputData.getPostTitle(),
                            adminInputData.getPostContent(),
                            adminInputData.getPostLocation(),
                            adminInputData.getPostTags(),
                            adminInputData.isLost()
                    );

                    System.out.println("AdminInteractor: Edit operation result: " + (editSuccess ? "Success" : "Failed"));

                    if (editSuccess) {
                        // Get updated post list after edit
                        List<Post> updatedPosts = adminDataAccessObject.getAllPosts();
                        System.out.println("AdminInteractor: Retrieved " + updatedPosts.size() + " posts after edit");
                        
                        // Get the updated version of the edited post
                        Post updatedPost = adminDataAccessObject.getPostById(Integer.parseInt(adminInputData.getPostId()));
                        System.out.println("AdminInteractor: Retrieved updated post: " + 
                                          (updatedPost != null ? "Success" : "Failed"));
                        
                        AdminOutputData editOutputData = new AdminOutputData("Post edited successfully!", true);
                        editOutputData.setPosts(updatedPosts);
                        editOutputData.setSelectedPost(updatedPost);
                        adminOutputBoundary.prepareSuccessView(editOutputData);
                    } else {
                        adminOutputBoundary.prepareFailView(new AdminOutputData("Failed to edit post"));
                    }
                    break;

                case "delete_post":
                    System.out.println("\nAdminInteractor: Processing delete_post action");
                    String postIdToDelete = adminInputData.getPostId();
                    
                    System.out.println("AdminInteractor: Validating post ID: " + postIdToDelete);
                    if (postIdToDelete == null || postIdToDelete.trim().isEmpty()) {
                        System.err.println("AdminInteractor: Invalid post ID detected");
                        adminOutputBoundary.prepareFailView(new AdminOutputData("Invalid post ID"));
                        return;
                    }

                    try {
                        System.out.println("AdminInteractor: Checking if post exists");
                        boolean exists = adminDataAccessObject.existsPost(postIdToDelete);
                        System.out.println("AdminInteractor: Post exists check result: " + exists);

                        if (!exists) {
                            System.err.println("AdminInteractor: Post not found for deletion");
                            adminOutputBoundary.prepareFailView(new AdminOutputData("Post not found"));
                            return;
                        }

                        System.out.println("AdminInteractor: Initiating delete operation in DAO");
                        adminDataAccessObject.deletePost(postIdToDelete);
                        
                        System.out.println("AdminInteractor: Delete operation completed, preparing success view");
                        List<Post> remainingPosts = adminDataAccessObject.getAllPosts();
                        System.out.println("AdminInteractor: Retrieved " + remainingPosts.size() + " remaining posts");
                        
                        AdminOutputData deleteOutputData = new AdminOutputData("Post deleted successfully!", true);
                        deleteOutputData.setPosts(remainingPosts);
                        adminOutputBoundary.prepareSuccessView(deleteOutputData);
                        
                    } catch (Exception e) {
                        System.err.println("AdminInteractor: Error during delete operation: " + e.getMessage());
                        e.printStackTrace();
                        adminOutputBoundary.prepareFailView(new AdminOutputData("Error deleting post: " + e.getMessage()));
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