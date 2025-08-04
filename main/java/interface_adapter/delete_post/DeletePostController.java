package interface_adapter.delete_post;

import interface_adapter.admin.AdminViewModel;
import use_case.delete_post.DeletePostInputBoundary;
import use_case.delete_post.DeletePostOutputBoundary;

public class DeletePostController {
    private final DeletePostInputBoundary deletePostInteractor;

    public DeletePostController(DeletePostInputBoundary deletePostInteractor) {
        this.deletePostInteractor = deletePostInteractor;
    }

    public void deletePost(String postId) {
        deletePostInteractor.deletePost(postId);
    }
}

