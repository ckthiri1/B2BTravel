package Evennement.Controller;

import Evennement.entities.Organisateur;
import Evennement.services.OrganisateurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ListeOrganisateur {

    @FXML
    private VBox organisateurListContainer; // VBox to hold organizers

    private final OrganisateurService organisateurService = new OrganisateurService();

    @FXML
    public void initialize() {
        loadOrganisateurs(); // Load organizers dynamically when the controller initializes
    }

    @FXML
    private void loadOrganisateurs() {
        organisateurListContainer.getChildren().clear(); // Clear any previous organizers

        List<Organisateur> organisateurs = organisateurService.getAllData(); // Fetch organizers from the database

        for (Organisateur organisateur : organisateurs) {
            HBox organisateurItem = createOrganisateurItem(organisateur); // Create each item
            organisateurListContainer.getChildren().add(organisateurItem); // Add to VBox
        }
    }

    private HBox createOrganisateurItem(Organisateur organisateur) {
        HBox organisateurItem = new HBox(20); // Horizontal layout for each item
        organisateurItem.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label nameLabel = new Label("👤 " + organisateur.getNomOr());
        Label contactLabel = new Label("📞 " + organisateur.getContact());

        Button updateButton = new Button("Modifier");
        updateButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
        updateButton.setOnAction(e -> InterfaceUpdate(organisateur));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> deleteOrganisateur(organisateur));

        organisateurItem.getChildren().addAll(nameLabel, contactLabel, updateButton, deleteButton);
        return organisateurItem;
    }

    private void InterfaceUpdate(Organisateur organisateur) {
        if (organisateur == null) {

            return;
        }

        try {
            // Load the UpdateOrganisateur.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateOrganisateur.fxml"));
            Parent root = loader.load();

            // Pass data to the update controller
            UpdateOrganisateur updateController = loader.getController();
            updateController.setOrganisateurData(organisateur);

            // Show new stage for the update interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'organisateur");
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void deleteOrganisateur(Organisateur organisateur) {
        if (organisateur != null) {
            organisateurService.delete(organisateur.getIDOr()); // Delete from the database
            organisateurListContainer.getChildren().removeIf(node -> {
                HBox item = (HBox) node;
                Label nameLabel = (Label) item.getChildren().get(0);
                return nameLabel.getText().equals("👤 " + organisateur.getNomOr());
            });
        }
    }

    @FXML
    private void interfaceAjoutO(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterOrganisateur.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Organisateur");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface d'ajout.");
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
