package interface_adapter.admin;

import entity.Post;
import use_case.admin.AdminInteractor;

public class AdminController {
    private final AdminInteractor interactor;

    public AdminController(AdminInteractor interactor) {
        this.interactor = interactor;
    }

    public void editPost(int postId) {
    }
    public void deletePost(int postId) {
    }

    public void addPost() {

    }

    public void logout() {

    }
}
