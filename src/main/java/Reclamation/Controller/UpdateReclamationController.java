package Reclamation.Controller;

import Reclamation.entities.Reclamation;
import Reclamation.Services.ReclamationServices;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class UpdateReclamationController {

    @FXML
    private TextField titreTextField; // Champ pour le titre

    @FXML
    private TextField descriptionTextField; // Champ pour la description

    @FXML
    private DatePicker datePicker; // Champ pour la date

    private Reclamation reclamationAModifier; // La réclamation à modifier
    private ListeReclamationController listeReclamationController; // Référence au contrôleur principal
    private final ReclamationServices reclamationServices = new ReclamationServices(); // Service pour interagir avec la base de données

    /**
     * Initialise les champs du formulaire avec les données de la réclamation sélectionnée.
     *
     * @param reclamation La réclamation à modifier.
     */
    public void setReclamationAModifier(Reclamation reclamation) {
        this.reclamationAModifier = reclamation;
        titreTextField.setText(reclamation.getTitre());
        descriptionTextField.setText(reclamation.getDescription());
        datePicker.setValue(reclamation.getDateR());
    }

    /**
     * Définit la référence du contrôleur principal.
     *
     * @param controller Le contrôleur principal.
     */
    public void setListeReclamationController(ListeReclamationController controller) {
        this.listeReclamationController = controller;
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur "Enregistrer".
     * Met à jour les données de la réclamation dans la base de données.
     */
    @FXML
    private void enregistrerModification() {
        // Récupérer les nouvelles valeurs
        String nouveauTitre = titreTextField.getText().trim();
        String nouvelleDescription = descriptionTextField.getText().trim();
        LocalDate nouvelleDate = datePicker.getValue();

        // Validation des champs
        if (nouveauTitre.isEmpty() || nouvelleDescription.isEmpty() || nouvelleDate == null) {
            System.out.println("Tous les champs doivent être remplis !");
            return;
        }

        // Mettre à jour les données de la réclamation
        reclamationAModifier.setTitre(nouveauTitre);
        reclamationAModifier.setDescription(nouvelleDescription);
        reclamationAModifier.setDateR(nouvelleDate);

        // Appeler la méthode de mise à jour en passant l'ID et l'objet modifié
        boolean isUpdated = reclamationServices.updateEntity(reclamationAModifier.getIDR(), reclamationAModifier);
        if (isUpdated) {
            System.out.println("Réclamation mise à jour avec succès !");

            // Rafraîchir le tableau dans le contrôleur principal
            if (listeReclamationController != null) {
                listeReclamationController.refreshTable(); // Appel correct de la méthode void
            }

            // Fermer la fenêtre de modification
            Stage stage = (Stage) titreTextField.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("Erreur : La réclamation n'a pas pu être mise à jour.");
        }
    }
}