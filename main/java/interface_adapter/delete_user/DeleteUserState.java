
package interface_adapter.delete_user;

import java.util.ArrayList;
import java.util.List;

public class DeleteUserState {
    private List<String> usersList = new ArrayList<>();
    private String error = "";
    private String successMessage = "";

    public List<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<String> usersList) {
        this.usersList = usersList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}