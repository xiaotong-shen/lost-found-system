package use_case.deleteUser;

public class DeleteUserInputData {
    private final String username;

    public DeleteUserInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
