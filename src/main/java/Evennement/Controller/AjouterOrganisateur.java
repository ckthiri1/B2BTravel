package Evennement.Controller;

import Evennement.entities.Organisateur;
import Evennement.services.OrganisateurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AjouterOrganisateur {

    @FXML
    private TextField txtnomor; // TextField for organizer name

    @FXML
    private TextField txtcontact; // TextField for organizer contact

    @FXML
    private Button btnAjouterOrganisateur; // Button to add organizer

    private final OrganisateurService organisateurService = new OrganisateurService(); // Service instance

    @FXML
    void ajouterOrganisateur(ActionEvent event) {
        // Debug: Check if btnAjouterOrganisateur is null
        if (btnAjouterOrganisateur == null) {
            System.err.println("btnAjouterOrganisateur is null! Check FXML injection.");
            return;
        }

        // Get user input
        String nomOr = txtnomor.getText().trim(); // Organizer name
        String contactText = txtcontact.getText().trim(); // Organizer contact (as text)

        // Validate input
        if (nomOr.isEmpty() || contactText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
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

        // Create a new Organisateur object
        Organisateur newOrganisateur = new Organisateur(nomOr, contact);

        // Add the organizer to the database
        organisateurService.add(newOrganisateur);

        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Organisateur ajouté avec succès !");

        // Clear input fields
        clearFields();

        // Close the window
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        txtnomor.clear();
        txtcontact.clear();
    }
}