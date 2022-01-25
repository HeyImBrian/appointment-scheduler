package sample;
import helper.JDBC;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;


/**
 * @author Brian St. Germain
 */
public class Main extends Application {
    /**
     * The start method is used to open the starting page.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("LogInForm.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    /**
     * In the main method, the database connection is opened and closed.
     * The ComboBoxCollections are also being set up, which fills many arrays with proper values.
     * By filling the comboBoxCollections here, we can ensure that it isn't filled twice, causing duplicates.
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        //Locale.setDefault(new Locale("fr")); // Used to set language to French.
        JDBC.openConnection();
        ComboBoxCollections.setup();  // Sets up the comboBoxes that have predetermined selection choices
        launch(args);
        JDBC.closeConnection();
    }
}
