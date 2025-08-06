package Domain;

import java.util.List;

public abstract class Staff {

    // Attributes
    protected String username;
    protected String password;
    protected Staff role;  // enum: BARISTA, MANAGER
    protected String fullName;

    // Constructor
    protected Staff(String username, String password, Staff role, String fullName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
    }

    // Methods
    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    public abstract List<String> getPermissions();

    // Standard getters
    public String getUsername() {
        return username;
    }

    public Staff getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }
}
