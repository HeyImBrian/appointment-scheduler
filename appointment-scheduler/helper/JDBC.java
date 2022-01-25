package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Appointment;
import sample.Contact;
import sample.Customer;
import sample.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "example username"; // Username
    private static String password = "example password"; // Password
    public static Connection connection;  // Connection Interface
    public static int currentUserID;


    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }


    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }


    private static Customer convertRowToCustomer(ResultSet input) throws SQLException {
        int ID = input.getInt("Customer_ID");
        String name = input.getString("Customer_Name");
        String address = input.getString("Address");
        String postal = input.getString("Postal_Code");
        String phone = input.getString("Phone");
        int divisionID = input.getInt("Division_ID");

        Customer tempCustomer = new Customer(name, address, postal, phone, divisionID);
        tempCustomer.setID(ID);
        return tempCustomer;
    }


    private static Appointment convertRowToAppointment(ResultSet input) throws SQLException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        int ID = input.getInt(1);
        String title = input.getString(2);
        String description = input.getString(3);
        String location = input.getString(4);
        String type = input.getString(5);
        LocalDateTime start = input.getObject(6, LocalDateTime.class);
        LocalDateTime end = input.getObject(7, LocalDateTime.class);
        int customerID = input.getInt(12);
        int userID = input.getInt(13);
        String contactID = input.getString(14);

        Appointment tempAppointment = new Appointment(title, description, location, type, start, end, customerID, userID, contactID);
        tempAppointment.setID(ID);
        return tempAppointment;
    }


    private static User convertRowToUser(ResultSet input) throws SQLException{
        int ID = input.getInt(1);
        String name = input.getString(2);
        String password = input.getString(3);

        User tempUser = new User(ID, name, password);
        return tempUser;
    }


    private static Contact convertRowToContact(ResultSet input) throws SQLException{
        int ID = input.getInt(1);
        String name = input.getString(2);
        String email = input.getString(3);

        Contact tempContact = new Contact(ID, name, email);
        return tempContact;
    }


    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        Statement statement = null;
        ResultSet results = null;

        try {
            statement = connection.createStatement();
            results = statement.executeQuery("select * from client_schedule.customers");

            while (results.next()){
                Customer tempCustomer = convertRowToCustomer(results);
                customers.add(tempCustomer);
            }

            return customers;


        } finally {
            statement.close();
            results.close();
        }
    }


    public static ObservableList<Appointment> getAllAppointments() throws SQLException{
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        Statement statement = null;
        ResultSet results = null;

        try {
            statement = connection.createStatement();
            results = statement.executeQuery("select * from client_schedule.appointments");

            while (results.next()){
                Appointment tempAppointment = convertRowToAppointment(results);
                appointments.add(tempAppointment);
            }

            return appointments;

        } finally {
            statement.close();
            results.close();

        }
    }


    public static ObservableList<User> getAllUsers() throws SQLException{
        ObservableList<User> users = FXCollections.observableArrayList();

        Statement statement = null;
        ResultSet results = null;

        try {
            statement = connection.createStatement();
            results = statement.executeQuery("select * from client_schedule.users");

            while (results.next()){
                User tempUser = convertRowToUser(results);
                users.add(tempUser);
            }

            return users;

        } finally {
            statement.close();
            results.close();

        }
    }


    public static ObservableList<Contact> getAllContacts() throws SQLException{
        ObservableList<Contact> contacts = FXCollections.observableArrayList();

        Statement statement = null;
        ResultSet results = null;

        try {
            statement = connection.createStatement();
            results = statement.executeQuery("select * from client_schedule.contacts");

            while (results.next()){
                Contact tempContacts = convertRowToContact(results);
                contacts.add(tempContacts);
            }

            return contacts;

        } finally {
            statement.close();
            results.close();

        }
    }


    public static Customer selectCustomerByID(int inputID) throws SQLException {
        ObservableList<Customer> customers = getAllCustomers();
        for (int i = 0; i < customers.size(); i++){
            if (customers.get(i).getID() == inputID){
                return customers.get(i);
            }
        }
        return customers.get(-1); // Will throw an error
    }


    public static Appointment selectAppointmentByID(int inputID) throws SQLException {
        ObservableList<Appointment> appointments = getAllAppointments();
        for (int i = 0; i < appointments.size(); i++){
            if (appointments.get(i).getID() == inputID){
                return appointments.get(i);
            }
        }
        return appointments.get(-1); // Will throw an error
    }


    public static void deleteCustomerByID(int inputID) throws SQLException {
        ObservableList<Customer> customers = getAllCustomers();
        ObservableList<Appointment> appointments = getAllAppointments();
        Statement statement = null;


        try {
            Customer tempCustomer = selectCustomerByID(inputID);

            // Deletes appointments associated with the customer
            for (int i = 0; i < appointments.size(); i++){
                if (appointments.get(i).getCustomerID() == tempCustomer.getID()){
                    statement = connection.createStatement();
                    statement.executeUpdate("DELETE FROM client_schedule.appointments WHERE Customer_ID = " + String.valueOf(inputID));
                }
            }

            // After appointments are deleted, we can delete the customer
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM client_schedule.customers WHERE Customer_ID = " + String.valueOf(inputID));


        } catch (Exception e) {
            System.out.println("Error deleteCustomerByID");
            return;
        } finally {
            statement.close();
        }
    }


    public static void deleteAppointmentByID(int inputID) throws SQLException {
        ObservableList<Appointment> appointments = getAllAppointments();
        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM client_schedule.appointments WHERE Appointment_ID = " + String.valueOf(inputID));

        } catch (Exception e) {
            System.out.println("Error deleteAppointmentByID");
            return;

        } finally {
            statement.close();

        }

    }


    public static void addCustomer(Customer inputCustomer) throws SQLException {
        String insertSQL = "INSERT INTO client_schedule.customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(insertSQL);

            preparedStatement.setString(1, inputCustomer.getName());
            preparedStatement.setString(2, inputCustomer.getAddress());
            preparedStatement.setString(3, inputCustomer.getPostal());
            preparedStatement.setString(4, inputCustomer.getPhone());
            preparedStatement.setInt(5, inputCustomer.getDivisionID());

            preparedStatement.executeUpdate();
        } finally {
            preparedStatement.close();
        }
    }


    public static void addAppointment(Appointment inputAppointment) throws SQLException {
        String insertSQL = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(insertSQL);

            preparedStatement.setString(1, inputAppointment.getTitle());
            preparedStatement.setString(2, inputAppointment.getDescription());
            preparedStatement.setString(3, inputAppointment.getLocation());
            preparedStatement.setString(4, inputAppointment.getType());
            preparedStatement.setObject(5,inputAppointment.getStart());
            preparedStatement.setObject(6, inputAppointment.getEnd());
            preparedStatement.setInt(7, inputAppointment.getCustomerID());
            preparedStatement.setInt(8, inputAppointment.getUserID());
            preparedStatement.setString(9, inputAppointment.getContactID());

            preparedStatement.executeUpdate();
        } finally {
            preparedStatement.close();
        }
    }


    public static void updateCustomer(Customer inputCustomer) throws SQLException {
        String insertSQL = "UPDATE client_schedule.customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = " + inputCustomer.getID();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(insertSQL);

            preparedStatement.setString(1, inputCustomer.getName());
            preparedStatement.setString(2, inputCustomer.getAddress());
            preparedStatement.setString(3, inputCustomer.getPostal());
            preparedStatement.setString(4, inputCustomer.getPhone());
            preparedStatement.setInt(5, inputCustomer.getDivisionID());

            preparedStatement.executeUpdate();
        } finally {
            preparedStatement.close();
        }
    }


    public static void updateAppointment(Appointment inputAppointment) throws SQLException {
        String insertSQL = "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = " + inputAppointment.getID();
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(insertSQL);

            preparedStatement.setString(1, inputAppointment.getTitle());
            preparedStatement.setString(2, inputAppointment.getDescription());
            preparedStatement.setString(3, inputAppointment.getLocation());
            preparedStatement.setString(4, inputAppointment.getType());
            preparedStatement.setObject(5,inputAppointment.getStart());
            preparedStatement.setObject(6, inputAppointment.getEnd());
            preparedStatement.setInt(7, inputAppointment.getCustomerID());
            preparedStatement.setInt(8, inputAppointment.getUserID());
            preparedStatement.setString(9, inputAppointment.getContactID());

            preparedStatement.executeUpdate();
        } finally {
            preparedStatement.close();
        }
    }
}