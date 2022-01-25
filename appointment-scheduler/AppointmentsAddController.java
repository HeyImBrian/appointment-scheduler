package sample;


import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.converter.StringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

/**
 * This is a form for adding appointments.
 */
public class AppointmentsAddController {


    private Parent root;
    private Scene scene;
    private Stage stage;

    private ObservableList<Customer> tempCustomerObservable;
    private ObservableList<String> tempIDAndNames;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    TextField fieldTitle;
    @FXML
    TextField fieldDesc;
    @FXML
    TextField fieldLoc;
    @FXML
    ComboBox comboBoxContact;
    @FXML
    ComboBox comboBoxType;
    @FXML
    DatePicker datePickerDate;
    @FXML
    TextField fieldStartTime;
    @FXML
    TextField fieldEndTime;
    @FXML
    ComboBox comboBoxCustomer;
    @FXML
    Label labelError;

    private ObservableList<Appointment> tempAppointmentList;

    /**
     * This function is ran when this page is loaded.
     * Sets the contactID to Contact Names.
     * Sets up the combo boxes for customers, contacts, and types.
     */
    public void initialize() throws SQLException {
        tempAppointmentList = JDBC.getAllAppointments();

        for (int i = 0; i < tempAppointmentList.size(); i++){
            tempAppointmentList.get(i).setContactID(ComboBoxCollections.comboBoxContacts.get(Integer.valueOf(tempAppointmentList.get(i).getContactID()) - 1));

            // These lines change the UTC time to Local time
            LocalDateTime tempStartTime = tempAppointmentList.get(i).getStart();
            LocalDateTime tempEndTime = tempAppointmentList.get(i).getEnd();
            tempAppointmentList.get(i).setStart(DifferentTimes.toLocal(tempStartTime));
            tempAppointmentList.get(i).setEnd(DifferentTimes.toLocal(tempEndTime));
        }




        comboBoxType.setItems(FXCollections.observableArrayList(ComboBoxCollections.comboBoxAppointmentTypes));
        tempCustomerObservable = JDBC.getAllCustomers();
        tempIDAndNames = FXCollections.observableArrayList();

        // Setting up customer list
        for (int i = 0; i < tempCustomerObservable.size(); i++){
            tempIDAndNames.add(tempCustomerObservable.get(i).getID() + " " + tempCustomerObservable.get(i).getName());
        }

        comboBoxContact.setItems(FXCollections.observableArrayList(ComboBoxCollections.comboBoxContacts));

        comboBoxCustomer.setItems((tempIDAndNames));
    }


    /**
     * Saves the current information to the database if it passes the error check.
     * Converts the date and times into an accepted format.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void save(ActionEvent event) throws IOException, SQLException {
        if (errorCheck()){
            return;
        }

        // Sets up String versions of the datetimes
        LocalDate date = LocalDate.parse(datePickerDate.getValue().toString(), dateFormatter);
        String tempStart = date + " " + fieldStartTime.getText();
        String tempEnd = date + " " + fieldEndTime.getText();


        // Parses Strings into LocalDateTimes
        LocalDateTime startDateTime = LocalDateTime.parse(tempStart.toString(), dateTimeFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse(tempEnd.toString(), dateTimeFormatter);

        // These lines change the Local time to UTC time
        startDateTime = DifferentTimes.toUtc(startDateTime);
        endDateTime = DifferentTimes.toUtc(endDateTime);

        // Gets CustomerID
        int currentCustomerSelectedIndex = comboBoxCustomer.getSelectionModel().getSelectedIndex();
        int currentSelectedCustomerID = tempCustomerObservable.get(currentCustomerSelectedIndex).getID();

        Appointment tempAppointment = new Appointment(fieldTitle.getText(), fieldDesc.getText(), fieldLoc.getText(), comboBoxType.getValue().toString(), startDateTime, endDateTime, currentSelectedCustomerID, JDBC.currentUserID, String.valueOf(comboBoxContact.getSelectionModel().getSelectedIndex() + 1));
        JDBC.addAppointment(tempAppointment);


        root = FXMLLoader.load(getClass().getResource("AppointmentsForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    public void cancel(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AppointmentsForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Checks for unacceptable inputs, and updates the error text accordingly.
     * Lambda is used with the ErrorInterface to set the amount of new-lines added at the end of the string.
     * Because lambda is abstract, I made one expression add one new-line, and another to make two new-lines.
     * This is useful because I can easily change the amount of new-lines, or add anything to the text as needed.
     * Instead of changing 15 lines of code manually, I can just change one or two.
     * @return true if error is found.
     */
    public boolean errorCheck(){ // Returns true if error is found.
        boolean errorFound = false;
        boolean timeGoodFormat = true;
        String errorText = "Exception:\n";

        ErrorInterface err1 = (input) -> input + "\n";
        ErrorInterface err2 = (input) -> input + "\n";


        if (fieldTitle.getLength() == 0){
            errorFound = true;
            errorText += err1.nl("Title required");
        }
        if (fieldDesc.getLength() == 0){
            errorFound = true;
            errorText += err1.nl("Description required");
        }
        if (fieldLoc.getLength() == 0){
            errorFound = true;
            errorText += err1.nl("Location required");
        }
        if (comboBoxContact.getValue() == null){
            errorFound = true;
            errorText += err1.nl("Contact ID required");
        }
        if (timeErrorCheck(fieldStartTime.getText())){
            errorFound = true;
            timeGoodFormat = false;
            errorText += err1.nl("Start time must be HH:MM:SS");
        }
        if (timeErrorCheck(fieldEndTime.getText())){
            errorFound = true;
            timeGoodFormat = false;
            errorText += err1.nl("End time must be HH:MM:SS");
        }

        if (timeBusinessHoursCheck(fieldStartTime.getText()) && timeGoodFormat){
            LocalTime tempTime = LocalTime.parse(fieldStartTime.getText(), timeFormatter); // Get EST time to display
            tempTime = DifferentTimes.localToEst(tempTime);

            errorFound = true;
            errorText += err1.nl("EST Business hours is 08:00 - 22:00");
            errorText += err2.nl("Start input time to EST: " + tempTime);
        }


        if (timeBusinessHoursCheck(fieldEndTime.getText()) && timeGoodFormat) {
            LocalTime tempTime = LocalTime.parse(fieldEndTime.getText(), timeFormatter); // Get EST time to display
            tempTime = DifferentTimes.localToEst(tempTime);

            errorFound = true;
            errorText += err1.nl("EST Business hours is 08:00 - 22:00");
            errorText += err2.nl("End input time to EST: " + tempTime);
        }

        if (timeEndAfterStartCheck(fieldStartTime.getText(), fieldEndTime.getText()) && timeGoodFormat) {
            errorFound = true;
            errorText += err1.nl("Start time must be before end time ");
        }

        if (timeCheckIfOverlapping() && timeGoodFormat){
            errorFound = true;
            errorText += err1.nl("This appointment overlaps another appointment ");
        }

        if (comboBoxType.getValue() == null){
            errorFound = true;
            errorText += err1.nl("Type required");
        }
        if (comboBoxCustomer.getValue() == null){
            errorFound = true;
            errorText += err1.nl("Customer required");
        }
        if (datePickerDate.getValue() == null){
            errorFound = true;
            errorText += err1.nl("Date is required");
        }

        if (errorFound){
            labelError.setText(errorText);
            return true;
        } else {
            return false;
        }
    }


    /**
     * Checks if the time is in the correct format.
     * @param input
     * @return true if an error is found.
     */
    public boolean timeErrorCheck(String input){ // Returns true if error
        try{
            LocalTime tempTime = LocalTime.parse(input, timeFormatter);

            return false;
        } catch (Exception e) {
            return true;
        }
    }


    /**
     * Checks if time is within EST business hours
     * @param input
     * @return true if an error is found.
     */
    public boolean timeBusinessHoursCheck(String input){ //Returns true if error
        try {
            LocalTime tempTime = LocalTime.parse(input, timeFormatter);

            // Convert times to EST
            tempTime = DifferentTimes.localToEst(tempTime);

            if (tempTime.isBefore(DifferentTimes.getEstCompanyOpen()) || tempTime.isAfter(DifferentTimes.getEstCompanyClose())) {
                return true;
            }

            return false;
        } catch (Exception e){
            return true;
        }
    }


    /**
     * Checks if the starting time is before the end time.
     * @param inputStart
     * @param inputEnd
     * @return true if an error is found.
     */
    public boolean timeEndAfterStartCheck(String inputStart, String inputEnd){ // Returns true if error
        try {
            LocalTime tempStartTime = LocalTime.parse(inputStart, timeFormatter);
            LocalTime tempEndTime = LocalTime.parse(inputEnd, timeFormatter);

            if (tempStartTime.isBefore(tempEndTime)) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e){
            return true;
        }

    }


    /**
     * Checks if start or end times are overlapping with existing appointments
     * @return true if an error is found.
     */
    public boolean timeCheckIfOverlapping(){ // Returns true if overlaps
        try {
            LocalDate tempDate = LocalDate.parse(datePickerDate.getValue().toString(), dateFormatter);
            String tempStart = tempDate + " " + fieldStartTime.getText();
            String tempEnd = tempDate + " " + fieldEndTime.getText();

            // Parses Strings into LocalDateTimes
            LocalDateTime startDateTime = LocalDateTime.parse(tempStart.toString(), dateTimeFormatter);
            LocalDateTime endDateTime = LocalDateTime.parse(tempEnd.toString(), dateTimeFormatter);


            LocalDateTime currCustStart;
            LocalDateTime currCustEnd;
            for (int i = 0; i < tempAppointmentList.size(); i++) {
                currCustStart = tempAppointmentList.get(i).getStart();
                currCustEnd = tempAppointmentList.get(i).getEnd();

                // Check if any part of our input is within existing appointments
                if (startDateTime.isAfter(currCustStart) && startDateTime.isBefore(currCustEnd)) {
                    return true;
                }
                if (endDateTime.isAfter(currCustStart) && endDateTime.isBefore(currCustEnd)) {
                    return true;
                }

                // Check if existing appointments are within our input
                if (currCustStart.isAfter(startDateTime) && currCustStart.isBefore(endDateTime)) {
                    return true;
                }
                if (currCustEnd.isAfter(startDateTime) && currCustEnd.isBefore(endDateTime)) {
                    return true;
                }
            }

            // If no overlap, return false.
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
