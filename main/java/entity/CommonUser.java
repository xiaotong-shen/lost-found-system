package entity;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User {

    // Firebase requires a no-arg constructor for deserialization
    public CommonUser() {
        this.name = "";
        this.password = "";
        this.admin = false;
    }

    private String name;
    private String password;
    private boolean admin;

    public CommonUser(String name, String password, boolean admin) {
        this.name = name;
        this.password = password;
        this.admin = admin;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAdmin() {
        return admin;
    }

    // Firebase setters for deserialization
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
