package sample;

import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * First page to open when the program is ran.
 * Users log-in here.
 */
public class LogInController {

    private Locale locale;
    private String language;
    private boolean isFrench = false;

    private Parent root;
    private Scene scene;
    private Stage stage;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @FXML
    TextField userNameTextField;
    @FXML
    TextField passwordTextField;

    @FXML
    Label userLocationField;
    @FXML
    Label userIDErrorLabel;
    @FXML
    Label passwordErrorLabel;
    @FXML
    Label tzConstLabel;
    @FXML
    Label loginConstLabel;

    @FXML
    Button logInButton;

    private ObservableList<Appointment> tempAppointmentList;

    /**
     * Displays the current Location/Timezone of the user.
     * Changes text to French if the user's system is in French.
     * @throws SQLException
     */
    public void initialize() throws SQLException {
        ZoneId userZone = ZoneId.systemDefault();
        userLocationField.setText(String.valueOf(userZone));

        // Check if French
        locale = Locale.getDefault();
        language = locale.getDisplayLanguage();
        if (language.equals("français")){
            isFrench = true;
        }

        // Change text if French
        if (isFrench){
            loginConstLabel.setText("Connexion");
            loginConstLabel.setLayoutX(140);
            tzConstLabel.setText("Emplacement du fuseau horaire actuel");
            userNameTextField.setPromptText("Nom d'utilisateur");
            passwordTextField.setPromptText("le mot de passe");
            logInButton.setText("Connexion");
        }


        tempAppointmentList = JDBC.getAllAppointments();

        for (int i = 0; i < tempAppointmentList.size(); i++){
            tempAppointmentList.get(i).setContactID(ComboBoxCollections.comboBoxContacts.get(Integer.valueOf(tempAppointmentList.get(i).getContactID()) - 1));

            // These lines change the UTC time to Local time
            LocalDateTime tempStartTime = tempAppointmentList.get(i).getStart();
            LocalDateTime tempEndTime = tempAppointmentList.get(i).getEnd();
            tempAppointmentList.get(i).setStart(DifferentTimes.toLocal(tempStartTime));
            tempAppointmentList.get(i).setEnd(DifferentTimes.toLocal(tempEndTime));
        }
    }


    /**
     * Alert box with appointment information if upcoming appointments are found within 15 minutes.
     * Only chooses appointments related to the current user.
     *
     * Alert box saying no appointments if no upcoming appointments are found within 15 minutes.
     */
    public void alertIfAppointmentsUpcoming() {
        // Alert Box if there is an upcoming appointment

        boolean idMatch = false;

        Appointment currApp;
        String upcomingAppointments = "You have upcoming appointments: \n";

        for (int i = 0; i < tempAppointmentList.size(); i++) {
            currApp = tempAppointmentList.get(i);
            if (currApp.getStart().isAfter(LocalDateTime.now()) && currApp.getStart().isBefore(LocalDateTime.now().plusMinutes(15)) && currApp.getUserID() == JDBC.currentUserID) {
                idMatch = true;
                upcomingAppointments += "ID: " + String.valueOf(currApp.getID()) + " Date: " + currApp.getStart().toLocalDate() + " Start: " + currApp.getStart().toLocalTime() + " End: " + currApp.getEnd().toLocalTime() + "\n";
            }
        }

        // Alert appointments or Alert no appointments
        if (idMatch) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Appointment Alert");
            alert.setHeaderText(upcomingAppointments);

            if (alert.showAndWait().get() == ButtonType.OK) {
                return;
            } else {
                return;
            }
        } else {
            Alert noAlert = new Alert(Alert.AlertType.CONFIRMATION);
            noAlert.setTitle("Appointment Alert");
            noAlert.setHeaderText("You have no upcoming appointments within 15 minutes.");

            if (noAlert.showAndWait().get() == ButtonType.OK) {
                return;
            } else {
                return;
            }
        }

    }

    /**
     * Logs log-in activity to login_activity.txt in the root folder.
     * @param inputSuccess
     * @throws IOException
     */
    public void writeLogInAttempt(boolean inputSuccess) throws IOException {
        FileWriter writer = new FileWriter("login_activity.txt", true);
        String log = "Log-in Attempt: \n";

        log += LocalDateTime.now().format(dateTimeFormatter).toString() + "  Username input: " + userNameTextField.getText() + " \n";
        if (inputSuccess){
            log += "SUCCESS\n\n";
        } else {
            log += "FAILED\n\n";
        }

        writer.write(log);
        writer.close();
    }


    /**
     * Checks if the log-in information matches a user.
     * If log-in is good, sends user to the navigation page.
     * If log-in is bad, displays appropriate error messages.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void attemptLogIn(ActionEvent event) throws IOException, SQLException {
        ObservableList<User> allUsers = JDBC.getAllUsers();
        String nameInput = userNameTextField.getText();
        String passInput = passwordTextField.getText();
        boolean userGood = false;
        boolean passGood = false;

        // Resetting the error texts
        userIDErrorLabel.setText("");
        passwordErrorLabel.setText("");

        for (int i = 0; i < allUsers.size(); i++){

            if (allUsers.get(i).getName().equals(nameInput) && allUsers.get(i).getPassword().equals(passInput)){
                switchToNav(event);
                JDBC.currentUserID = allUsers.get(i).getID(); // This will keep track of who is currently logged in.
                writeLogInAttempt(true);
                alertIfAppointmentsUpcoming();
                return;
            }
            if (allUsers.get(i).getName().equals(nameInput)){
                userGood = true;
            }
            if (allUsers.get(i).getPassword().equals(passInput)){
                passGood = true;
            }
        }

        if (!userGood){
            if (!isFrench){
                userIDErrorLabel.setText("Username not found");
            } else {
                userIDErrorLabel.setText("Nom d'utilisateur non trouvé");
            }

        } else if (!passGood){
            if (!isFrench){
                passwordErrorLabel.setText("Password is incorrect");
            } else {
                passwordErrorLabel.setText("Le mot de passe est incorrect");
            }
        }
        writeLogInAttempt(false); // Not a successful log-in
    }


    private void switchToNav(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("NavigationForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



}
