package CRUD.controllers;

import CRUD.entities.Hebergement;
import CRUD.services.HebergementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifierHebergement {

    @FXML
    private TextField AdresseM;

    @FXML
    private TextField NomM;

    @FXML
    private SplitMenuButton TypeM;

    @FXML
    private TextArea desM;

    @FXML
    private Button listButton;

    @FXML
    private Button modifier;
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
    void Modifier(ActionEvent event) {
        try {
            // Réinitialiser les messages d'erreur
            msg1.setVisible(false);
            msg2.setVisible(false);
            msg3.setVisible(false);
            msg4.setVisible(false);

            // Vérification du Nom
            String nom = NomM.getText().trim();
            if (nom.isEmpty() || !nom.matches("[a-zA-Z ]{3,}")) {
                showMessage(msg1, "Nom invalide (3 lettres min, lettres uniquement).");
                return;
            }

            // Vérification de l'Adresse
            String adresse = AdresseM.getText().trim();
            if (adresse.isEmpty() || adresse.length() < 5) {
                showMessage(msg2, "Adresse invalide (5 caractères min).");
                return;
            }

            // Vérification du Type
            String type = TypeM.getText();
            if (type == null || type.isEmpty() || !isValidType(type)) {
                showMessage(msg3, "Veuillez sélectionner un type valide.");
                return;
            }

            // Vérification de la Description
            String desc = desM.getText().trim();
            if (desc.isEmpty() || desc.length() < 10) {
                showMessage(msg4, "Description trop courte (10 caractères min).");
                return;
            }

            // Mise à jour de l'hébergement
            selectedHebergement.setNom(nom);
            selectedHebergement.setAdresse(adresse);
            selectedHebergement.setType(type);
            selectedHebergement.setDescription(desc);

            // Mise à jour dans la base de données
            HebergementService hebergementService = new HebergementService();
            hebergementService.updateEntity(selectedHebergement.getId_hebergement(), selectedHebergement);

            // Affichage du message de succès
            showMessage(successMessage, "Hébergement modifié avec succès !");
            Stage stage = (Stage) modifier.getScene().getWindow();
        } catch (NumberFormatException e) {
            showMessage(successMessage, "Le prix doit être un nombre valide.");
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

    // Vérifie si le type est valide
    private boolean isValidType(String type) {
        String[] validTypes = {"Hotel", "Hostel", "Maison"};
        for (String validType : validTypes) {
            if (validType.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    private Hebergement selectedHebergement;
    private AffichageHebergementC parentController;
    // Référence pour rafraîchir la TableView
    public void initData(Hebergement hebergement, AffichageHebergementC parent) {
        selectedHebergement = hebergement;
        parentController = parent;

        // Set date and price
        NomM.setText(String.valueOf(hebergement.getNom()));
        AdresseM.setText(String.valueOf(hebergement.getAdresse()));
        NomM.setText(String.valueOf(hebergement.getNom()));
        setupStatusMenu();

        desM.setText(String.valueOf(hebergement.getDescription()));


        // Set up status menu

    }
    private void setupStatusMenu() {
        TypeM.getItems().clear(); // Efface les anciennes options

        // Définition des options
        String[] statusOptions = {"Hotel", "Hostel" , "Maison"};
        for (String option : statusOptions) {
            MenuItem item = new MenuItem(option);
            item.setOnAction(event -> {
                TypeM.setText(option); // Met à jour l'affichage
                TypeM.setUserData(option); // Stocke la valeur sélectionnée
            });
            TypeM.getItems().add(item);
        }

        // Pré-sélectionner la valeur actuelle du statut
        if (selectedHebergement != null) {
            TypeM.setText(selectedHebergement.getType());
        }
    }

    @FXML
    void listeH(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageHebergement.fxml")); // Page précédente
            Parent root = loader.load();

            // Récupérer la scène actuelle et remplacer son contenu
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du retour : " + e.getMessage());
        }
    }
    }


