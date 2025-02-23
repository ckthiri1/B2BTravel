package Reclamation.Controller;

import Reclamation.entities.Reponse;
import Reclamation.Services.ReponseServices;
import Reclamation.tools.MyConnection;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class ListeReponseController {

    @FXML
    private TableView<Reponse> reponsesTable;

    @FXML
    private TableColumn<Reponse, String> colRec;

    @FXML
    private TableColumn<Reponse, String> colRep;

    @FXML
    private TableColumn<Reponse, Void> colSupprimer;

    @FXML
    private TableColumn<Reponse, Void> colModifier;

    private final ReponseServices reponseServices = new ReponseServices();
    private int reclamationId;

    @FXML
    void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReponse.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une réponse");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la vue AjouterReponse.fxml : " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        colRec.setCellValueFactory(new PropertyValueFactory<>("reclamationTitre"));
        colRep.setCellValueFactory(new PropertyValueFactory<>("descriptionRep"));

        addModifyButtonToTable();
        addDeleteButtonToTable();

        if (reclamationId <= 0) {
            loadEvennements();
        }
    }

    private void addDeleteButtonToTable() {
        Callback<TableColumn<Reponse, Void>, TableCell<Reponse, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(event -> {
                    Reponse reponse = getTableView().getItems().get(getIndex());
                    supprimerReponse(reponse);
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        };

        colSupprimer.setCellFactory(cellFactory);
    }

    private void addModifyButtonToTable() {
        Callback<TableColumn<Reponse, Void>, TableCell<Reponse, Void>> cellFactory = param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setOnAction(event -> {
                    Reponse reponse = getTableView().getItems().get(getIndex());
                    modifierReponse(reponse);
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        };

        colModifier.setCellFactory(cellFactory);
    }

    private void supprimerReponse(Reponse reponse) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la réponse");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réponse ?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean isDeleted = reponseServices.deleteEntity(reponse);
            if (isDeleted) {
                reponsesTable.getItems().remove(reponse);
                System.out.println("Réponse supprimée avec succès.");
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText("Suppression échouée");
                errorAlert.setContentText("Une erreur est survenue lors de la suppression de la réponse.");
                errorAlert.showAndWait();
            }
        }
    }

    private void modifierReponse(Reponse reponse) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReponse.fxml"));
            Parent root = loader.load();

            ModifierReponseController controller = loader.getController();
            controller.setReponse(reponse); // Pass the whole object

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Réponse");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'ouverture de ModifierReponse.fxml : " + e.getMessage());
        }
    }

    private ObservableList<Reponse> loadEvennements() {
        ObservableList<Reponse> reponses = FXCollections.observableArrayList();
        String query = "SELECT r.IDRep, rec.Titre AS reclamationTitre, r.DescriptionRep " +
                "FROM Reponse r " +
                "JOIN Reclamation rec ON r.IDR = rec.IDR";
        try (Statement stmt = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int IDRep = rs.getInt("IDRep"); // Fetching the ID
                String reclamationTitre = rs.getString("reclamationTitre");
                String descriptionRep = rs.getString("DescriptionRep");

                Reponse reponse = new Reponse(IDRep, reclamationTitre, descriptionRep);
                reponses.add(reponse);
            }

            System.out.println("Loaded responses (all): " + reponses.size());
            reponsesTable.setItems(reponses);

        } catch (SQLException e) {
            System.out.println("❌ SQL Error while fetching responses: " + e.getMessage());
        }
        return reponses;
    }


    private void chargerDonnees() {
        ObservableList<Reponse> reponses = FXCollections.observableArrayList(
                reponseServices.getReponsesByReclamation(reclamationId)
        );
        System.out.println("Loaded responses for reclamation " + reclamationId + ": " + reponses.size());
        reponsesTable.setItems(reponses);

    }

    public void setReclamationId(int idr) {
        this.reclamationId = idr;
        chargerDonnees();
    }
    private void refreshTable() {
        // Clear the current items in the TableView
        reponsesTable.getItems().clear();

        // Reload the data from the database
        ObservableList<Reponse> reponses = FXCollections.observableArrayList(
                reponseServices.getReponsesByReclamation(reclamationId)
        );

        // Set the updated data in the TableView
        reponsesTable.setItems(reponses);
    }
}
