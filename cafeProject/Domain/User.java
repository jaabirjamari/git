package Domain;

public abstract class User {
    private String name;        // name for user

    // constructor  
    public User(String name){
        this.name = name; 
    }

    // getter
    public String getName(){ 
        return name;
    }

    // setter
    public void setName(String name){
        this.name = name;
    }

    // abstract get role to get roles
    public abstract String getRole();

    @Override
    public String toString() {
        return getRole() + ": " + name;
    }

}
