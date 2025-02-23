package Projet.controllers.GestionReservationController;

import Projet.entities.User;
import Projet.entities.Vol;
import Projet.services.VolService;
import Projet.tools.MyConnection;
import Projet.tools.UserSession;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class HistoriqueReservationController {

        @FXML
        private VBox reservationContainer;
        @FXML
        private Label totalPriceValue;
        @FXML
        private Button reserveButton;
        @FXML
        private ScrollPane scrollPane;

        private final VolService volService = new VolService();
        private double totalReservationPrice = 0;

        @FXML
        public void initialize() {
            setupUI();
            loadUserReservations();
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



    private void loadUserReservations() {
        try {
            User currentUser = UserSession.getInstance().getUser();
            if (currentUser == null) {
                System.out.println("No user is logged in.");
                return;
            }

            List<Vol> userVols = volService.getVolsReserverByUserId();
            reservationContainer.getChildren().clear();
            totalReservationPrice = 0; // Reset total price

            for (Vol vol : userVols) {
                if (vol.getStatus() == Vol.StatusVol.RESERVER) { // ✅ Now checking for "RESERVER"
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



            priceSection.getChildren().addAll(priceLabel);
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


        private void showAlert(Alert.AlertType type, String title, String content) {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }

    }

