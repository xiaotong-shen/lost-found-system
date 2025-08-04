package use_case.deleteUser;

import java.util.List;

public interface DeleteUserOutputBoundary {
    void prepareSuccessView(DeleteUserOutputData user);
    void prepareFailView(String error);
    void presentUsersList(List<String> users);
}
