package interface_adapter.delete_user;

import interface_adapter.adminloggedIn.AdminLoggedInState;
import interface_adapter.adminloggedIn.AdminLoggedInViewModel;
import use_case.deleteUser.DeleteUserOutputBoundary;
import use_case.deleteUser.DeleteUserOutputData;

import java.util.List;

public class DeleteUserPresenter implements DeleteUserOutputBoundary {
    private final AdminLoggedInViewModel adminLoggedInViewModel;

    public DeleteUserPresenter(AdminLoggedInViewModel adminLoggedInViewModel) {
        this.adminLoggedInViewModel = adminLoggedInViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteUserOutputData response) {
        AdminLoggedInState state = adminLoggedInViewModel.getState();
        state.setDeleteUserMessage(response.getMessage());
        adminLoggedInViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        AdminLoggedInState state = adminLoggedInViewModel.getState();
        state.setDeleteUserError(error);
        adminLoggedInViewModel.firePropertyChanged();
    }

    @Override
    public void presentUsersList(List<String> users) {

    }
}