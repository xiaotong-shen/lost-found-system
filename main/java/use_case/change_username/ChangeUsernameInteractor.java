package use_case.change_username;

public class ChangeUsernameInteractor implements ChangeUsernameInputBoundary {
    private final ChangeUsernameUserDataAccessInterface userDataAccessObject;
    private final ChangeUsernameOutputBoundary outputBoundary;

    public ChangeUsernameInteractor(ChangeUsernameUserDataAccessInterface userDataAccessObject,
                                    ChangeUsernameOutputBoundary outputBoundary) {
        this.userDataAccessObject = userDataAccessObject;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(ChangeUsernameInputData inputData) {
        String oldUsername = inputData.getOldUsername();
        String newUsername = inputData.getNewUsername();
        if (userDataAccessObject.existsByName(newUsername)) {
            outputBoundary.prepareFailView("Username already exists.");
            return;
        }
        boolean success = userDataAccessObject.changeUsername(oldUsername, newUsername);
        if (success) {
            outputBoundary.prepareSuccessView(new ChangeUsernameOutputData(newUsername, false));
        } else {
            outputBoundary.prepareFailView("Failed to change username.");
        }
    }
} 