package use_case.change_username;

public interface ChangeUsernameOutputBoundary {
    void prepareSuccessView(ChangeUsernameOutputData outputData);
    void prepareFailView(String error);
} 