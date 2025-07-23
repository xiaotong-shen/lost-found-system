package use_case.change_username;

public interface ChangeUsernameUserDataAccessInterface {
    boolean existsByName(String username);
    boolean changeUsername(String oldUsername, String newUsername);
} 