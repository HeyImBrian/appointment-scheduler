package sample;

public class Contact {
    private int ID;
    private String name;
    private String email;

    /**
     * Contact class is used to create contact objects.
     * @param ID
     * @param name
     * @param email
     */
    public Contact(int ID, String name, String email) {
        this.ID = ID;
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}