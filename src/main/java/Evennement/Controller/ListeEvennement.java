package Evennement.Controller;

import Evennement.entities.Evennement;
import Evennement.entities.Organisateur;
import Evennement.services.EvennementService;
import Evennement.tools.MyConnection;
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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    private TableColumn<Evennement, String> NomOr;

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
        colAction.setCellFactory(createButtonCellFactory());
        NomOr.setCellValueFactory(new PropertyValueFactory<>("NomOr"));



        // Load events when the controller initializes
        loadEvennements();
    }

    @FXML
    private List<Evennement> loadEvennements() {
        List<Evennement> events = new ArrayList<>();
        String query = "SELECT e.IDE, e.NomE, e.Local, e.DateE, e.DesE, o.IDOr, o.NomOr, o.Contact " +
                "FROM Evennement e " +
                "JOIN Organisateur o ON e.IDOr = o.IDOr";

        try {
            Statement stmt = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int IDE = rs.getInt("IDE");
                String nomE = rs.getString("NomE");
                String local = rs.getString("Local");
                String desE = rs.getString("DesE");
                Date dateE = new Date(rs.getTimestamp("DateE").getTime());

                // Retrieve the Organisateur details
                int IDOr = rs.getInt("IDOr");
                String nomOr = rs.getString("NomOr");
                int Contact = rs.getInt("Contact");

                // Create Organisateur object with IDOr, nomOr, and Contact
                Organisateur organisateur = new Organisateur(IDOr, nomOr, Contact);

                // Create Evennement object and set its properties
                Evennement evennement = new Evennement(nomE, local, desE, dateE, organisateur);
                evennement.setIDE(IDE); // Set the Evennement ID

                // Add the event to the list
                events.add(evennement);
            }

            // Create ObservableList and set the TableView items
            ObservableList<Evennement> observableList = FXCollections.observableArrayList(events);
            tableView.setItems(observableList); // Ensure tableView is the correct TableView instance

        } catch (SQLException e) {
            System.out.println("❌ SQL Error while fetching events: " + e.getMessage());
        }

        return events;
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