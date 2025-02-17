package Evennement.Controller;

import Evennement.entities.Evennement;
import Evennement.entities.Organisateur;
import Evennement.services.OrganisateurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class ListeOrganisateur {

    @FXML
    private TableView<Organisateur> tableViewOrganisateur;

    @FXML
    private TableColumn<Organisateur, String> colNomOrganisateur;

    @FXML
    private TableColumn<Organisateur, Integer> colContact;

    @FXML
    private TableColumn<Organisateur, Void> colActionOrganisateur;

    private final OrganisateurService organisateurService = new OrganisateurService();

    @FXML
    public void initialize() {
        // Initialize columns
        colNomOrganisateur.setCellValueFactory(new PropertyValueFactory<>("nomOr"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));

        // Initialize the action column with a custom cell factory
        colActionOrganisateur.setCellFactory(createButtonCellFactory());

        // Load organizers when the controller initializes
        loadOrganisateurs();
    }

    @FXML
    private void loadOrganisateurs() {
        List<Organisateur> organisateursList = organisateurService.getAllData();
        ObservableList<Organisateur> observableList = FXCollections.observableArrayList(organisateursList);
        tableViewOrganisateur.setItems(observableList);
    }

    private Callback<TableColumn<Organisateur, Void>, TableCell<Organisateur, Void>> createButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Organisateur, Void> call(final TableColumn<Organisateur, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Supprimer");

                    {
                        deleteButton.setOnAction(event -> {
                            Organisateur organisateur = getTableView().getItems().get(getIndex());
                            organisateurService.delete(organisateur.getIDOr()); // Delete from database
                            tableViewOrganisateur.getItems().remove(organisateur); // Remove from TableView

                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            }
        };
    }

    @FXML
    private void InterfaceUpdate(ActionEvent event) {
        Organisateur selectedEvent = tableViewOrganisateur.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                // Load UpdateEvennement.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateOrganisateur.fxml"));
                Parent root = loader.load();

                // Get controller and pass the selected event
                UpdateOrganisateur updateController = loader.getController();
                updateController.setEvennementData(selectedEvent);

                // Show new scene
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Organisateur");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void interfaceAjoutO(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterOrganisateur.fxml"));
            Parent root = loader.load();

            // Create a new stage for the "Ajouter Organisateur" window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Organisateur");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Show an error alert if the FXML file cannot be loaded
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'interface d'ajout.");
        }
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}