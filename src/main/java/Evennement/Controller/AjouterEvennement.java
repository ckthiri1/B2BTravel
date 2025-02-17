package Evennement.Controller;

import Evennement.entities.Evennement;
import Evennement.services.EvennementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.util.Date;

public class AjouterEvennement {

    @FXML
    private TextField txtNomE;

    @FXML
    private TextField txtLocal;

    @FXML
    private DatePicker datePickerDateE;

    @FXML
    private TextField txtDesE;

    @FXML
    private Button btnAjouter;

    // Create an instance of EvennementService
    private final EvennementService evennementService = new EvennementService();

    @FXML
    void ajouterEvennement(ActionEvent event) {
        // Get user input
        String nomE = txtNomE.getText().trim();
        String local = txtLocal.getText().trim();
        String desE = txtDesE.getText().trim();
        if (datePickerDateE.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date.");
            return;
        }


        Date dateE = java.sql.Date.valueOf(datePickerDateE.getValue());
        if (nomE.isEmpty() || local.isEmpty() || desE.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        Evennement newEvent = new Evennement(nomE, local, desE, dateE);
        evennementService.add(newEvent);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement ajouté avec succès !");
        clearFields();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        txtNomE.clear();
        txtLocal.clear();
        datePickerDateE.setValue(null);
        txtDesE.clear();
    }
}
