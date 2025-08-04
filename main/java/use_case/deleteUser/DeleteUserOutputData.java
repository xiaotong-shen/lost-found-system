package use_case.deleteUser;

public class DeleteUserOutputData {
    private final boolean success;
    private final String message;

    public DeleteUserOutputData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
