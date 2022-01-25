package sample;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


/**
 * This is a form for adding customers.
 */
public class CustomersAddController {
    private Parent root;
    private Scene scene;
    private Stage stage;

    @FXML
    TextField fieldID;
    @FXML
    TextField fieldName;
    @FXML
    TextField fieldAddress;
    @FXML
    TextField fieldPostalCode;
    @FXML
    TextField fieldPhone;
    @FXML
    Button buttonSave;
    @FXML
    Button buttonCancel;
    @FXML
    Label labelError;
    @FXML
    ComboBox comboBoxCountry;
    @FXML
    ComboBox comboBoxDivision;


    /**
     * This function is ran when this page is loaded.
     * Sets up the combo box for countries.
     */
    public void initialize(){
        comboBoxCountry.setItems(FXCollections.observableArrayList(ComboBoxCollections.comboBoxCountries));
    }

    /**
     * This is called when a country is selected.
     * Sets up the division box based on which country is chosen.
     */
    public void countryBoxSelected(){
        if (comboBoxCountry.getValue().equals("U.S")){
            comboBoxDivision.setItems(FXCollections.observableArrayList(ComboBoxCollections.comboBoxUSDivisions));
        }
        if (comboBoxCountry.getValue().equals("UK")){
            comboBoxDivision.setItems(FXCollections.observableArrayList(ComboBoxCollections.comboBoxUKDivisions));
        }
        if (comboBoxCountry.getValue().equals("Canada")){
            comboBoxDivision.setItems(FXCollections.observableArrayList(ComboBoxCollections.comboBoxCanadaDivisions));
        }
    }


    /**
     * Saves the current information to the database if it passes the error check.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void savePressed(ActionEvent event) throws IOException, SQLException {
        if (errorCheck()){
            return;
        }
        int division = ComboBoxCollections.returnDivisionFromName((String) comboBoxDivision.getValue());
        Customer tempCustomer = new Customer(fieldName.getText(), fieldAddress.getText(), fieldPostalCode.getText(), fieldPhone.getText(), division);
        JDBC.addCustomer(tempCustomer);

        root = FXMLLoader.load(getClass().getResource("CustomersForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



    public void cancelPressed(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("CustomersForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Checks for unacceptable inputs, and updates the error text accordingly.
     * @return true if an error is found.
     */
    public boolean errorCheck(){ // Returns true if error is found.
        boolean errorFound = false;
        String errorText = "Exception:\n";


        if (fieldName.getLength() == 0){
            errorFound = true;
            errorText += "Name required\n";
        }
        if (fieldAddress.getLength() == 0){
            errorFound = true;
            errorText += "Address required\n";
        }
        if (fieldPostalCode.getLength() == 0){
            errorFound = true;
            errorText += "Postal Code required\n";
        }
        if (fieldPhone.getLength() == 0){
            errorFound = true;
            errorText += "Phone number required\n";
        }

        if (comboBoxCountry.getValue() == null){
            errorFound = true;
            errorText += "Country required\n";
        }
        if (comboBoxDivision.getValue() == null){
            errorFound = true;
            errorText += "Division required\n";
        }

        if (errorFound){
            labelError.setText(errorText);
            return true;
        } else {
            return false;
        }
    }




}
