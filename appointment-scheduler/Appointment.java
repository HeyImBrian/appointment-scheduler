package sample;

import java.time.LocalDateTime;

public class Appointment {
    private int ID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int userID;
    private String contactID;

    /**
     * Appointment class is used to create appointment objects.
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     * @param contactID
     */
    public Appointment(String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID, String contactID){
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * set ID
     * @param ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * set title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * set location.
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * set type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return start
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * set start
     * @param start
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * @return end
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * set end
     * @param end
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * @return customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * set customerID
     * @param customerID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * set userID
     * @param userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return contactID
     */
    public String getContactID() {
        return contactID;
    }

    /**
     * set contactID
     * @param contactID
     */
    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
