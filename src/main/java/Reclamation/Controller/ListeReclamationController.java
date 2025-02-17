package Reclamation.Controller;

import Reclamation.entities.Reclamation;
import Reclamation.Services.ReclamationServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListeReclamationController implements Initializable {

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
    private TableColumn<Reclamation, Void> actionsColumn; // Colonne pour le bouton "Supprimer"

    @FXML
    private TableColumn<Reclamation, Void> modifierColumn; // Colonne pour le bouton "Modifier"

    private final ReclamationServices reclamationServices = new ReclamationServices();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurer les colonnes existantes
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
                        editButton.setOnAction(event -> {
                            Reclamation reclamation = getTableView().getItems().get(getIndex());
                            ouvrirFormulaireModification(reclamation);
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

        // Charger toutes les réclamations au démarrage
        loadReclamations();
    }

    @FXML
    private void loadReclamations() {
        List<Reclamation> reclamationsList = reclamationServices.getAllData();
        ObservableList<Reclamation> observableList = FXCollections.observableArrayList(reclamationsList);
        reclamationTable.setItems(observableList);
    }

    public void refreshTable() {
        loadReclamations();
    }

    private void supprimerReclamation(Reclamation reclamation) {
        boolean isDeleted = reclamationServices.deleteEntity(reclamation);
        if (isDeleted) {
            loadReclamations();
            System.out.println("Réclamation supprimée avec succès !");
        } else {
            System.out.println("Erreur lors de la suppression de la réclamation.");
        }
    }

    /**
     * Ouvre le formulaire de modification pour la réclamation sélectionnée.
     *
     * @param reclamation La réclamation à modifier.
     */
    private void ouvrirFormulaireModification(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateReclamation.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Passer la réclamation sélectionnée au contrôleur de modification
            UpdateReclamationController controller = loader.getController();
            controller.setReclamationAModifier(reclamation);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'ouverture du formulaire de modification : " + e.getMessage());
        }
    }
}