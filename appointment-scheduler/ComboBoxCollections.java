package sample;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Used to have all combo box information in one location.
 */
public abstract class ComboBoxCollections {

    // Used for customers
    public static ArrayList<String> comboBoxCountries = new ArrayList<String>();
    public static ArrayList<String> comboBoxUKDivisions = new ArrayList<String>();
    public static ArrayList<String> comboBoxUSDivisions = new ArrayList<String>();
    public static ArrayList<String> comboBoxCanadaDivisions = new ArrayList<String>();

    // Used for appointments
    public static ArrayList<String> comboBoxAppointmentTypes = new ArrayList<String>();
    public static ArrayList<String> comboBoxContacts = new ArrayList<String>();


    /**
     * Given a division name, returns the division ID
     * @param inputName
     * @return divisionID
     */
    public static int returnDivisionFromName(String inputName){
        for (int i = 0; i < lookUpDivision.length; i++){
            if (lookUpDivision[i][1].equals(inputName)){
                return Integer.valueOf(lookUpDivision[i][0]);
            }
        }
        return -1;
    }

    /**
     * Given a divisionID, returns division name.
     * @param inputDivision
     * @return division name
     */
    public static String returnNameFromDivision(int inputDivision){
        for (int i = 0; i < lookUpDivision.length; i++){
            if (lookUpDivision[i][0].equals(String.valueOf(inputDivision))){
                return lookUpDivision[i][1];
            }
        }
        return "";
    }


    /**
     * Sets up all of the lists.
     * This way it doesn't all have to be in one line.
     * @throws SQLException
     */
    public static void setup() throws SQLException {
        comboBoxCountries.add("U.S");
        comboBoxCountries.add("Canada");
        comboBoxCountries.add("UK");

        comboBoxUKDivisions.add("England");
        comboBoxUKDivisions.add("Wales");
        comboBoxUKDivisions.add("Scotland");
        comboBoxUKDivisions.add("Northern Ireland");

        comboBoxCanadaDivisions.add("Northwest Territories");
        comboBoxCanadaDivisions.add("Alberta");
        comboBoxCanadaDivisions.add("British Columbia");
        comboBoxCanadaDivisions.add("Manitoba");
        comboBoxCanadaDivisions.add("New Brunswick");
        comboBoxCanadaDivisions.add("Nova Scotia");
        comboBoxCanadaDivisions.add("Prince Edward Island");
        comboBoxCanadaDivisions.add("Ontario");
        comboBoxCanadaDivisions.add("Québec");
        comboBoxCanadaDivisions.add("Saskatchewan");
        comboBoxCanadaDivisions.add("Nunavut");
        comboBoxCanadaDivisions.add("Yukon");
        comboBoxCanadaDivisions.add("Newfoundland and Labrador");

        comboBoxUSDivisions.add("Alabama");
        comboBoxUSDivisions.add("Arizona");
        comboBoxUSDivisions.add("Arkansas");
        comboBoxUSDivisions.add("California");
        comboBoxUSDivisions.add("Colorado");
        comboBoxUSDivisions.add("Connecticut");
        comboBoxUSDivisions.add("Delaware");
        comboBoxUSDivisions.add("District of Columbia");
        comboBoxUSDivisions.add("Florida");
        comboBoxUSDivisions.add("Georgia");
        comboBoxUSDivisions.add("Idaho");
        comboBoxUSDivisions.add("Illinois");
        comboBoxUSDivisions.add("Indiana");
        comboBoxUSDivisions.add("Iowa");
        comboBoxUSDivisions.add("Kansas");
        comboBoxUSDivisions.add("Kentucky");
        comboBoxUSDivisions.add("Louisiana");
        comboBoxUSDivisions.add("Maine");
        comboBoxUSDivisions.add("Maryland");
        comboBoxUSDivisions.add("Massachusetts");
        comboBoxUSDivisions.add("Michigan");
        comboBoxUSDivisions.add("Minnesota");
        comboBoxUSDivisions.add("Mississippi");
        comboBoxUSDivisions.add("Missouri");
        comboBoxUSDivisions.add("Montana");
        comboBoxUSDivisions.add("Nebraska");
        comboBoxUSDivisions.add("Nevada");
        comboBoxUSDivisions.add("New Hampshire");
        comboBoxUSDivisions.add("New Jersey");
        comboBoxUSDivisions.add("New Mexico");
        comboBoxUSDivisions.add("New York");
        comboBoxUSDivisions.add("North Carolina");
        comboBoxUSDivisions.add("North Dakota");
        comboBoxUSDivisions.add("Ohio");
        comboBoxUSDivisions.add("Oklahoma");
        comboBoxUSDivisions.add("Oregon");
        comboBoxUSDivisions.add("Pennsylvania");
        comboBoxUSDivisions.add("Rhode Island");
        comboBoxUSDivisions.add("South Carolina");
        comboBoxUSDivisions.add("South Dakota");
        comboBoxUSDivisions.add("Tennessee");
        comboBoxUSDivisions.add("Texas");
        comboBoxUSDivisions.add("Utah");
        comboBoxUSDivisions.add("Vermont");
        comboBoxUSDivisions.add("Virginia");
        comboBoxUSDivisions.add("Washington");
        comboBoxUSDivisions.add("West Virginia");
        comboBoxUSDivisions.add("Wisconsin");
        comboBoxUSDivisions.add("Wyoming");
        comboBoxUSDivisions.add("Hawaii");
        comboBoxUSDivisions.add("Alaska");

        comboBoxAppointmentTypes.add("Planning Session");
        comboBoxAppointmentTypes.add("De-Briefing");
        comboBoxAppointmentTypes.add("Pizza Party!");
        ObservableList<Contact> tempContacts = JDBC.getAllContacts();
        for (int i = 0; i < tempContacts.size(); i++){
            comboBoxContacts.add(tempContacts.get(i).getName());
        }

    }

    /**
     * Used for the returnDivisionFromName and returnNameFromDivision functions.
     */
    public static String[][] lookUpDivision = {
                    {"1",	"Alabama"},
                    {"2",	"Arizona"},
                    {"3",	"Arkansas"},
                    {"4",	"California"},
                    {"5",	"Colorado"},
                    {"6",	"Connecticut"},
                    {"7",	"Delaware"},
                    {"8",	"District of Columbia"},
                    {"9",	"Florida"},
                    {"10",	"Georgia"},
                    {"11",	"Idaho"},
                    {"12",	"Illinois"},
                    {"13",	"Indiana"},
                    {"14",	"Iowa"},
                    {"15",	"Kansas"},
                    {"16",	"Kentucky"},
                    {"17",	"Louisiana"},
                    {"18",	"Maine"},
                    {"19",	"Maryland"},
                    {"20",	"Massachusetts"},
                    {"21",	"Michigan"},
                    {"22",	"Minnesota"},
                    {"23",	"Mississippi"},
                    {"24",	"Missouri"},
                    {"25",	"Montana"},
                    {"26",	"Nebraska"},
                    {"27",	"Nevada"},
                    {"28",	"New Hampshire"},
                    {"29",	"New Jersey"},
                    {"30",	"New Mexico"},
                    {"31",	"New York"},
                    {"32",	"North Carolina"},
                    {"33",	"North Dakota"},
                    {"34",	"Ohio"},
                    {"35",	"Oklahoma"},
                    {"36",	"Oregon"},
                    {"37",	"Pennsylvania"},
                    {"38",	"Rhode Island"},
                    {"39",	"South Carolina"},
                    {"40",	"South Dakota"},
                    {"41",	"Tennessee"},
                    {"42",	"Texas"},
                    {"43",	"Utah"},
                    {"44",	"Vermont"},
                    {"45",	"Virginia"},
                    {"46",	"Washington"},
                    {"47",	"West Virginia"},
                    {"48",	"Wisconsin"},
                    {"49",	"Wyoming"},
                    {"52",	"Hawaii"},
                    {"54",	"Alaska"},

                    {"60",	"Northwest Territories"},
                    {"61",	"Alberta"},
                    {"62",	"British Columbia"},
                    {"63",	"Manitoba"},
                    {"64",	"New Brunswick"},
                    {"65",	"Nova Scotia"},
                    {"66",	"Prince Edward Island"},
                    {"67",	"Ontario"},
                    {"68",	"Québec"},
                    {"69",	"Saskatchewan"},
                    {"70",	"Nunavut"},
                    {"71",	"Yukon"},
                    {"72",	"Newfoundland and Labrador"},

                    {"101",	"England"},
                    {"102",	"Wales"},
                    {"103",	"Scotland"},
                    {"104",	"Northern Ireland"}
    };
}