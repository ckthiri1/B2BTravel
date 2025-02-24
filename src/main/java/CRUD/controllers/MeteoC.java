package CRUD.controllers;

import CRUD.tools.MyConnection;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MeteoC {

    private static final String API_KEY = "14558128eff777dcd766e29ab93e78b9"; // Replace with your actual API key

    @FXML
    private VBox vbox;

    @FXML
    private ComboBox<String> cityComboBox;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label descriptionLabel;
    @FXML
    private ImageView emoji;


    @FXML
    private Button refreshButton;

    public void initialize() {
        // Populate the ComboBox with cities from the database
        loadCitiesFromDatabase();

        // When the refresh button is clicked, get the weather data for the selected city
        refreshButton.setOnAction(e -> {
            String selectedCity = cityComboBox.getValue();
            if (selectedCity != null && !selectedCity.trim().isEmpty()) {
                getWeatherData(selectedCity);
            } else {
                temperatureLabel.setText("Error: No city selected");
                descriptionLabel.setText("");
            }
        });
        TranslateTransition transition = new TranslateTransition(Duration.seconds(5), emoji);
        transition.setFromX(-50);  // Starting position (to the left)
        transition.setToX(50);     // Ending position (to the right)
        transition.setCycleCount(TranslateTransition.INDEFINITE); // Repeat the animation indefinitely
        transition.setAutoReverse(true); // Make it move back and forth
        transition.play();
    }

    // Fetch the city list from the database and populate the ComboBox
    private void loadCitiesFromDatabase() {
        try (Connection conn = MyConnection.getInstance().getCnx()) {
            String query = "SELECT name FROM cities";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                cityComboBox.getItems().add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch weather data from the OpenWeather API
    private void getWeatherData(String city) {
        try {
            String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parsing the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("main")) {
                double temperature = jsonResponse.getJSONObject("main").getDouble("temp");
                String description = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");

                // Updating the UI
                temperatureLabel.setText("Temperature: " + temperature + "°C");
                descriptionLabel.setText("Description: " + description);
            } else {
                temperatureLabel.setText("Error: Invalid city or API issue");
                descriptionLabel.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            temperatureLabel.setText("Error: Could not retrieve data");
            descriptionLabel.setText("");
        }
    }
}
