package entity;

/**
 * Factory for creating users.
 */
public interface UserFactory {
    /**
     * Creates a new User.
     * @param name the name of the new user
     * @param password the password of the new user
     * @param admin true if there is admin
     * @return the new user
     */
    User create(String name, String password, boolean admin);

}
