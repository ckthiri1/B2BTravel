package Reclamation.Controller;

import Reclamation.entities.Reponse;
import Reclamation.Services.ReponseServices;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.logging.Logger;

public class ModifierReponseController {

    @FXML
    private TextArea txtDescription;

    @FXML
    private Button btnModifier;

    private final ReponseServices reponseServices = new ReponseServices();
    private Reponse reponse;

    private static final Logger LOGGER = Logger.getLogger(ModifierReponseController.class.getName());

    public void setReponse(Reponse reponse) {
        this.reponse = reponse;
        txtDescription.setText(reponse.getDescriptionRep());

        // Ensure DateRep is not null
        if (reponse.getDateRep() == null) {
            reponse.setDateRep(LocalDate.now()); // Set a default date
        }
    }

    @FXML
    private void modifierReponse() {
        if (reponse != null) {
            String newDescription = txtDescription.getText().trim();

            // Vérification si la description est vide
            if (newDescription.isEmpty()) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La description ne peut pas être vide.");
                return;
            }

            // Mise à jour des valeurs
            reponse.setDescriptionRep(newDescription);

            // Vérification et affectation de la date si elle est null
            if (reponse.getDateRep() == null) {
                LOGGER.warning("⚠️ DateRep est null, affectation de la date actuelle.");
                reponse.setDateRep(LocalDate.now());
            }

            // Debug log
            LOGGER.info("Modification en cours pour IDRep: " + reponse.getIDRep() +
                    ", Nouvelle description: " + reponse.getDescriptionRep() +
                    ", Date: " + reponse.getDateRep());

            boolean success = reponseServices.updateEntity(reponse.getIDRep(), reponse);

            if (success) {
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Réponse modifiée avec succès !");
                fermerFenetre();
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification de la réponse.");
            }
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnModifier.getScene().getWindow();
        stage.close();
    }
}
