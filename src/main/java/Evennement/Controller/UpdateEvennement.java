package Evennement.Controller;

import Evennement.entities.Evennement;
import Evennement.entities.Organisateur;
import Evennement.services.EvennementService;
import Evennement.services.OrganisateurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
public class UpdateEvennement {

    @FXML
    private TextField txtNomE;

    @FXML
    private TextField txtLocal;

    @FXML
    private DatePicker datePickerDateE;

    @FXML
    private TextField txtDesE;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<Organisateur> comboOrganisateurUpdate;  // ComboBox for selecting an Organisateur

    private Evennement selectedEvennement;
    private EvennementService evennementService = new EvennementService();
    private OrganisateurService organisateurService = new OrganisateurService();  // Assuming you have a service for Organisateur

    // Setter for the Evennement to be updated
    public void setEvennement(Evennement evennement) {
        this.selectedEvennement = evennement;
        setEvennementData();
    }

    // Set the current Evennement data into the form and load Organisateurs into the ComboBox
    public void setEvennementData() {
        if (selectedEvennement != null) {
            txtNomE.setText(selectedEvennement.getNomE());
            txtLocal.setText(selectedEvennement.getLocal());
            txtDesE.setText(selectedEvennement.getDesE());

            // Convert Date to LocalDate for DatePicker
            LocalDate localDate = new java.sql.Date(selectedEvennement.getDateE().getTime()).toLocalDate();
            datePickerDateE.setValue(localDate);

            // Load Organisateurs into ComboBox
            loadOrganisateurs();

            // Select the Organisateur that is currently assigned to the Evennement
            Organisateur selectedOrganisateur = selectedEvennement.getOrganisateur(); // Get the Organisateur from Evennement
            comboOrganisateurUpdate.setValue(selectedOrganisateur);
        }
    }

    // Load all Organisateurs into the ComboBox
    private void loadOrganisateurs() {
        List<Organisateur> organisateurs = organisateurService.getAllData();  // Fetch all Organisateurs
        ObservableList<Organisateur> observableList = FXCollections.observableArrayList(organisateurs);
        comboOrganisateurUpdate.setItems(observableList);
    }

    // Handle the update action when the button is clicked
    @FXML
    void updateEv(ActionEvent event) {
        if (selectedEvennement == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun événement sélectionné.");
            return;
        }

        // Get updated values
        String nomE = txtNomE.getText().trim();
        String local = txtLocal.getText().trim();
        LocalDate dateE = datePickerDateE.getValue();
        String desE = txtDesE.getText().trim();
        Organisateur organisateur = comboOrganisateurUpdate.getValue();  // Get the selected Organisateur

        // Validate input fields
        if (nomE.isEmpty() || local.isEmpty() || dateE == null || desE.isEmpty() || organisateur == null) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Convert LocalDate to Timestamp for the database
        Timestamp timestampDateE = Timestamp.valueOf(dateE.atStartOfDay());

        // Update the selected Evennement with the new values
        selectedEvennement.setNomE(nomE);
        selectedEvennement.setLocal(local);
        selectedEvennement.setDateE(new Date(timestampDateE.getTime())); // Convert Timestamp to Date
        selectedEvennement.setDesE(desE);
        selectedEvennement.setOrganisateur(organisateur);  // Set the selected Organisateur

        // Call the service method to update the Evennement in the database
        evennementService.update(selectedEvennement, selectedEvennement.getIDE());

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement mis à jour avec succès !");

        // Close the window after updating
        btnUpdate.getScene().getWindow().hide();
    }

    // Helper method to show alerts
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
