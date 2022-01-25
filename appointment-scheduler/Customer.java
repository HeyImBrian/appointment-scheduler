package sample;

public class Customer {
    private int ID;
    private String name;
    private String address;
    private String postal;
    private String phone;
    private int divisionID;


    /**
     * Customer class is used to create customer objects.
     * @param name
     * @param address
     * @param postal
     * @param phone
     * @param divisionID
     */
    public Customer(String name, String address, String postal, String phone, int divisionID){
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.divisionID = divisionID;
    }



    public int getID(){
        return ID;
    }

    public void setID(int input){
        ID = input;
    }

    public String getName(){
        return name;
    }

    public void setName(String input){
        name = input;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String input){
        address = input;
    }

    public String getPostal(){
        return postal;
    }

    public void setPostal(String input){
        postal = input;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String input){
        phone = input;
    }

    public int getDivisionID(){
        return divisionID;
    }

    public void setDivisionID(int input){
        divisionID = input;
    }









}
