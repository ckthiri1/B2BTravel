package Reclamation.Controller;

import Reclamation.entities.Reclamation;
import Reclamation.Services.ReclamationServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ListeReclamationController {

    @FXML
    private VBox reclamationContainer; // VBox pour contenir les lignes de réclamations

    private final ReclamationServices reclamationServices = new ReclamationServices();

    @FXML
    public void initialize() {
        loadReclamations(); // Charger les réclamations au démarrage
    }

    private void loadReclamations() {
        // Vider le conteneur avant de charger de nouvelles données
        reclamationContainer.getChildren().clear();

        // Récupérer la liste des réclamations depuis le service
        List<Reclamation> reclamations = reclamationServices.getAllData();

        // Créer une HBox pour chaque réclamation
        for (Reclamation reclamation : reclamations) {
            HBox row = createReclamationRow(reclamation);
            reclamationContainer.getChildren().add(row);
        }
    }

    private HBox createReclamationRow(Reclamation reclamation) {
        // Créer des labels pour chaque champ (sans l'ID)
        Label titreLabel = new Label(reclamation.getTitre());
        titreLabel.setPrefWidth(150); // Largeur correspondant à l'en-tête

        Label descriptionLabel = new Label(reclamation.getDescription());
        descriptionLabel.setPrefWidth(200); // Largeur correspondant à l'en-tête

        Label dateLabel = new Label(reclamation.getDateR().toString());
        dateLabel.setPrefWidth(100); // Largeur correspondant à l'en-tête

        // Créer des boutons pour les actions
        Button deleteButton = new Button("Supprimer");
        deleteButton.setPrefWidth(75); // Largeur du bouton Supprimer

        Button editButton = new Button("Modifier");
        editButton.setPrefWidth(75); // Largeur du bouton Modifier

        // Définir les actions des boutons
        deleteButton.setOnAction(event -> supprimerReclamation(reclamation));
        editButton.setOnAction(event -> modifierReclamation(reclamation));

        // Créer une HBox pour contenir les labels et les boutons
        HBox row = new HBox(10); // 10 est l'espacement entre les éléments
        row.getChildren().addAll(titreLabel, descriptionLabel, dateLabel, deleteButton, editButton);

        // Appliquer un style à la ligne
        row.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-padding: 10px;");

        return row;
    }

    private void supprimerReclamation(Reclamation reclamation) {
        // Logique pour supprimer la réclamation
        boolean isDeleted = reclamationServices.deleteEntity(reclamation);
        if (isDeleted) {
            loadReclamations(); // Recharger la liste après la suppression
        }
    }

    private void modifierReclamation(Reclamation reclamation) {
        try {
            // Charger la fenêtre de modification
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateReclamation.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la fenêtre de modification
            UpdateReclamationController controller = loader.getController();
            controller.setReclamationAModifier(reclamation); // Passer la réclamation à modifier
            controller.setListeReclamationController(this); // Passer une référence de ce contrôleur

            // Créer une nouvelle scène et afficher la fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier une Réclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la fenêtre de modification.");
        }
    }

    public void refreshTable() {
        loadReclamations(); // Recharge les réclamations depuis la base de données
    }
}