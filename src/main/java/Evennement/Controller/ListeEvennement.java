package Evennement.Controller;

import Evennement.entities.Evennement;
import Evennement.entities.EventType;
import Evennement.entities.Organisateur;
import Evennement.services.EvennementService;
import Evennement.tools.MyConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListeEvennement {

    @FXML
    private VBox eventListContainer; // Replacing TableView with VBox

    private final EvennementService evennementService = new EvennementService(); // Service instance

    @FXML
    public void initialize() {

        loadEvennements(); // Load events dynamically
    }
    @FXML
    private void loadEvennements() {
        try {
            eventListContainer.getChildren().clear();

            List<Evennement> events = new ArrayList<>();
            String query = "SELECT e.IDE, e.NomE, e.Local, e.DateE, e.DesE, e.event_type, " +
                    "o.IDOr, o.NomOr, o.Contact " +
                    "FROM Evennement e " +
                    "JOIN Organisateur o ON e.IDOr = o.IDOr";

            Statement stmt = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int IDE = rs.getInt("IDE");
                String nomE = rs.getString("NomE");
                String local = rs.getString("Local");
                String desE = rs.getString("DesE");
                Date dateE = new Date(rs.getTimestamp("DateE").getTime());

                // Retrieve event type and handle invalid or null cases
                String eventTypeString = rs.getString("event_type");

                // Normalize the event type string to match the enum values
                if (eventTypeString != null) {
                    eventTypeString = eventTypeString.toUpperCase(); // Convert to uppercase
                }

                EventType eventType = null;
                if (eventTypeString != null) {
                    try {
                        eventType = EventType.valueOf(eventTypeString);  // Convert String to EventType enum
                        System.out.println("Successfully converted to EventType: " + eventType); // Debugging log
                    } catch (IllegalArgumentException e) {
                        System.out.println("❌ Invalid event type: " + eventTypeString);
                        System.out.println("Valid event types are: " + Arrays.toString(EventType.values())); // Log all valid enum values
                    }
                }

                if (eventType == null) {
                    eventType = EventType.DEFAULT; // Default event type
                    System.out.println("Event type set to default: " + eventType); // Debugging log
                }

                int IDOr = rs.getInt("IDOr");
                String nomOr = rs.getString("NomOr");
                int contact = rs.getInt("Contact");

                Organisateur organisateur = new Organisateur(IDOr, nomOr, contact);
                Evennement evennement = new Evennement(nomE, local, desE, dateE, organisateur, eventType);
                evennement.setIDE(IDE);

                // Debugging: Verify the event type set in Evennement
                System.out.println("EventType set in Evennement: " + evennement.getEventType());

                // Dynamically add event to VBox
                eventListContainer.getChildren().add(createEventItem(evennement));
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Error while fetching events: " + e.getMessage());
            showAlert("Erreur", "Erreur lors de la récupération des événements.");
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            showAlert("Erreur", "Une erreur inattendue est survenue.");
        }
    }













    private HBox createEventItem(Evennement evennement) {
        HBox eventBox = new HBox(20);
        eventBox.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Create labels for event information
        Label nameLabel = new Label("📌 " + evennement.getNomE());
        Label locationLabel = new Label("📍 " + evennement.getLocal());
        Label dateLabel = new Label("📅 " + evennement.getDateE().toString());
        Label organizerLabel = new Label("👤 " + evennement.getOrganisateur().getNomOr());

        // New label for event type
        Label eventTypeLabel;
        if (evennement.getEventType() != null) {
            eventTypeLabel = new Label("📅 Type: " + evennement.getEventType().toString());
        } else {
            eventTypeLabel = new Label("📅 Type: Unknown");
        }

        // Buttons for modifying and deleting the event
        Button updateButton = new Button("Modifier");
        updateButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
        updateButton.setOnAction(e -> InterfaceUpdate(evennement));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> deleteEvennement(evennement));

        // Add all elements to the event box
        eventBox.getChildren().addAll(nameLabel, locationLabel, dateLabel, organizerLabel, eventTypeLabel, updateButton, deleteButton);
        return eventBox;
    }


    @FXML
    private void InterfaceUpdate(Evennement evennement) {
        if (evennement == null) {
            showAlert("Erreur", "Aucun événement sélectionné !");
            return;
        }

        try {
            // Load the UpdateEvennement.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEvennement.fxml"));
            Parent root = loader.load();

            // Pass data to the update controller
            UpdateEvennement updateController = loader.getController();
            updateController.setEvennement(evennement); // Pass the selected event to the update controller

            // Open new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'événement");
            stage.show();
            stage.setFullScreen(true);

            // Close current window (optional)
            Stage currentStage = (Stage) eventListContainer.getScene().getWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface de mise à jour.");
        }
    }




    private void deleteEvennement(Evennement evennement) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                evennementService.delete(evennement.getIDE());
                loadEvennements(); // Refresh VBox
                showAlert("Succès", "L'événement a été supprimé avec succès !");
            }
        });
    }

    @FXML
    private void interfaceAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvennement.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Evennement");
            stage.show();
            stage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface d'ajout.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}