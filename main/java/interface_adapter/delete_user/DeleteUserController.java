package interface_adapter.delete_user;

import use_case.deleteUser.DeleteUserInputBoundary;
import use_case.deleteUser.DeleteUserInputData;

public class DeleteUserController {
    final DeleteUserInputBoundary deleteUserUseCaseInteractor;

    public DeleteUserController(DeleteUserInputBoundary deleteUserUseCaseInteractor) {
        System.out.println("DEBUG: Creating DeleteUserController with interactor: " + (deleteUserUseCaseInteractor != null));
        this.deleteUserUseCaseInteractor = deleteUserUseCaseInteractor;
    }


    public void execute(String username) {
        DeleteUserInputData deleteUserInputData = new DeleteUserInputData(username);
        deleteUserUseCaseInteractor.execute(deleteUserInputData);
    }

    public void loadUsers() {
        System.out.println("DEBUG: loadUsers called, interactor is: " + (deleteUserUseCaseInteractor != null));
        if (deleteUserUseCaseInteractor == null) {
            throw new IllegalStateException("DeleteUserInputBoundary not initialized");
        }
        deleteUserUseCaseInteractor.loadUsers();
    }

}