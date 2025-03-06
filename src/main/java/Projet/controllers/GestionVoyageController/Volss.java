package Projet.controllers.GestionVoyageController;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class Volss {


    @FXML
    private ComboBox<String> departureCity;

    @FXML
    private ComboBox<String> arrivalCity;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox<String> tripType;

    @FXML
    public void initialize() {
        // Initialize combo boxes with sample data
        departureCity.getItems().addAll("Paris", "London", "New York", "Tokyo");
        arrivalCity.getItems().addAll("Rome", "Dubai", "Singapore", "Sydney");
        tripType.getItems().addAll("ALLER - RETOUR", "ALLER SIMPLE");

        // Set default values
        dateField.setValue(LocalDate.now());
    }

    @FXML
    private void handleSearch() {
        // Add search logic here
        System.out.println("Searching for flights...");
        System.out.println("From: " + departureCity.getValue());
        System.out.println("To: " + arrivalCity.getValue());
        System.out.println("Date: " + dateField.getValue());
        System.out.println("Trip Type: " + tripType.getValue());
    }
}
