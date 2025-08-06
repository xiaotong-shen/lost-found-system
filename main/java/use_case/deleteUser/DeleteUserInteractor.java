package use_case.deleteUser;

import data_access.FirebaseUserDataAccessObject;

public class DeleteUserInteractor implements DeleteUserInputBoundary {
    final FirebaseUserDataAccessObject userDataAccessObject;
    final DeleteUserOutputBoundary deleteUserPresenter;

    public DeleteUserInteractor(FirebaseUserDataAccessObject userDataAccessObject,
                                DeleteUserOutputBoundary deleteUserOutputBoundary) {
        this.userDataAccessObject = userDataAccessObject;
        this.deleteUserPresenter = deleteUserOutputBoundary;
    }

    @Override
    public void execute(DeleteUserInputData deleteUserInputData) {
        String username = deleteUserInputData.getUsername();

        try {
            userDataAccessObject.deleteUser(username);
            DeleteUserOutputData deleteUserOutputData = new DeleteUserOutputData(true,
                    "Successfully deleted user: " + username);
            deleteUserPresenter.prepareSuccessView(deleteUserOutputData);

            // Reload users list after successful deletion
            loadUsers();
        } catch (Exception e) {
            deleteUserPresenter.prepareFailView("Failed to delete user: " + e.getMessage());
        }
    }

    @Override
    public void loadUsers() {
        System.out.println("DEBUG: DeleteUserInteractor.loadUsers called");
        try {
            var users = userDataAccessObject.getAllUsers();
            System.out.println("DEBUG: Retrieved users from DAO: " + users);
            deleteUserPresenter.presentUsersList(users);
        } catch (Exception e) {
            System.err.println("DEBUG: Error in loadUsers: " + e.getMessage());
            deleteUserPresenter.prepareFailView("Failed to load users: " + e.getMessage());
        }
    }
}

