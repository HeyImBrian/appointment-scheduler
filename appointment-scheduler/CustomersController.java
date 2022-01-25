package sample;


import helper.JDBC;
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

/**
 * This is the main customer page that shows a table of customers.
 * This page leads to other related pages.
 */
public class CustomersController {


    private Parent root;
    private Scene scene;
    private Stage stage;


    @FXML
    Button addButton;
    @FXML
    Button updateButton;
    @FXML
    Button deleteButton;
    @FXML
    Button cancelButton;












    @FXML
    TableView customersTable;
    @FXML
    TableColumn tableID;
    @FXML
    TableColumn tableName;
    @FXML
    TableColumn tableAddress;
    @FXML
    TableColumn tablePostal;
    @FXML
    TableColumn tablePhone;
    @FXML
    TableColumn tableDivisionID;


    /**
     * The initialize function gets called whenever this page is opened.
     * Fills the customer table with information from the database.
     * @throws SQLException
     */
    public void initialize() throws SQLException {
        customersTable.setItems(JDBC.getAllCustomers());

        tableID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tableAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        tablePostal.setCellValueFactory(new PropertyValueFactory<>("Postal"));
        tablePhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        tableDivisionID.setCellValueFactory(new PropertyValueFactory<>("DivisionID"));
    }


    /**
     * When the delete button is pressed, this function is called.
     * Deletes a selected customer from the database.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void delete(ActionEvent event) throws IOException, SQLException {
        Customer selectedCustomer = (Customer) customersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null){
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Item");
        alert.setHeaderText("Are you sure?");

        if (alert.showAndWait().get() == ButtonType.OK){
            JDBC.deleteCustomerByID(selectedCustomer.getID());
            customersTable.setItems(JDBC.getAllCustomers());
        } else{
            return;
        }

    }

    /**
     * When the add button is pressed, this function is called.
     * Switches to the add customer page.
     * @param event
     * @throws IOException
     */
    public void switchToAddCustomersForm(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("CustomersAddForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * When the update button is pressed, this function is called.
     * Passes the current selected customer to the update page's class.
     * Switches to the update customer page.
     * @param event
     * @throws IOException
     */
    public void switchToUpdateCustomersForm(ActionEvent event) throws IOException {
        Customer selected = (Customer) customersTable.getSelectionModel().getSelectedItem();
        if (selected == null){
            return;
        }
        CustomersUpdateController.inputCustomer = selected;

        root = FXMLLoader.load(getClass().getResource("CustomersUpdateForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * When the cancel button is pressed, this function is called.
     * Switches to the navigation page.
     * @param event
     * @throws IOException
     */
    public void switchToNavigationForm(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("NavigationForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}