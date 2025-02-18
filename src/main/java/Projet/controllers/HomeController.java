package Projet.controllers;

import Projet.API.FlightApiClient;
import Projet.entities.Vol;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class HomeController {
    @FXML private ComboBox<String> departureCity;
    @FXML private ComboBox<String> arrivalCity;
    @FXML private DatePicker dateField;
    @FXML private DatePicker dateField1;
    @FXML private ComboBox<String> tripType;
    @FXML private VBox resultsContainer;
    @FXML private Button searchButton;

    private final Map<String, String> cityToIata = new HashMap<>();

    @FXML
    public void initialize() {
        initializeCityMappings();
        setupComboBoxes();
        setupDatePickers();

        if (searchButton != null) {
            searchButton.setOnAction(event -> searchFlights());
        } else {
            System.out.println("ERROR: searchButton is NULL");
        }
    }

    private void initializeCityMappings() {
        cityToIata.put("Tunis", "TUN");
        cityToIata.put("Dubai", "DXB");
        cityToIata.put("Paris", "CDG");
        cityToIata.put("Londres", "LHR");
        cityToIata.put("Munich", "MUC");
        cityToIata.put("Tokyo", "HND");
        cityToIata.put("Casablanca", "CMN");
        cityToIata.put("Le Caire", "CAI");
    }

    private void setupComboBoxes() {
        if (departureCity != null) {
            departureCity.setItems(FXCollections.observableArrayList(cityToIata.keySet()));
        }

        if (arrivalCity != null) {
            arrivalCity.setItems(FXCollections.observableArrayList(cityToIata.keySet()));
        }

        if (tripType != null) {
            tripType.setItems(FXCollections.observableArrayList("ALLER SIMPLE", "ALLER-RETOUR"));
        }
    }

    private void setupDatePickers() {
        if (dateField != null) {
            dateField.setValue(LocalDate.now());
        }

        if (dateField1 != null) {
            dateField1.setValue(LocalDate.now().plusDays(7));
        }

        if (dateField != null) {
            dateField.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isBefore(LocalDate.now()));
                }
            });
        }

        if (dateField1 != null) {
            dateField1.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || (dateField != null && date.isBefore(dateField.getValue())));
                }
            });
        }
    }
    @FXML
    private void searchFlights() {
        if (!validateSearchInputs()) {
            return;
        }

        String depIata = cityToIata.get(departureCity.getValue());
        String arrIata = cityToIata.get(arrivalCity.getValue());

        try {
            System.out.println("Fetching flights from " + depIata + " to " + arrIata);
            String jsonResponse = FlightApiClient.getFlights(depIata, arrIata);
            System.out.println("API Response: " + jsonResponse);
            displayFlightResults(jsonResponse);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch flights: " + e.getMessage());
        }
    }

    private boolean validateSearchInputs() {
        if (departureCity.getValue() == null || arrivalCity.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select both departure and arrival cities");
            return false;
        }

        if (dateField.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a departure date");
            return false;
        }

        if (tripType.getValue() != null &&
                tripType.getValue().equals("ALLER-RETOUR") &&
                dateField1.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please select a return date for round-trip flights");
            return false;
        }

        return true;
    }

    private void displayFlightResults(String jsonResponse) {
        resultsContainer.getChildren().clear();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);
            JsonNode flightsNode = rootNode.path("data");

            if (!flightsNode.isArray() || flightsNode.size() == 0) {
                Label noFlights = new Label("No flights found for the selected route");
                noFlights.getStyleClass().add("no-results");
                resultsContainer.getChildren().add(noFlights);
                return;
            }

            Label resultsHeader = new Label(String.format("Résultat de recherche: %d Vols", flightsNode.size()));
            resultsHeader.getStyleClass().add("results-header");
            resultsContainer.getChildren().add(resultsHeader);

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");

            for (JsonNode flightNode : flightsNode) {
                VBox flightItem = createFlightItemPane(flightNode, inputFormat, outputFormat);
                resultsContainer.getChildren().add(flightItem);
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to process flight results: " + e.getMessage());
        }
    }

    private VBox createFlightItemPane(JsonNode flightNode, SimpleDateFormat inputFormat, SimpleDateFormat outputFormat) throws Exception {
        VBox flightItem = new VBox();
        flightItem.getStyleClass().add("flight-item");

        HBox content = new HBox(20);
        content.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        VBox details = new VBox();
        details.getStyleClass().add("flight-details");

        Label airline = new Label(flightNode.path("airline").path("name").asText());
        airline.getStyleClass().add("airline-label");

        String depTime = outputFormat.format(inputFormat.parse(flightNode.path("departure").path("scheduled").asText()));
        String arrTime = outputFormat.format(inputFormat.parse(flightNode.path("arrival").path("scheduled").asText()));
        Label time = new Label(depTime + " - " + arrTime);
        time.getStyleClass().add("time-label");

        details.getChildren().addAll(airline, time);

        VBox priceContainer = new VBox();
        priceContainer.getStyleClass().add("price-container");
        priceContainer.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        HBox.setHgrow(priceContainer, Priority.ALWAYS);

        int randomPrice = (int) (Math.random() * 1000 + 500);
        Label price = new Label(randomPrice + " DT");
        price.getStyleClass().add("price-value");

        Button reserveButton = new Button("RESERVER");
        reserveButton.getStyleClass().add("reserve-button");
        reserveButton.setOnAction(event -> handleReservation(flightNode));

        priceContainer.getChildren().addAll(price, reserveButton);

        content.getChildren().addAll(details, priceContainer);
        flightItem.getChildren().add(content);

        return flightItem;
    }

    private void handleReservation(JsonNode flightNode) {
        showAlert(Alert.AlertType.INFORMATION, "Reservation",
                "Processing reservation for flight " + flightNode.path("flight").path("number").asText());
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
