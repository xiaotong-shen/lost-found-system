package use_case.change_username;

public class ChangeUsernameOutputData {
    private final String newUsername;
    private final boolean useCaseFailed;

    public ChangeUsernameOutputData(String newUsername, boolean useCaseFailed) {
        this.newUsername = newUsername;
        this.useCaseFailed = useCaseFailed;
    }

    public String getNewUsername() { return newUsername; }
    public boolean isUseCaseFailed() { return useCaseFailed; }
} 