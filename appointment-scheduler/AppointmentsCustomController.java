package sample;


import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This page has custom views for appointments.
 */
public class AppointmentsCustomController {


    private Parent root;
    private Scene scene;
    private Stage stage;

    private DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");


    @FXML
    ComboBox comboBoxContact;
    @FXML
    ComboBox comboBoxMonth;
    @FXML
    ComboBox comboBoxType;

    @FXML
    Label labelMonthTypeTotal;
    @FXML
    Label labelAllTotal;

    @FXML
    TableView appointmentsTable;
    @FXML
    TableColumn tableID;
    @FXML
    TableColumn tableTitle;
    @FXML
    TableColumn tableDesc;
    @FXML
    TableColumn tableLoc;
    @FXML
    TableColumn tableContactID;
    @FXML
    TableColumn tableType;
    @FXML
    TableColumn tableStartDT;
    @FXML
    TableColumn tableEndDT;
    @FXML
    TableColumn tableCustomerID;
    @FXML
    TableColumn tableUserID;

    @FXML
    Button cancelButton;

    ObservableList<String> months = FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
    private ObservableList<Appointment> tempAppointmentList;


    /**
     * This is called when the page loads.
     * Sets up the table and combo boxes with the required information.
     * @throws SQLException
     */
    public void initialize() throws SQLException{
        // Changes contact ID to a name
        tempAppointmentList = JDBC.getAllAppointments();
        for (int i = 0; i < tempAppointmentList.size(); i++){
            tempAppointmentList.get(i).setContactID(ComboBoxCollections.comboBoxContacts.get(Integer.valueOf(tempAppointmentList.get(i).getContactID()) - 1));

            // These lines change the UTC time to Local time
            LocalDateTime tempStartTime = tempAppointmentList.get(i).getStart();
            LocalDateTime tempEndTime = tempAppointmentList.get(i).getEnd();
            tempAppointmentList.get(i).setStart(DifferentTimes.toLocal(tempStartTime));
            tempAppointmentList.get(i).setEnd(DifferentTimes.toLocal(tempEndTime));
        }
        appointmentsTable.setItems(tempAppointmentList);

        // Set columns
        tableID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tableTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        tableDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tableLoc.setCellValueFactory(new PropertyValueFactory<>("Location"));
        tableContactID.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
        tableType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        tableStartDT.setCellValueFactory(new PropertyValueFactory<>("Start"));
        tableEndDT.setCellValueFactory(new PropertyValueFactory<>("End"));
        tableCustomerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        tableUserID.setCellValueFactory(new PropertyValueFactory<>("UserID"));



        comboBoxContact.setItems(FXCollections.observableArrayList(ComboBoxCollections.comboBoxContacts));
        comboBoxMonth.setItems(months);
        comboBoxType.setItems(FXCollections.observableArrayList(ComboBoxCollections.comboBoxAppointmentTypes));



        // Set Appointment Totals
        labelAllTotal.setText(String.valueOf(tempAppointmentList.size()));
    }

    /**
     * Called when the contact is selected/changed.
     * Sets the table up with appointments with the same chosen contact.
     * @param event
     * @throws SQLException
     */
    public void changeContact(ActionEvent event) throws SQLException {
        int selectedContact = comboBoxContact.getSelectionModel().getSelectedIndex();
        int matchID = JDBC.getAllContacts().get(selectedContact).getID();

        ObservableList<Appointment> tempAppList = JDBC.getAllAppointments();
        ObservableList<Appointment> matchingAppointments = FXCollections.observableArrayList();

        for (int i = 0; i < tempAppList.size(); i++){
            if (tempAppList.get(i).getContactID().equals(String.valueOf(matchID))){
                matchingAppointments.add(tempAppList.get(i));
            }
        }

        // Changes contact ID to a name, and fixes time
        for (int i = 0; i < matchingAppointments.size(); i++){
            matchingAppointments.get(i).setContactID(ComboBoxCollections.comboBoxContacts.get(Integer.valueOf(matchingAppointments.get(i).getContactID()) - 1));

            // These lines change the UTC time to Local time
            LocalDateTime tempStartTime = matchingAppointments.get(i).getStart();
            LocalDateTime tempEndTime = matchingAppointments.get(i).getEnd();
            matchingAppointments.get(i).setStart(DifferentTimes.toLocal(tempStartTime));
            matchingAppointments.get(i).setEnd(DifferentTimes.toLocal(tempEndTime));
        }

        appointmentsTable.setItems(matchingAppointments);

    }

    /**
     * Called when the month combo box is changed/selected.
     * Needs to be combined with a the type combo box for results.
     * @param event
     * @throws SQLException
     */
    public void changeMonth(ActionEvent event) throws SQLException {
        if (comboBoxMonth.getSelectionModel().isEmpty()){
            return;
        }
        if (comboBoxType.getSelectionModel().isEmpty()){
            return;
        }

        monthTypeMatches(comboBoxMonth.getValue().toString(), comboBoxType.getValue().toString());
    }

    /**
     * Called when the type combo box is changed/selected.
     * Needs to be combined with a the month combo box for results.
     * @param event
     * @throws SQLException
     */
    public void changeType(ActionEvent event) throws SQLException {
        if (comboBoxMonth.getSelectionModel().isEmpty()){
            return;
        }
        if (comboBoxType.getSelectionModel().isEmpty()){
            return;
        }

        monthTypeMatches(comboBoxMonth.getValue().toString(), comboBoxType.getValue().toString());
    }

    /**
     * When the month and type combo boxes have values, this function is called.
     * Gets a count of the appointments that matches those two requirements.
      * @param insertMonth
     * @param insertType
     * @throws SQLException
     */
    public void monthTypeMatches(String insertMonth, String insertType) throws SQLException {
        ObservableList<Appointment> tempAppointmentList = JDBC.getAllAppointments();

        int totalCount = 0;
        for (int i = 0; i < tempAppointmentList.size(); i++){
            int checkMonth = tempAppointmentList.get(i).getStart().getMonthValue();

            if (Integer.valueOf(insertMonth) == checkMonth && insertType.equals(tempAppointmentList.get(i).getType())){
                totalCount += 1;
            }
        }

        labelMonthTypeTotal.setText(String.valueOf(totalCount));
    }


    public void cancel(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AppointmentsForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
