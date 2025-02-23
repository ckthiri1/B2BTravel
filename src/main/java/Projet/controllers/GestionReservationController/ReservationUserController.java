

package Projet.controllers.GestionReservationController;

import Projet.entities.Vol;
import Projet.entities.reservation_voyage;
import Projet.entities.User;
import Projet.services.VolService;
import Projet.tools.UserSession;
import Projet.tools.MyConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationUserController {

    @FXML
    private VBox reservationContainer;
    @FXML
    private Label totalPriceValue;
    @FXML
    private Button reserveButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML private AnchorPane mainPane;


    private final VolService volService = new VolService();
    private double totalReservationPrice = 0;

    @FXML
    public void initialize() {
        setupUI();
        loadUserReservations();
        setupReserveButton();
    }

    private void setupUI() {
        if (scrollPane != null) {
            scrollPane.setFitToWidth(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        }

        if (reservationContainer != null) {
            reservationContainer.setSpacing(15);
            reservationContainer.setPadding(new Insets(10));
        }
    }

    private void setupReserveButton() {
        if (reserveButton != null) {
            reserveButton.setOnAction(event -> handleReservation());
            reserveButton.getStyleClass().add("reserve-button");
        }
    }

    private void handleReservation() {
        try {
            User currentUser = UserSession.getInstance().getUser();
            if (currentUser == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please log in to make a reservation.");
                return;
            }

            List<Vol> userVols = volService.getVolsByUserId();
            if (userVols.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "No flights selected for reservation.");
                return;
            }

            totalReservationPrice = 0;
            Connection connection = MyConnection.getInstance().getCnx();

            String insertQuery = "INSERT INTO reservation_voyage (id_user, id_vol, place, prixTotal) VALUES (?, ?, ?, ?)";
            String updateStatusQuery = "UPDATE vol SET status = 'RESERVER' WHERE volID = ?";  // Query to update status

            try (PreparedStatement pstInsert = connection.prepareStatement(insertQuery);
                 PreparedStatement pstUpdate = connection.prepareStatement(updateStatusQuery)) {

                connection.setAutoCommit(false);  // Start transaction

                for (Vol vol : userVols) {
                    int numberOfPlaces = 2; // Fixed at 2 places
                    double flightTotalPrice = calculateTotalPrice(vol.getPrixVol(), numberOfPlaces);
                    totalReservationPrice += flightTotalPrice;

                    // Insert reservation details
                    pstInsert.setInt(1, currentUser.getUser_id());
                    pstInsert.setInt(2, vol.getVolID());
                    pstInsert.setInt(3, numberOfPlaces);
                    pstInsert.setDouble(4, flightTotalPrice);
                    pstInsert.executeUpdate();

                    // Update vol status to RESERVER
                    pstUpdate.setInt(1, vol.getVolID());
                    pstUpdate.executeUpdate();
                }

                connection.commit();  // Commit transaction
                connection.setAutoCommit(true);  // Reset auto-commit

                updateTotalPriceDisplay();
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        String.format("Reservation completed successfully!\nTotal Price: %.2f DT",
                                totalReservationPrice));

                loadUserReservations();
                NavigateToHisqtoriqueReservation();// Refresh the display

            } catch (SQLException e) {
                connection.rollback();  // Rollback on error
                connection.setAutoCommit(true);
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to complete reservation: " + e.getMessage());
        }
    }

    private void loadUserReservations() {
        try {
            User currentUser = UserSession.getInstance().getUser();
            if (currentUser == null) {
                System.out.println("No user is logged in.");
                return;
            }

            List<Vol> userVols = volService.getVolsByUserId(); // ✅ Only get NON_RESERVER flights
            reservationContainer.getChildren().clear();
            totalReservationPrice = 0; // Reset total price

            for (Vol vol : userVols) {
                if (vol.getStatus() == Vol.StatusVol.NON_RESERVER) { // ✅ Check status
                    AnchorPane reservationItem = createReservationItem(vol);
                    reservationContainer.getChildren().add(reservationItem);
                    totalReservationPrice += calculateTotalPrice(vol.getPrixVol(), 1);
                }
            }

            updateTotalPriceDisplay();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load reservations: " + e.getMessage());
        }
    }

    private AnchorPane createReservationItem(Vol vol) {
        AnchorPane reservationItem = new AnchorPane();
        reservationItem.getStyleClass().add("reservation-item");

        HBox contentBox = new HBox();
        contentBox.setSpacing(10);
        contentBox.setAlignment(Pos.CENTER_LEFT);
        contentBox.setPrefWidth(1400);

        // Flight Info Section
        VBox flightInfo = createFlightInfoSection(vol);

        // Time and Date Section
        VBox timeDate = createTimeDateSection(vol);

        // Price Section
        VBox priceSection = createPriceSection(vol);

        // Set growth priorities
        HBox.setHgrow(flightInfo, Priority.ALWAYS);
        HBox.setHgrow(timeDate, Priority.SOMETIMES);
        HBox.setHgrow(priceSection, Priority.NEVER);

        contentBox.getChildren().addAll(flightInfo, timeDate, priceSection);
        contentBox.setAlignment(Pos.CENTER);

        reservationItem.getChildren().add(contentBox);
        return reservationItem;
    }

    private void updateTotalPrice() {
        totalReservationPrice = 0;

        for (var node : reservationContainer.getChildren()) {
            if (node instanceof AnchorPane reservationItem) {
                for (var child : reservationItem.getChildren()) {
                    if (child instanceof HBox contentBox) {
                        for (var subChild : contentBox.getChildren()) {
                            if (subChild instanceof VBox flightInfo) {
                                for (var flightChild : flightInfo.getChildren()) {
                                    if (flightChild instanceof HBox seatBox) {
                                        for (var seatChild : seatBox.getChildren()) {
                                            if (seatChild instanceof TextField seatInput) {
                                                try {
                                                    // Get the associated Vol object
                                                    Vol vol = (Vol) seatInput.getUserData();
                                                    if (vol != null) {
                                                        int seats = Integer.parseInt(seatInput.getText().isEmpty() ? "1" : seatInput.getText());
                                                        totalReservationPrice += vol.getPrixVol() * seats;
                                                    }
                                                } catch (NumberFormatException ignored) {
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Update UI
        totalPriceValue.setText(String.format("%.2f DT", totalReservationPrice));
    }

    private VBox createFlightInfoSection(Vol vol) {
        VBox flightInfo = new VBox(20);
        flightInfo.getStyleClass().add("flight-info-section");
        flightInfo.setPadding(new Insets(20));
        flightInfo.setPrefWidth(250);

        TextField seatInput = new TextField("1"); // Default is 1 seat
        seatInput.setPrefWidth(50);
        seatInput.getStyleClass().add("seat-input");
        seatInput.setUserData(vol); // Store the Vol object inside the input field

        // Ensure only numbers are entered and minimum value is 1
        seatInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                seatInput.setText(oldValue); // Prevent non-numeric input
            } else if (newValue.isEmpty()) {
                seatInput.setText(""); // Allow temporary empty input
            } else {
                try {
                    int numSeats = Integer.parseInt(newValue);
                    if (numSeats < 1) {
                        seatInput.setText("1"); // Reset to 1 if below minimum
                    }
                    updateTotalPrice(); // Update total price dynamically
                } catch (NumberFormatException e) {
                    seatInput.setText("1"); // Handle parsing error
                }
            }
        });

        // Reset to "1" when focus is lost and field is empty
        seatInput.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && seatInput.getText().isEmpty()) {
                seatInput.setText("1");
                updateTotalPrice();
            }
        });

        HBox seatBox = new HBox(8);
        Label seatLabel = new Label("👥 SEATS:");
        seatLabel.getStyleClass().add("info-label");
        seatBox.getChildren().addAll(seatLabel, seatInput);
        seatBox.setAlignment(Pos.CENTER_LEFT);

        flightInfo.getChildren().addAll(
                createLabeledBox("✈ AIRLINE:", vol.getAirLine()),
                createLabeledBox("🔢 FLIGHT NO:", String.valueOf(vol.getFlightNumber())),
                createLabeledBox("📌 TYPE:", vol.getTypeVol().toString()),
                seatBox
        );

        return flightInfo;
    }

    private VBox createTimeDateSection(Vol vol) {
        VBox timeDate = new VBox(10);
        timeDate.getStyleClass().add("time-date-section");
        timeDate.setAlignment(Pos.CENTER);
        timeDate.setPadding(new Insets(10, 50, 10, 50));
        timeDate.setPrefWidth(1000);

        Label durationLabel = new Label(vol.getDureeVol());
        durationLabel.getStyleClass().add("time-label");

        VBox departureBox = createDateBox("DEPARTURE", vol.getDateDepart().toString());
        VBox arrivalBox = createDateBox("ARRIVAL", vol.getDateArrival().toString());

        Label arrowLabel = new Label("→");
        arrowLabel.getStyleClass().add("arrow-label");

        HBox flightTime = new HBox(departureBox, arrowLabel, arrivalBox);
        flightTime.setSpacing(15);
        flightTime.setAlignment(Pos.CENTER);

        timeDate.getChildren().addAll(durationLabel, flightTime);
        return timeDate;
    }

    private VBox createPriceSection(Vol vol) {
        VBox priceSection = new VBox(8);
        priceSection.getStyleClass().add("price-section");
        priceSection.setPrefWidth(250);
        priceSection.setPadding(new Insets(10, 10, 10, 50));
        priceSection.setAlignment(Pos.CENTER_RIGHT);

        Label priceLabel = new Label(vol.getPrixVol() + " DT");
        priceLabel.getStyleClass().add("price-label");
        priceLabel.setAlignment(Pos.CENTER_RIGHT);

        Button deleteButton = new Button("DELETE");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(event -> deleteVol(vol.getVolID()));

        HBox buttonContainer = new HBox(deleteButton);
        buttonContainer.setAlignment(Pos.CENTER);
        HBox.setHgrow(deleteButton, Priority.ALWAYS);

        priceSection.getChildren().addAll(priceLabel, buttonContainer);
        return priceSection;
    }

    private HBox createLabeledBox(String title, String value) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("info-label");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("info-value");

        HBox labeledBox = new HBox(titleLabel, valueLabel);
        labeledBox.setSpacing(8);
        labeledBox.setAlignment(Pos.CENTER_LEFT);

        return labeledBox;
    }

    private VBox createDateBox(String label, String date) {
        Label labelTitle = new Label(label);
        labelTitle.getStyleClass().add("date-label");

        Label dateValue = new Label(date);
        dateValue.getStyleClass().add("date-value");

        VBox dateBox = new VBox(labelTitle, dateValue);
        dateBox.getStyleClass().add("date-box");
        dateBox.setAlignment(Pos.CENTER);

        return dateBox;
    }

    private double calculateTotalPrice(double basePrice, int numberOfPlaces) {
        return basePrice * numberOfPlaces;
    }

    private void updateTotalPriceDisplay() {
        if (totalPriceValue != null) {
            totalPriceValue.setText(String.format("%.2f DT", totalReservationPrice));
        }
    }

    private void deleteVol(int volId) {
        try {
            Connection connection = MyConnection.getInstance().getCnx();
            String deleteQuery = "DELETE FROM reservation_voyage WHERE id_vol = ?";

            try (PreparedStatement pst = connection.prepareStatement(deleteQuery)) {
                pst.setInt(1, volId);
                pst.executeUpdate();
            }

            volService.deleteVol(volId);
            loadUserReservations(); // Refresh the list
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to delete flight: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    void NavigateToHistoriqueReservation(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface Utilisateur/HistoriqueReservation.fxml"));
            Parent listVoyageRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(listVoyageRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void NavigateToHisqtoriqueReservation(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface Utilisateur/HistoriqueReservation.fxml"));
            Parent reservationPage = loader.load();

            // Get the current stage using any node in the scene (like mainPane)
            Stage stage = (Stage) mainPane.getScene().getWindow();
            Scene scene = new Scene(reservationPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}