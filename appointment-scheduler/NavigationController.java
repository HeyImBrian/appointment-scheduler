package sample;


import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * This page acts as a simple navigation menu.
 * The user is able to choose between the customer page and appointment page.
 * This helps prevent a user from being overwhelmed by information.
 */
public class NavigationController {


    private Parent root;
    private Scene scene;
    private Stage stage;


    @FXML
    Button customersButton;
    @FXML
    Button appointmentsButton;


    /**
     * Switches to the customers page.
     * @param event
     * @throws IOException
     */
    public void switchToCustomers(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("CustomersForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switches to the appointments page.
     * @param event
     * @throws IOException
     */
    public void switchToAppointments(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("AppointmentsForm.fxml"));
        scene = new Scene(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
