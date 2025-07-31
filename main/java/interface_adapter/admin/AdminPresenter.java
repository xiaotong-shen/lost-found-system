package interface_adapter.admin;

import use_case.admin.AdminOutputBoundary;
import use_case.admin.AdminOutputData;

/**
 * Presenter for the Dashboard View.
 * Handles the output from the dashboard use case and updates the view model.
 */
public class AdminPresenter implements AdminOutputBoundary {
    private final AdminViewModel adminViewModel;

    public AdminPresenter(AdminViewModel adminViewModel) {
        this.adminViewModel = adminViewModel;
    }

    @Override
    public void prepareSuccessView(AdminOutputData adminOutputData) {
        AdminState currentState = adminViewModel.getState();

        if (adminOutputData.getPosts() != null) {
            currentState.setPosts(adminOutputData.getPosts());
        }
        if (adminOutputData.getSelectedPost() != null) {
            currentState.setSelectedPost(adminOutputData.getSelectedPost());
        }
        if (adminOutputData.getSuccessMessage() != null) {
            currentState.setSuccessMessage(adminOutputData.getSuccessMessage());
        }

        currentState.setError("");
        currentState.setLoading(false);
        adminViewModel.setState(currentState);
        adminViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(AdminOutputData adminOutputData) {
        AdminState currentState = adminViewModel.getState();
        currentState.setError(adminOutputData.getError());
        currentState.setSuccessMessage("");
        currentState.setLoading(false);
        adminViewModel.setState(currentState);
        adminViewModel.firePropertyChanged();
    }

    /**
     * Prepares the view for loading state.
     */
    public void prepareLoadingView() {
        AdminState currentState = adminViewModel.getState();
        currentState.setLoading(true);
        currentState.setError("");
        currentState.setSuccessMessage("");
        adminViewModel.setState(currentState);
        adminViewModel.firePropertyChanged();
    }
}
