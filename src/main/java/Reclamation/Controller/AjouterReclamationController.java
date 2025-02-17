package Reclamation.Controller;

import Reclamation.entities.Reclamation;
import Reclamation.Services.ReclamationServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    private final ReclamationServices reclamationServices = new ReclamationServices();

    @FXML
    private void ajouterReclamation(ActionEvent event) {
        // Récupérer les données des champs
        String titre = titreTextField.getText().trim();
        String description = descriptionTextField.getText().trim();
        LocalDate dateR = datePicker.getValue();

        // Validation des champs
        if (titre.isEmpty() || description.isEmpty() || dateR == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText(null);
            alert.setContentText("Tous les champs doivent être remplis !");
            alert.showAndWait();
            return;
        }

        // Vérification de la date
        if (dateR.isAfter(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Date invalide");
            alert.setHeaderText(null);
            alert.setContentText("La date ne peut pas être dans le futur !");
            alert.showAndWait();
            return;
        }

        // Création de la réclamation
        Reclamation reclamation = new Reclamation(titre, description, dateR);

        // Enregistrement dans la base de données
        reclamationServices.addEntity(reclamation);

        // Affichage du message de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Réclamation ajoutée avec succès !");
        alert.showAndWait();

        // Réinitialiser les champs
        titreTextField.clear();
        descriptionTextField.clear();
        datePicker.setValue(null);

        // Navigation vers la page de liste des réclamations
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