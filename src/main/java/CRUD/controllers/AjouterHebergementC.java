package CRUD.controllers;

import CRUD.entities.Hebergement;
import CRUD.entities.Reservation;
import CRUD.services.HebergementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AjouterHebergementC {

    @FXML
    private TextField Adresse;

    @FXML
    private Button AjoutH;

    @FXML
    private TextField Nom;

    @FXML
    private SplitMenuButton Type;

    @FXML
    private TextArea des;

    @FXML
    private Button listButton;
    private HebergementService hebergementService = new HebergementService();
    private String selectedType;
    @FXML
    private Label successMessage;
    @FXML
    private Label msg1;
    @FXML
    private Label msg2;
    @FXML
    private Label msg3;
    @FXML
    private Label msg4;

    @FXML
    void initialize() {

        setupStatusMenu();  // Initialiser le menu des statuts
    }

    @FXML
    void ajouterH(ActionEvent event) {
        try {
            // Réinitialiser les messages d'erreur
            msg1.setVisible(false);
            msg2.setVisible(false);
            msg3.setVisible(false);
            msg4.setVisible(false);

            // Vérification du Nom
            String nom = Nom.getText().trim();
            if (nom.isEmpty() || !nom.matches("[a-zA-Z ]{3,}")) {
                showMessage(msg1, "Nom invalide (3 lettres min, lettres uniquement).");
                return;
            }

            // Vérification de l'Adresse
            String adresse = Adresse.getText().trim();
            if (adresse.isEmpty() || adresse.length() < 3) {
                showMessage(msg2, "Adresse invalide (5 caractères min).");
                return;
            }

            // Vérification du Type
            if (selectedType == null || selectedType.isEmpty()) {
                showMessage(msg3, "Veuillez sélectionner un type.");
                return;
            }

            // Vérification de la Description
            String desc = des.getText().trim();
            if (desc.isEmpty() || desc.length() < 10) {
                showMessage(msg4, "Description trop courte (10 caractères min).");
                return;
            }

            // Création et ajout de l'hébergement
            Hebergement hebergement = new Hebergement(nom, adresse, selectedType, desc);
            hebergementService.addEntity2(hebergement);

            // Affichage du message de succès
            showMessage(successMessage, "Hébergement ajouté avec succès !");

        } catch (Exception e) {
            showMessage(successMessage, "Erreur : " + e.getMessage());
        }
    }

    // Fonction pour afficher un message d'erreur pendant 3 secondes
    private void showMessage(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                label.setVisible(false);
            } catch (InterruptedException ignored) {}
        }).start();
    }

    private void setupStatusMenu() {
        for (MenuItem item : Type.getItems()) {
            item.setOnAction(event -> {
                selectedType = item.getText();
                Type.setText(selectedType);
            });
        }
    }

    @FXML
    void listeR(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageHebergement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page Affichage: " + e.getMessage());
        }
    }

}




