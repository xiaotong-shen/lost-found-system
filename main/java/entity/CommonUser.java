package entity;

/**
 * A simple implementation of the User interface.
 */
public class CommonUser implements User {

    // Firebase requires a no-arg constructor for deserialization
    public CommonUser() {
        this.name = "";
        this.password = "";
    }

    private String name;
    private String password;

    public CommonUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    // Firebase setters for deserialization
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
