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

/**
 * This is the main appointment page that shows a table of appointments.
 * This page leads to other related pages.
 *
 * Radio buttons are located at the top to change table views.
 */
public class AppointmentsController {


    private Parent root;
    private Scene scene;
    private Stage stage;

    @FXML
    RadioButton radioMonth;
    @FXML
    RadioButton radioWeek;
    @FXML
    RadioButton radioAll;


    @FXML
    Button customReportButton;
    @FXML
    Button addButton;
    @FXML
    Button updateButton;
    @FXML
    Button deleteButton;
    @FXML
    Button cancelButton;


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

    /**
     * This list was necessary for changing the contactIDs to their respective names.
     */
    private ObservableList<Appointment> tempAppointmentList;

    /**
     * The initialize function gets called whenever this page is opened.
     * Fills the appointment table with information from the database.
     *
     * A lambda expression was used to make the for loop more compact. Changes ContactID to Contact Name.
     * @throws SQLException
     */
    public void initialize() throws SQLException {
        tempAppointmentList = JDBC.getAllAppointments();

        tempAppointmentList.forEach(app -> {
            app.setContactID(ComboBoxCollections.comboBoxContacts.get(Integer.valueOf(app.getContactID()) - 1));
            // These lines change the UTC time to Local time
            LocalDateTime tempStartTime = app.getStart();
            LocalDateTime tempEndTime = app.getEnd();
            app.setStart(DifferentTimes.toLocal(tempStartTime));
            app.setEnd(DifferentTimes.toLocal(tempEndTime));
        });

        appointmentsTable.setItems(tempAppointmentList);


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


    }


    /**
     * When the delete button is pressed, this function is called.
     * Deletes a selected appointment from the database.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void delete(ActionEvent event) throws IOException, SQLException {
        Appointment selectedAppointment = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null){
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Item");
        alert.setHeaderText("Are you sure you'd like to delete: \n" + "ID: " + String.valueOf(selectedAppointment.getID()) + " Type: " + selectedAppointment.getType());

        if (alert.showAndWait().get() == ButtonType.OK){
            JDBC.deleteAppointmentByID(selectedAppointment.getID());
            tempAppointmentList.remove(selectedAppointment);
            appointmentsTable.setItems(tempAppointmentList);
        } else{
            return;
        }
    }


    /**
     * Switches views of the table.
     * Options are: All, Month, Week.
     * If none are found, displays alert.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void radioSwitch(ActionEvent event) throws IOException, SQLException {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        ZonedDateTime monthAfter = ZonedDateTime.now(ZoneOffset.UTC).plusMonths(1);
        ZonedDateTime weekAfter = ZonedDateTime.now(ZoneOffset.UTC).plusWeeks(1);

        ObservableList<Appointment> matchingAppointments = FXCollections.observableArrayList();
        if (radioMonth.isSelected()){
            for (int i = 0; i < tempAppointmentList.size(); i++){
                if (tempAppointmentList.get(i).getStart().isAfter(ChronoLocalDateTime.from(now)) && tempAppointmentList.get(i).getStart().isBefore(ChronoLocalDateTime.from(monthAfter))){
                    matchingAppointments.add(tempAppointmentList.get(i));
                    appointmentsTable.setItems(matchingAppointments);
                }
            }
        } else if (radioWeek.isSelected()){
            for (int i = 0; i < tempAppointmentList.size(); i++){
                if (tempAppointmentList.get(i).getStart().isAfter(ChronoLocalDateTime.from(now)) && tempAppointmentList.get(i).getStart().isBefore(ChronoLocalDateTime.from(weekAfter))){
                    matchingAppointments.add(tempAppointmentList.get(i));
                    appointmentsTable.setItems(matchingAppointments);
                }
            }
        } else if (radioAll.isSelected()){
            matchingAppointments = tempAppointmentList;
            appointmentsTable.setItems(matchingAppointments);
        }

        if (matchingAppointments.size() == 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No matching appointments");
            alert.setHeaderText("No appointments were found under the given time frame.");

            if (alert.showAndWait().get() == ButtonType.OK){
                return;
            } else{
                return;
            }
        }


    }


    public void switchToCustomReport(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AppointmentsCustomForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    public void switchToAddAppointmentsForm(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AppointmentsAddForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToUpdateAppointmentsForm(ActionEvent event) throws IOException, SQLException {
        int selectedIndex = appointmentsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1){
            return;
        }

        appointmentsTable.setItems(JDBC.getAllAppointments());
        appointmentsTable.getSelectionModel().select(selectedIndex);
        Appointment selected = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();


        // These lines change the UTC time to Local time
        LocalDateTime tempStartTime = selected.getStart();
        LocalDateTime tempEndTime = selected.getEnd();
        selected.setStart(DifferentTimes.toLocal(tempStartTime));
        selected.setEnd(DifferentTimes.toLocal(tempEndTime));


        AppointmentsUpdateController.inputAppointment = selected; // Send selected to the update form

        root = FXMLLoader.load(getClass().getResource("AppointmentsUpdateForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToNavigationForm(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("NavigationForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}