package Evennement.Controller;

import Evennement.entities.Evennement;
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
        eventListContainer.getChildren().clear(); // Clear previous events

        List<Evennement> events = new ArrayList<>();
        String query = "SELECT e.IDE, e.NomE, e.Local, e.DateE, e.DesE, o.IDOr, o.NomOr, o.Contact " +
                "FROM Evennement e " +
                "JOIN Organisateur o ON e.IDOr = o.IDOr";

        try {
            Statement stmt = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int IDE = rs.getInt("IDE");
                String nomE = rs.getString("NomE");
                String local = rs.getString("Local");
                String desE = rs.getString("DesE");
                Date dateE = new Date(rs.getTimestamp("DateE").getTime());

                // Retrieve Organisateur details
                int IDOr = rs.getInt("IDOr");
                String nomOr = rs.getString("NomOr");
                int contact = rs.getInt("Contact");

                // Create Organisateur object
                Organisateur organisateur = new Organisateur(IDOr, nomOr, contact);

                // Create Evennement object and set its properties
                Evennement evennement = new Evennement(nomE, local, desE, dateE, organisateur);
                evennement.setIDE(IDE);

                // Add event to VBox dynamically
                eventListContainer.getChildren().add(createEventItem(evennement));
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Error while fetching events: " + e.getMessage());
        }
    }

    private HBox createEventItem(Evennement evennement) {
        HBox eventBox = new HBox(20);
        eventBox.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label nameLabel = new Label("📌 " + evennement.getNomE());
        Label locationLabel = new Label("📍 " + evennement.getLocal());
        Label dateLabel = new Label("📅 " + evennement.getDateE().toString());
        Label organizerLabel = new Label("👤 " + evennement.getOrganisateur().getNomOr());

        Button updateButton = new Button("Modifier");
        updateButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
        updateButton.setOnAction(e -> InterfaceUpdate(evennement));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> deleteEvennement(evennement));

        eventBox.getChildren().addAll(nameLabel, locationLabel, dateLabel, organizerLabel, updateButton, deleteButton);
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
