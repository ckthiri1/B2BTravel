package Evennement.Controller;

import Evennement.entities.Evennement;
import Evennement.services.EvennementService;
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

public class ListeEvennement {

    @FXML
    private TableView<Evennement> tableView;

    @FXML
    private TableColumn<Evennement, String> colNom;

    @FXML
    private TableColumn<Evennement, String> colLocal;

    @FXML
    private TableColumn<Evennement, String> colDate;

    @FXML
    private TableColumn<Evennement, String> colDes;

    @FXML
    private TableColumn<Evennement, Void> colAction; // New column for the delete button

    private final EvennementService evennementService = new EvennementService(); // Service instance

    @FXML
    public void initialize() {
        // Initialize columns
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomE"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateE"));
        colDes.setCellValueFactory(new PropertyValueFactory<>("desE"));

        // Initialize the action column with a custom cell factory
        colAction.setCellFactory(createButtonCellFactory());

        // Load events when the controller initializes
        loadEvennements();
    }

    @FXML
    private void loadEvennements() {
        List<Evennement> evennementsList = evennementService.getAllData();
        ObservableList<Evennement> observableList = FXCollections.observableArrayList(evennementsList);
        tableView.setItems(observableList);
    }

    @FXML
    private void interfaceAjout(ActionEvent event) {
        try {
            // Load AjouterEvennement.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvennement.fxml"));
            Parent root = loader.load();

            // Show new scene in a new window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Evennement");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface d'ajout.");
        }
    }

    @FXML
    private void InterfaceUpdate(ActionEvent event) {
        Evennement selectedEvent = tableView.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                // Load UpdateEvennement.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEvennement.fxml"));
                Parent root = loader.load();

                // Get controller and pass the selected event
                UpdateEvennement updateController = loader.getController();
                updateController.setEvennementData(selectedEvent);

                // Show new scene
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Evennement");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Avertissement", "Veuillez sélectionner un événement à modifier.");
        }
    }

    @FXML
    private void deleteEvennement(ActionEvent event) {
        Evennement selectedEvent = tableView.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            // Confirmation Alert
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    evennementService.delete(selectedEvent.getIDE()); // Call service delete method
                    loadEvennements(); // Refresh TableView
                    showAlert("Succès", "L'événement a été supprimé avec succès !");
                }
            });
        } else {
            showAlert("Erreur", "Veuillez sélectionner un événement à supprimer.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Custom cell factory for the delete button
    private Callback<TableColumn<Evennement, Void>, TableCell<Evennement, Void>> createButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Evennement, Void> call(final TableColumn<Evennement, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Supprimer");

                    {
                        deleteButton.setOnAction(event -> {
                            Evennement evennement = getTableView().getItems().get(getIndex());
                            evennementService.delete(evennement.getIDE()); // Delete from database
                            tableView.getItems().remove(evennement); // Remove from TableView
                            showAlert("Succès", "L'événement a été supprimé avec succès !");
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
}