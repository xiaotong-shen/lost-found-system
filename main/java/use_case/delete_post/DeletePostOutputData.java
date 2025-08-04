package use_case.delete_post;

public class DeletePostOutputData {
    private final String message;
    private final boolean success;

    public DeletePostOutputData(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}