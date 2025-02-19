package Evennement.Controller;

import Evennement.entities.Organisateur;
import Evennement.services.OrganisateurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateOrganisateur {

    @FXML
    private TextField txtnomo; // TextField for organizer name

    @FXML
    private TextField txtcontacto; // TextField for organizer contact

    @FXML
    private Button btnUpdateOrganisateur; // Button to update organizer

    private Organisateur selectedOrganisateur; // Selected organizer to update
    private final OrganisateurService organisateurService = new OrganisateurService(); // Service instance

    /**
     * Populates the form fields with the selected organizer's data.
     *
     * @param organisateur The organizer to update.
     */
    public void setOrganisateurData(Organisateur organisateur) {
        this.selectedOrganisateur = organisateur;
        txtnomo.setText(organisateur.getNomOr());
        txtcontacto.setText(String.valueOf(organisateur.getContact()));
    }

    @FXML
    void updateOr(ActionEvent event) {
        // Check if an organizer is selected
        if (selectedOrganisateur == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun organisateur sélectionné.");
            return;
        }

        // Get updated values
        String nomOr = txtnomo.getText().trim();
        String contactText = txtcontacto.getText().trim();

        // Validate input
        if (nomOr.isEmpty() || contactText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Validate contact input (must be a valid integer)
        int contact;
        try {
            contact = Integer.parseInt(contactText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le contact doit être un nombre valide.");
            return;
        }

        // Update organizer object
        selectedOrganisateur.setNomOr(nomOr);
        selectedOrganisateur.setContact(contact);

        try {
            // Call the update function
            organisateurService.update(selectedOrganisateur, selectedOrganisateur.getIDOr());

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Organisateur mis à jour avec succès !");
        } catch (Exception e) {
            // Show error message in case of failure (e.g., database issue)
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la mise à jour.");
            e.printStackTrace();
            return;
        }

        // Close the window after update
        closeWindow();
    }

    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) btnUpdateOrganisateur.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
