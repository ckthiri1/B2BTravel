package Evennement.Controller;

import Evennement.entities.Evennement;
import Evennement.services.EvennementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

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

    private Evennement selectedEvennement;
    private EvennementService evennementService = new EvennementService();

    public void setEvennementData(Evennement evennement) {
        this.selectedEvennement = evennement;
        txtNomE.setText(evennement.getNomE());
        txtLocal.setText(evennement.getLocal());
        txtDesE.setText(evennement.getDesE());

        // Convert Date to LocalDate for DatePicker
        LocalDate localDate = new java.sql.Date(evennement.getDateE().getTime()).toLocalDate();
        datePickerDateE.setValue(localDate);
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

        // Validate input
        if (nomE.isEmpty() || local.isEmpty() || dateE == null || desE.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        // Convert LocalDate to Timestamp
        Timestamp timestampDateE = Timestamp.valueOf(dateE.atStartOfDay());

        // Update event object
        selectedEvennement.setNomE(nomE);
        selectedEvennement.setLocal(local);
        selectedEvennement.setDateE(new Date(timestampDateE.getTime())); // Convert Timestamp to Date
        selectedEvennement.setDesE(desE);

        // Call the update function
        evennementService.update(selectedEvennement, selectedEvennement.getIDE());

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement mis à jour avec succès !");

        // Close the window after update
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