package interface_adapter.delete_post;

import interface_adapter.admin.AdminViewModel;
import use_case.delete_post.DeletePostOutputBoundary;
import use_case.delete_post.DeletePostOutputData;

public class DeletePostPresenter implements DeletePostOutputBoundary {
    private final AdminViewModel adminViewModel;

    public DeletePostPresenter(AdminViewModel adminViewModel) {
        this.adminViewModel = adminViewModel;
    }

    @Override
    public void prepareSuccessView(DeletePostOutputData deletePostOutputData) {
        adminViewModel.getState().setSuccessMessage("Post successfully deleted");
        adminViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(DeletePostOutputData deletePostOutputData) {
        adminViewModel.getState().setError(deletePostOutputData.getMessage());
        adminViewModel.firePropertyChanged();
    }
}