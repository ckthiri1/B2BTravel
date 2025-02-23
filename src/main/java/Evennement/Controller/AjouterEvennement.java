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
import javafx.scene.control.*;
import javafx.util.StringConverter;

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
    private ComboBox<Organisateur> comboOrganisateur;  // ComboBox for selecting an Organisateur

    @FXML
    private ComboBox<EventType> comboEventType;  // ComboBox for selecting EventType

    private final EvennementService evennementService = new EvennementService();
    private final OrganisateurService organisateurService = new OrganisateurService();

    @FXML
    public void initialize() {
        loadOrganisateurs();
        loadEventTypes();
    }

    private void loadOrganisateurs() {
        List<Organisateur> organisateurs = organisateurService.getAllData();
        ObservableList<Organisateur> observableList = FXCollections.observableArrayList(organisateurs);
        comboOrganisateur.setItems(observableList);

        // Display only the Organisateur name in ComboBox
        comboOrganisateur.setConverter(new StringConverter<Organisateur>() {
            @Override
            public String toString(Organisateur organisateur) {
                return (organisateur != null) ? organisateur.getNomOr() : "";
            }

            @Override
            public Organisateur fromString(String string) {
                return comboOrganisateur.getItems().stream()
                        .filter(o -> o.getNomOr().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        System.out.println("📌 Organisateurs loaded: " + organisateurs.size());
    }

    private void loadEventTypes() {
        ObservableList<EventType> eventTypes = FXCollections.observableArrayList(EventType.values());
        comboEventType.setItems(eventTypes);

        // Ensure no default selection is set
        comboEventType.getSelectionModel().clearSelection();
    }

    @FXML
    void ajouterEvennement(ActionEvent event) {
        String nomE = txtNomE.getText().trim();
        String local = txtLocal.getText().trim();
        String desE = txtDesE.getText().trim();

        System.out.println("NomE: " + nomE);
        System.out.println("Local: " + local);
        System.out.println("DesE: " + desE);

        if (nomE.isEmpty() || local.isEmpty() || desE.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return;
        }

        if (datePickerDateE.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une date.");
            return;
        }

        Date dateE = Date.valueOf(datePickerDateE.getValue());
        Organisateur selectedOrganisateur = comboOrganisateur.getValue();
        EventType selectedEventType = comboEventType.getValue();  // Get selected event type

        System.out.println("Selected Organisateur: " + selectedOrganisateur);
        System.out.println("Selected EventType: " + selectedEventType);

        if (selectedOrganisateur == null || selectedOrganisateur.getIDOr() == 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un organisateur valide.");
            return;
        }

        if (selectedEventType == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un type d'événement.");
            return;
        }

        // Use the constructor that includes eventType
        Evennement evennement = new Evennement(nomE, local, desE, dateE, selectedOrganisateur, selectedEventType);

        try {
            evennementService.add(evennement);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement ajouté avec succès !");
            clearFields();
            btnAjouter.getScene().getWindow().hide();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
            e.printStackTrace();
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
        comboEventType.getSelectionModel().clearSelection();  // Clear event type selection
    }
}