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
    private Button btnUpdate;

    private final EvennementService evennementService = new EvennementService(); // Service instance

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomE"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateE"));
        colDes.setCellValueFactory(new PropertyValueFactory<>("desE"));

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
    private void updateEvennement(ActionEvent event) {
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

}