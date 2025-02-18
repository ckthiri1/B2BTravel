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
        reclamationServices.updateEntity(reclamationAModifier.getIDR(), reclamationAModifier);
        System.out.println("Réclamation mise à jour avec succès !");

        // Fermer la fenêtre de modification
        Stage stage = (Stage) titreTextField.getScene().getWindow();
        stage.close();
    }
}
