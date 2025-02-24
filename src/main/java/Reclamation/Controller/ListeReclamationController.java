package Reclamation.Controller;

import Reclamation.entities.Reclamation;
import Reclamation.Services.ReclamationServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ListeReclamationController {

    @FXML
    private GridPane reclamationGrid; // GridPane pour contenir les réclamations

    private final ReclamationServices reclamationServices = new ReclamationServices();

    @FXML
    public void initialize() {
        loadReclamations(); // Charger les réclamations dès l'ouverture de la fenêtre
    }

    private void loadReclamations() {
        reclamationGrid.getChildren().clear(); // Nettoyer avant de recharger

        // Ajouter l'en-tête
        reclamationGrid.addRow(0,
                createHeaderLabel("Titre", 150),
                createHeaderLabel("Description", 200),
                createHeaderLabel("Date", 100),
                createHeaderLabel("Actions", 150)
        );

        // Récupérer la liste des réclamations
        List<Reclamation> reclamations = reclamationServices.getAllData();

        for (int i = 0; i < reclamations.size(); i++) {
            Reclamation reclamation = reclamations.get(i);
            int rowIndex = i + 1;

            // Création des cellules
            Label titreLabel = createCellLabel(reclamation.getTitre(), 150);
            Label descriptionLabel = createCellLabel(reclamation.getDescription(), 200);
            Label dateLabel = createCellLabel(reclamation.getDateR().toString(), 100);

            // Création des boutons d'actions
            Button deleteButton = createActionButton("Supprimer", "#e74c3c");
            Button editButton = createActionButton("Modifier", "#f39c12");

            deleteButton.setOnAction(event -> supprimerReclamation(reclamation));
            editButton.setOnAction(event -> modifierReclamation(reclamation));

            // Conteneur pour les boutons
            HBox actionBox = new HBox(10, deleteButton, editButton);

            // Ajouter la ligne à la grille
            reclamationGrid.addRow(rowIndex, titreLabel, descriptionLabel, dateLabel, actionBox);
        }
    }

    // Méthode pour supprimer une réclamation
    private void supprimerReclamation(Reclamation reclamation) {
        boolean isDeleted = reclamationServices.deleteEntity(reclamation);
        if (isDeleted) {
            loadReclamations(); // Rafraîchir la table après suppression
        }
    }

    // Méthode pour modifier une réclamation
    private void modifierReclamation(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateReclamation.fxml"));
            Parent root = loader.load();

            // Charger le contrôleur de la fenêtre de modification
            UpdateReclamationController controller = loader.getController();
            controller.setReclamationAModifier(reclamation);
            controller.setListeReclamationController(this);

            // Afficher la fenêtre de modification
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier une Réclamation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la fenêtre de modification.");
        }
    }

    // Méthode pour créer les labels d'en-tête bien alignés
    private Label createHeaderLabel(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        label.setStyle(
                "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #2980b9; " +
                        "-fx-padding: 10px; " +
                        "-fx-border-color: #2980b9; " +
                        "-fx-alignment: CENTER;"
        );
        return label;
    }

    // Méthode pour créer les cellules bien alignées
    private Label createCellLabel(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        label.setStyle(
                "-fx-padding: 10px; " +
                        "-fx-border-color: #bdc3c7; " +
                        "-fx-border-width: 0 0 1px 0; " +
                        "-fx-alignment: CENTER-LEFT;"
        );
        return label;
    }

    // Méthode pour créer les boutons d'action avec une couleur spécifique
    private Button createActionButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(90);
        button.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-padding: 5px;"
        );
        return button;
    }

    // Rafraîchir la table après modification
    public void refreshTable() {
        loadReclamations();
    }
}
