package interface_adapter.change_username;

import use_case.change_username.ChangeUsernameInputBoundary;
import use_case.change_username.ChangeUsernameInputData;

public class ChangeUsernameController {
    private final ChangeUsernameInputBoundary interactor;

    public ChangeUsernameController(ChangeUsernameInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String oldUsername, String newUsername) {
        interactor.execute(new ChangeUsernameInputData(oldUsername, newUsername));
    }
} 