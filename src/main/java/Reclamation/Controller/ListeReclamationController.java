package Reclamation.Controller;

import Reclamation.entities.Reclamation;
import Reclamation.Services.ReclamationServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class ListeReclamationController {

    @FXML
    private TableView<Reclamation> reclamationTable;

    @FXML
    private TableColumn<Reclamation, Integer> idColumn;

    @FXML
    private TableColumn<Reclamation, String> titreColumn;

    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;

    @FXML
    private TableColumn<Reclamation, String> dateColumn;

    @FXML
    private TableColumn<Reclamation, Void> actionsColumn;

    @FXML
    private TableColumn<Reclamation, Void> modifierColumn;

    @FXML
    private TableColumn<Reclamation, Void> repondreColumn;

    private final ReclamationServices reclamationServices = new ReclamationServices();

    @FXML
    public void initialize() {

        // Configurer les colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("IDR"));
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("DateR"));

        // Configurer la colonne des actions (bouton de suppression)
        actionsColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Reclamation, Void> call(final TableColumn<Reclamation, Void> param) {
                return new TableCell<>() {
                    private final Button deleteButton = new Button("Supprimer");

                    {
                        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                        deleteButton.setOnAction(event -> {
                            Reclamation reclamation = getTableView().getItems().get(getIndex());
                            supprimerReclamation(reclamation);
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
        });

        // Configurer la colonne des actions (bouton de modification)
        modifierColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Reclamation, Void> call(final TableColumn<Reclamation, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = new Button("Modifier");

                    {
                        editButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
                        editButton.setOnAction(event -> {
                            Reclamation reclamation = getTableView().getItems().get(getIndex());
                            modifierReclamation(reclamation);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(editButton);
                        }
                    }
                };
            }
        });

        // Configurer la colonne des actions (bouton de réponse)
        repondreColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Reclamation, Void> call(final TableColumn<Reclamation, Void> param) {
                return new TableCell<>() {
                    private final Button repondreButton = new Button("Répondre");

                    {
                        repondreButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                        repondreButton.setOnAction(event -> {
                            Reclamation reclamation = getTableView().getItems().get(getIndex());
                            repondreAReclamation(reclamation);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(repondreButton);
                        }
                    }
                };
            }
        });

        // Charger les données dans la TableView
        reclamationTable.setItems(reclamationServices.getAllData());
    }

    private void supprimerReclamation(Reclamation reclamation) {
        boolean isDeleted = reclamationServices.deleteEntity(reclamation);
        if (isDeleted) {
            reclamationTable.getItems().remove(reclamation);
            System.out.println("Réclamation supprimée avec succès !");
        } else {
            System.out.println("Erreur lors de la suppression de la réclamation.");
        }
    }

    private void modifierReclamation(Reclamation reclamation) {
        try {
            // Load the update view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateReclamation.fxml"));
            Parent root = loader.load();

            // Get the update controller and pass the selected reclamation
            UpdateReclamationController updateController = loader.getController();
            updateController.setReclamationAModifier(reclamation);

            // Open the update window and wait until it closes
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Réclamation");
            stage.showAndWait();

            // Refresh the table view after modification
            refreshTable();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement du fichier FXML pour la mise à jour de la réclamation : " + e.getMessage());
        }
    }



    private void repondreAReclamation(Reclamation reclamation) {
        try {
            // Charger la vue AjouterReponse.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReponse.fxml"));
            Parent root = loader.load();

            // Passer l'ID de la réclamation au contrôleur AjouterReponseController
            AjouterReponseController controller = loader.getController();
            controller.setIDR(reclamation.getIDR());

            // Afficher la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une réponse");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la vue AjouterReponse.fxml : " + e.getMessage());
        }
    }

    public void refreshTable() {
        // Clear and reload the data in the table
        reclamationTable.getItems().clear();
        reclamationTable.setItems(reclamationServices.getAllData());
    }
}