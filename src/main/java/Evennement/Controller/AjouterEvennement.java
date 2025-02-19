package Evennement.Controller;

import Evennement.entities.Evennement;
import Evennement.entities.Organisateur;
import Evennement.services.EvennementService;
import Evennement.services.OrganisateurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.util.List;

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

    @FXML
    private ComboBox<Organisateur> comboOrganisateur; // ComboBox for selecting an Organisateur

    private final EvennementService evennementService = new EvennementService();
    private final OrganisateurService organisateurService = new OrganisateurService();

    @FXML
    public void initialize() {
        loadOrganisateurs();
    }

    private void loadOrganisateurs() {
        List<Organisateur> organisateurs = organisateurService.getAllData();
        ObservableList<Organisateur> observableList = FXCollections.observableArrayList(organisateurs);
        comboOrganisateur.setItems(observableList);

        // Debugging: Check if organisateurs are loaded
        System.out.println("Organisateurs loaded: " + organisateurs.size());
    }


    @FXML
    void ajouterEvennement(ActionEvent event) {
        String nomE = txtNomE.getText().trim();
        String local = txtLocal.getText().trim();
        String desE = txtDesE.getText().trim();

        if (datePickerDateE.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date.");
            return;
        }

        Date dateE = Date.valueOf(datePickerDateE.getValue());

        if (nomE.isEmpty() || local.isEmpty() || desE.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        Organisateur selectedOrganisateur = comboOrganisateur.getValue();
        System.out.println("Selected Organisateur: " + selectedOrganisateur);
        if (selectedOrganisateur == null || selectedOrganisateur.getIDOr() == 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un organisateur valide.");
            return;
        }


        // Debugging: Print selected Organisateur ID to confirm it is valid
        System.out.println("📌 Selected Organisateur ID: " + selectedOrganisateur.getIDOr());

        Evennement evennement = new Evennement(nomE, local, desE, dateE, selectedOrganisateur);

// Debugging: Print event before insertion
        System.out.println("📝 Created Event (Before Insertion): " + evennement);

// Check if the 'Organisateur' is correctly assigned
        if (evennement.getOrganisateur() == null) {
            System.out.println("ERROR: Organisateur is null in the Evennement constructor!");
        } else if (evennement.getOrganisateur().getIDOr() == 0) {
            System.out.println("ERROR: Organisateur ID is 0 in the Evennement!");
        } else {
            System.out.println("Success: Organisateur correctly assigned.");
        }

// Continue with insertion if valid
        if (evennement.getOrganisateur() != null && evennement.getOrganisateur().getIDOr() != 0) {
            try {
                evennementService.add(evennement);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement ajouté avec succès !");
                clearFields();
                btnAjouter.getScene().getWindow().hide();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout de l'événement: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'événement n'a pas été ajouté car l'organisateur est invalide.");
        }
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
        comboOrganisateur.getSelectionModel().clearSelection();
    }
}
