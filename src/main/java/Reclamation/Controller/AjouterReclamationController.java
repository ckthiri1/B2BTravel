package Reclamation.Controller;

import Reclamation.API.JokeAPI;
import Reclamation.entities.Reclamation;
import Reclamation.Services.ReclamationServices;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;

public class AjouterReclamationController {

    @FXML
    private TextField titreTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button ajouterButton;

    @FXML
    private Button blagueButton; // Référence au bouton "Blague Aléatoire"

    private final ReclamationServices reclamationServices = new ReclamationServices();

    @FXML
    private void ajouterReclamation(ActionEvent event) {
        // Récupérer les données des champs
        String titre = titreTextField.getText().trim();
        String description = descriptionTextField.getText().trim();
        LocalDate dateR = datePicker.getValue();

        // Validation des champs
        if (titre.isEmpty() || description.isEmpty() || dateR == null) {
            showAlert(Alert.AlertType.WARNING, "WARNING", null, "Tous les champs doivent être remplis !");
            return;
        }

        // Vérification de la date
        if (dateR.isAfter(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Date invalide", null, "La date ne peut pas être dans le futur !");
            return;
        }

        // Création de la réclamation
        Reclamation reclamation = new Reclamation(titre, description, dateR);

        // Enregistrement dans la base de données
        reclamationServices.addEntity(reclamation);

        // Affichage du message de succès
        showAlert(Alert.AlertType.INFORMATION, "Succès", null, "Réclamation ajoutée avec succès !");

        // Réinitialiser les champs
        titreTextField.clear();
        descriptionTextField.clear();
        datePicker.setValue(null);

        // Navigation vers la page de liste des réclamations
        navigateToListeReclamations();
    }

    @FXML
    private void showRandomJoke(ActionEvent event) {
        // Appliquer un effet de rebond au bouton
        TranslateTransition bounce = new TranslateTransition(Duration.millis(100), blagueButton);
        bounce.setFromY(0);
        bounce.setToY(-10);
        bounce.setAutoReverse(true);
        bounce.setCycleCount(2);
        bounce.play();

        try {
            // Charger le fichier FXML de la popup de blague
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/JokePopup.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la popup
            JokeController jokeController = loader.getController();

            // Définir un callback pour réactiver le bouton après la fermeture
            jokeController.setOnCloseCallback(() -> {
                blagueButton.setDisable(false); // Réactiver le bouton
                blagueButton.setText("Blague Aléatoire"); // Remettre le texte d'origine
            });

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre (Stage)
            Stage jokeStage = new Stage();
            jokeStage.initModality(Modality.APPLICATION_MODAL); // Rend la fenêtre modale
            jokeStage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Définit la fenêtre parente
            jokeStage.setScene(scene);
            jokeStage.setTitle("😂 Blague Aléatoire 😂");
            jokeStage.getIcons().add(new Image("/images/joke_icon.png")); // Ajoute une icône
            jokeStage.show();

            // Désactiver le bouton après avoir affiché une blague
            blagueButton.setDisable(true);
            blagueButton.setText("Nouvelle Blague");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", null, "Impossible de charger la blague aléatoire.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToListeReclamations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeReclamations.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur de la page ListeReclamations
            ListeReclamationController listeReclamationController = loader.getController();

            // Recharger les données dans la TableView
            listeReclamationController.refreshTable();

            // Afficher la nouvelle scène
            Stage stage = (Stage) titreTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Réclamations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page ListeReclamations.fxml : " + e.getMessage());
        }
    }
}