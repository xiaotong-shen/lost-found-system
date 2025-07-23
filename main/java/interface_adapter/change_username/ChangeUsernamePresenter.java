package interface_adapter.change_username;

import use_case.change_username.ChangeUsernameOutputBoundary;
import use_case.change_username.ChangeUsernameOutputData;
import interface_adapter.change_password.LoggedInViewModel;
import interface_adapter.change_password.LoggedInState;

public class ChangeUsernamePresenter implements ChangeUsernameOutputBoundary {
    private final ChangeUsernameViewModel viewModel;
    private final LoggedInViewModel loggedInViewModel;

    public ChangeUsernamePresenter(ChangeUsernameViewModel viewModel, LoggedInViewModel loggedInViewModel) {
        this.viewModel = viewModel;
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(ChangeUsernameOutputData outputData) {
        ChangeUsernameState state = viewModel.getState();
        state.setNewUsername(outputData.getNewUsername());
        state.setError(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged("usernameChanged");

        // Update the logged in view model's state and fire property change
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(outputData.getNewUsername());
        loggedInViewModel.setState(loggedInState);
        loggedInViewModel.firePropertyChanged("state");
    }

    @Override
    public void prepareFailView(String error) {
        ChangeUsernameState state = viewModel.getState();
        state.setError(error);
        viewModel.setState(state);
        viewModel.firePropertyChanged("usernameChangeError");
    }
} 