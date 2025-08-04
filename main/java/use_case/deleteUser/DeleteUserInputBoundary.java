package use_case.deleteUser;

public interface DeleteUserInputBoundary {
    void execute(DeleteUserInputData deleteUserInputData);
    void loadUsers();
}