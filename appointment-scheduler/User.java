package sample;

public class User {
    private int ID;
    private String name;
    private String password;


    /**
     * User class is used to create User objects.
     * @param ID
     * @param name
     * @param password
     */
    public User(int ID, String name, String password){
        this.ID = ID;
        this.name = name;
        this.password = password;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
