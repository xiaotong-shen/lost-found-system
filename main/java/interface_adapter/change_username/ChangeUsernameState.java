package interface_adapter.change_username;

public class ChangeUsernameState {
    private String newUsername = "";
    private String error = null;

    public String getNewUsername() { return newUsername; }
    public void setNewUsername(String newUsername) { this.newUsername = newUsername; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
} 