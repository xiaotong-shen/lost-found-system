package interface_adapter.delete_user;

import use_case.deleteUser.DeleteUserOutputBoundary;
import use_case.deleteUser.DeleteUserOutputData;
import java.util.List;

public class DeleteUserPresenter implements DeleteUserOutputBoundary {
    private final DeleteUserViewModel deleteUserViewModel;

    public DeleteUserPresenter(DeleteUserViewModel deleteUserViewModel) {
        this.deleteUserViewModel = deleteUserViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteUserOutputData data) {
        DeleteUserState state = deleteUserViewModel.getState();
        state.setSuccessMessage(data.getMessage());
        state.setError("");
        deleteUserViewModel.setState(state);
        deleteUserViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        DeleteUserState state = deleteUserViewModel.getState();
        state.setError(error);
        state.setSuccessMessage("");
        deleteUserViewModel.setState(state);
        deleteUserViewModel.firePropertyChanged();
    }

    @Override
    public void presentUsersList(List<String> users) {
        System.out.println("DEBUG: DeleteUserPresenter.presentUsersList called with users: " + users);
        DeleteUserState state = deleteUserViewModel.getState();
        state.setUsersList(users);
        deleteUserViewModel.setState(state);
        deleteUserViewModel.firePropertyChanged();
        System.out.println("DEBUG: DeleteUserPresenter finished updating state");
    }
}