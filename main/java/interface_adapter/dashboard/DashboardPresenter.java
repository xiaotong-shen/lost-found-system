package interface_adapter.dashboard;

import use_case.dashboard.DashboardOutputBoundary;
import use_case.dashboard.DashboardOutputData;

/**
 * Presenter for the Dashboard View.
 * Handles the output from the dashboard use case and updates the view model.
 */
public class DashboardPresenter implements DashboardOutputBoundary {
    private final DashboardViewModel dashboardViewModel;

    public DashboardPresenter(DashboardViewModel dashboardViewModel) {
        this.dashboardViewModel = dashboardViewModel;
    }

    @Override
    public void prepareSuccessView(DashboardOutputData dashboardOutputData) {
        DashboardState currentState = dashboardViewModel.getState();
        
        if (dashboardOutputData.getPosts() != null) {
            currentState.setPosts(dashboardOutputData.getPosts());
        }
        if (dashboardOutputData.getSelectedPost() != null) {
            currentState.setSelectedPost(dashboardOutputData.getSelectedPost());
        }
        if (dashboardOutputData.getSuccessMessage() != null) {
            System.out.println("DEBUG: DashboardPresenter setting success message: '" + dashboardOutputData.getSuccessMessage() + "'");
            currentState.setSuccessMessage(dashboardOutputData.getSuccessMessage());
        }
        
        currentState.setError("");
        currentState.setLoading(false);
        dashboardViewModel.setState(currentState);
        dashboardViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(DashboardOutputData dashboardOutputData) {
        DashboardState currentState = dashboardViewModel.getState();
        currentState.setError(dashboardOutputData.getError());
        currentState.setSuccessMessage("");
        currentState.setLoading(false);
        dashboardViewModel.setState(currentState);
        dashboardViewModel.firePropertyChanged();
    }

    /**
     * Prepares the view for loading state.
     */
    public void prepareLoadingView() {
        DashboardState currentState = dashboardViewModel.getState();
        currentState.setLoading(true);
        currentState.setError("");
        currentState.setSuccessMessage("");
        dashboardViewModel.setState(currentState);
        dashboardViewModel.firePropertyChanged();
    }
}
