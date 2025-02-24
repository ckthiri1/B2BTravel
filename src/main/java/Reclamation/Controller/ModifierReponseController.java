package Reclamation.Controller;

import Reclamation.entities.Reponse;
import Reclamation.Services.ReponseServices;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ModifierReponseController {

    @FXML
    private TextArea txtDescription; // TextArea for the response description

    @FXML
    private Button btnModifier; // Button to save changes

    private final ReponseServices reponseServices = new ReponseServices();
    private Reponse reponse; // The response to modify

    /**
     * Initializes the controller with the selected Reponse object.
     *
     * @param reponse The response to modify.
     */
    public void setReponse(Reponse reponse) {
        this.reponse = reponse;
        txtDescription.setText(reponse.getDescriptionRep()); // Set the description in the TextArea
    }

    /**
     * Handles the "Modifier" button click event.
     */
    @FXML
    private void modifierReponse() {
        if (reponse != null) {
            String newDescription = txtDescription.getText().trim();

            // Validate the description
            if (newDescription.isEmpty()) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La description ne peut pas être vide.");
                return;
            }

            // Update the response description
            reponse.setDescriptionRep(newDescription);

            // Save the changes to the database
            boolean success = reponseServices.updateEntity(reponse.getIDRep(), reponse);

            if (success) {
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Réponse modifiée avec succès !");
                fermerFenetre(); // Close the window after saving
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la modification de la réponse.");
            }
        }
    }

    /**
     * Displays an alert to the user.
     *
     * @param type    The type of alert (ERROR, INFORMATION, etc.).
     * @param titre   The title of the alert.
     * @param message The message to display.
     */
    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Closes the modification window.
     */
    private void fermerFenetre() {
        Stage stage = (Stage) btnModifier.getScene().getWindow();
        stage.close();
    }
}