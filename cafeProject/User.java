package CafeOrderProject;

public abstract class User {
    private String name;

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

    public abstract String getRole();
}
