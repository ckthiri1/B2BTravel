package Evennement.Controller;

import Evennement.entities.Evennement;
import Evennement.entities.Organisateur;
import Evennement.entities.EventType;  // Import EventType
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
    private ComboBox<Organisateur> comboOrganisateurUpdate;

    @FXML
    private ComboBox<EventType> comboEventUpdate;

    private Evennement selectedEvennement;
    private EvennementService evennementService = new EvennementService();
    private OrganisateurService organisateurService = new OrganisateurService();
    public void setEvennement(Evennement evennement) {
        this.selectedEvennement = evennement;
        setEvennementData();
    }
    public void setEvennementData() {
        if (selectedEvennement != null) {
            txtNomE.setText(selectedEvennement.getNomE());
            txtLocal.setText(selectedEvennement.getLocal());
            txtDesE.setText(selectedEvennement.getDesE());

            // Convert Date to LocalDate for DatePicker
            LocalDate localDate = new java.sql.Date(selectedEvennement.getDateE().getTime()).toLocalDate();
            datePickerDateE.setValue(localDate);

            // Load Organisateurs and EventTypes into ComboBoxes
            loadOrganisateurs();
            loadEventTypes();

            // Select the Organisateur and EventType that are currently assigned to the Evennement
            comboOrganisateurUpdate.setValue(selectedEvennement.getOrganisateur());
            comboEventUpdate.setValue(selectedEvennement.getEventType()); // Set the selected EventType

            // Debugging: Verify the initial EventType
            System.out.println("Initial EventType in ComboBox: " + selectedEvennement.getEventType());

            // Add a listener to the ComboBox
            comboEventUpdate.valueProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("ComboBox EventType changed from " + oldValue + " to " + newValue);
            });
        }
    }

    private void loadOrganisateurs() {
        List<Organisateur> organisateurs = organisateurService.getAllData();
        ObservableList<Organisateur> observableList = FXCollections.observableArrayList(organisateurs);
        comboOrganisateurUpdate.setItems(observableList);
    }private void loadEventTypes() {
        ObservableList<EventType> eventTypes = FXCollections.observableArrayList(EventType.values());
        comboEventUpdate.setItems(eventTypes);
    }



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
        Organisateur organisateur = comboOrganisateurUpdate.getValue();
        EventType eventType = comboEventUpdate.getValue();


        // Debugging: Verify the selected EventType
        System.out.println("Selected EventType from ComboBox: " + eventType);

        // Validate input fields
        if (nomE.isEmpty() || local.isEmpty() || dateE == null || desE.isEmpty() || organisateur == null || eventType == null) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Convert LocalDate to Timestamp for the database
        Timestamp timestampDateE = Timestamp.valueOf(dateE.atStartOfDay());

        // Update the selected Evennement with the new values
        selectedEvennement.setNomE(nomE);
        selectedEvennement.setLocal(local);
        selectedEvennement.setDateE(new Date(timestampDateE.getTime()));
        selectedEvennement.setDesE(desE);
        selectedEvennement.setOrganisateur(organisateur);
        selectedEvennement.setEventType(eventType); // Set the selected EventType

        // Debugging: Verify the eventType set in Evennement
        System.out.println("EventType set in Evennement object: " + selectedEvennement.getEventType());

        // Call the service method to update the Evennement in the database
        evennementService.update(selectedEvennement, selectedEvennement.getIDE());

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement mis à jour avec succès !");

        // Close the window after updating
        btnUpdate.getScene().getWindow().hide();
    }









    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}