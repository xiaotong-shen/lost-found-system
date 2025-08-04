package interface_adapter.delete_user;

import use_case.deleteUser.DeleteUserInputBoundary;
import use_case.deleteUser.DeleteUserInputData;

public class DeleteUserController {
    final DeleteUserInputBoundary deleteUserUseCaseInteractor;

    public DeleteUserController(DeleteUserInputBoundary deleteUserUseCaseInteractor) {
        this.deleteUserUseCaseInteractor = deleteUserUseCaseInteractor;
    }

    public void execute(String username) {
        DeleteUserInputData deleteUserInputData = new DeleteUserInputData(username);
        deleteUserUseCaseInteractor.execute(deleteUserInputData);
    }

    public void loadUsers() {
        deleteUserUseCaseInteractor.loadUsers();
    }
}