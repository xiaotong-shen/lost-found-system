package entity;

/**
 * The representation of a user in our program.
 */
public interface User {

    /**
     * Returns the username of the user.
     * @return the username of the user.
     */
    String getName();

    /**
     * Returns the password of the user.
     * @return the password of the user.
     */
    String getPassword();

    /**
     * Returns whether the user is an admin. For backward compatibility, the default
     * implementation returns false so older test doubles that do not implement this
     * method will still compile and behave as non-admin users.
     * @return true if the user is an admin, false otherwise
     */
    default boolean isAdmin() { return false; }

}
