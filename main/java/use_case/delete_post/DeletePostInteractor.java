package use_case.delete_post;

public class DeletePostInteractor implements DeletePostInputBoundary {
    private final DeletePostDataAccessInterface postDataAccessObject;
    private final DeletePostOutputBoundary deletePostPresenter;

    public DeletePostInteractor(DeletePostDataAccessInterface postDataAccessObject,
                                DeletePostOutputBoundary deletePostPresenter) {
        this.postDataAccessObject = postDataAccessObject;
        this.deletePostPresenter = deletePostPresenter;
    }

    @Override
    public void deletePost(String postId) {
        if (!postDataAccessObject.existsPost(postId)) {
            DeletePostOutputData outputData = new DeletePostOutputData("Post does not exist", false);
            deletePostPresenter.prepareFailView(outputData);
            return;
        }

        try {
            postDataAccessObject.deletePost(postId);
            DeletePostOutputData outputData = new DeletePostOutputData("Post successfully deleted", true);
            deletePostPresenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            DeletePostOutputData outputData = new DeletePostOutputData(
                    "Failed to delete post: " + e.getMessage(), false);
            deletePostPresenter.prepareFailView(outputData);
        }
    }
}