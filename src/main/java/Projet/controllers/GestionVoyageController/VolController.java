package Projet.controllers.GestionVoyageController;

import Projet.entities.Vol;
import Projet.entities.Voyage;
import Projet.API.FlightApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.hc.core5.http.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class VolController {

    @FXML
    private TableView<Vol> volTable;

    @FXML
    private TableColumn<Vol, String> airlineColumn;

    @FXML
    private TableColumn<Vol, Integer> flightNumberColumn;

    @FXML
    private TableColumn<Vol, Date> departureDateColumn;

    @FXML
    private TableColumn<Vol, Date> arrivalDateColumn;
    @FXML
    private TableColumn<Vol, String> durationColumn;
    @FXML
    private TableColumn<Vol, Integer> priceColumn;
    @FXML
    private TableColumn<Vol, String> typeColumn;

    private Voyage currentVoyage;

    @FXML
    public void initialize() {
        // Set up the table columns
        airlineColumn.setCellValueFactory(new PropertyValueFactory<>("airLine"));
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateDepart"));
        arrivalDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateArrival"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("dureeVol"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("prixVol"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeVol"));
    }

    public void setVoyage(Voyage voyage) {
        this.currentVoyage = voyage;
        loadFlightsForVoyage();
    }

    private void loadFlightsForVoyage() {
        if (currentVoyage != null) {
            volTable.getItems().clear();

            String departureIata = getIataCode(currentVoyage.getDepart());
            String arrivalIata = getIataCode(currentVoyage.getDestination());

            if (departureIata.isEmpty() || arrivalIata.isEmpty()) {
                showError("Invalid departure or arrival city");
                return;
            }




            try {
                String jsonResponse = FlightApiClient.getFlights(departureIata, arrivalIata);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(jsonResponse);

                JsonNode flightsNode = rootNode.path("data");
                if (flightsNode.isMissingNode() || !flightsNode.isArray()) {
                    showError("No flights found");
                    return;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                for (JsonNode flightNode : flightsNode) {
                    String airline = flightNode.path("airline").path("name").asText();
                    int flightNumber = flightNode.path("flight").path("number").asInt();
                    Date departureTime = dateFormat.parse(flightNode.path("departure").path("scheduled").asText());
                    Date arrivalTime = dateFormat.parse(flightNode.path("arrival").path("scheduled").asText());
                    String duration = flightNode.path("flight_time").asText();
                    int price = (int) (Math.random() * 1000 + 500); // Example price generation since API might not provide it

                    Vol vol = new Vol(
                            0,
                            departureTime,
                            arrivalTime,
                            airline,
                            flightNumber,
                            duration,
                            price,
                            Vol.TypeVol.Aller,
                            currentVoyage
                    );

                    volTable.getItems().add(vol);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Error loading flights: " + e.getMessage());
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String getIataCode(String city) {
        Map<String, String> cityToIata = new HashMap<>();
        cityToIata.put("tunis", "TUN");
        cityToIata.put("dubai", "DXB");
        cityToIata.put("paris", "CDG");
        cityToIata.put("london", "LHR");

        cityToIata.put("argentine", "ARG");
        cityToIata.put("australie", "AUS");
        cityToIata.put("belgique", "BEL");

        cityToIata.put("brésil", "BRA");
        cityToIata.put("croatie", "HRV");
        cityToIata.put("égypte", "EGY");
        cityToIata.put("france", "FRA");

        cityToIata.put("munich", "MUN");
        cityToIata.put("japon", "JPN");
        cityToIata.put("libye", "LBY");
        cityToIata.put("monaco", "MCO");

        cityToIata.put("maroc", "MAR");
        cityToIata.put("états-Unis", "USA");



        return cityToIata.getOrDefault(city.toLowerCase(), "");
    }


}