package entity;

/**
 * Factory for creating CommonUser objects.
 */
public class CommonUserFactory implements UserFactory {

    @Override
    public User create(String name, String password, boolean admin) {
        return new CommonUser(name, password, admin);
    }
}
