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
//            userDataAccessObject.deleteUser(username);
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
        try {
//            var users = userDataAccessObject.getAllUsers();
//            deleteUserPresenter.presentUsersList(users);
        } catch (Exception e) {
            deleteUserPresenter.prepareFailView("Failed to load users: " + e.getMessage());
        }
    }
}

