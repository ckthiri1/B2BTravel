package Evennement.Controller;

import Evennement.entities.Organisateur;
import Evennement.services.OrganisateurService;
import Evennement.services.SMS;
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
    private final SMS smsService = new SMS(); // SMS service instance


    @FXML
    void ajouterOrganisateur(ActionEvent event) {
        // Get user input
        String nomOr = txtnomor.getText().trim(); // Organizer name
        String contactText = txtcontact.getText().trim(); // Organizer contact (as text)

        // Validate input
        if (nomOr.isEmpty() || contactText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // Validate contact input (must be a valid phone number)
        if (!contactText.matches("\\d{8}")) { // Example: 8 digits for Tu
            // nisian numbers
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le contact doit être un numéro de téléphone valide (8 chiffres).");
            return;
        }

        // Format the phone number with +216 (only for SMS)
        String formattedPhoneNumber = "+216" + contactText;

        // Create a new Organisateur object
        Organisateur newOrganisateur = new Organisateur(nomOr, contactText);

        // Add the organizer to the database
        organisateurService.add(newOrganisateur);

        // Send an SMS to the organizer
        String message = "Bonjour " + nomOr + ", bienvenue à B2B Travel !";
        smsService.sendSms(formattedPhoneNumber, message);

        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Organisateur ajouté avec succès et SMS envoyé !");

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