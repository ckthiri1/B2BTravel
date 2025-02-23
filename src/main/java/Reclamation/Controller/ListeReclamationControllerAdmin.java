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

public class ListeReclamationControllerAdmin {

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

        // Créer un bouton pour répondre
        Button respondButton = new Button("Répondre");
        respondButton.setPrefWidth(150); // Largeur correspondant à l'en-tête

        // Définir l'action du bouton Répondre
        respondButton.setOnAction(event -> repondreAReclamation(reclamation));

        // Créer une HBox pour contenir les labels et le bouton
        HBox row = new HBox(10); // 10 est l'espacement entre les éléments
        row.getChildren().addAll(titreLabel, descriptionLabel, dateLabel, respondButton);

        // Appliquer un style à la ligne
        row.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-padding: 10px;");

        return row;
    }

    private void repondreAReclamation(Reclamation reclamation) {
        try {
            // Charger la vue AjouterReponse.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReponse.fxml"));
            Parent root = loader.load();

            // Passer l'ID de la réclamation au contrôleur AjouterReponseController
            AjouterReponseController controller = loader.getController();
            controller.setIDR(reclamation.getIDR()); // Utilisez setIDR pour définir l'ID

            // Afficher la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une réponse");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AjouterReponse.fxml : " + e.getMessage());
        }
    }
}