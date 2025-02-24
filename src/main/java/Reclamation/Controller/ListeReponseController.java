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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class ListeReponseController {

    @FXML
    private VBox reponsesVBox; // VBox pour contenir les réponses

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
        loadEvennements(); // Charger les réponses au démarrage
    }

    private void loadEvennements() {
        // Vider le conteneur avant de charger de nouvelles données
        reponsesVBox.getChildren().clear();

        // Ajouter l'en-tête de la table
        HBox header = new HBox(20);
        header.setStyle("-fx-padding: 10px;");
        header.getChildren().addAll(
                createStyledLabel("Reponse", "header-label"),
                createStyledLabel("Titre réclamation", "header-label"),
                createStyledLabel("Modifier", "header-label"),
                createStyledLabel("Supprimer", "header-label")
        );
        reponsesVBox.getChildren().add(header);

        // Récupérer la liste des réponses depuis le service
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

                // Ensure the Reponse object is constructed with the correct fields
                Reponse reponse = new Reponse(IDRep, descriptionRep, reclamationTitre);
                reponses.add(reponse);
            }
            // Ajouter chaque réponse dans une nouvelle ligne du VBox
            for (Reponse reponse : reponses) {
                // Créer des labels pour chaque champ
                Label reponseLabel = createStyledLabel(reponse.getDescriptionRep(), "row-label");
                Label titreLabel = createStyledLabel(reponse.getReclamationTitre(), "row-label");

                // Créer des boutons pour les actions
                Button deleteButton = new Button("Supprimer");
                deleteButton.getStyleClass().add("button");
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> supprimerReponse(reponse));

                Button editButton = new Button("Modifier");
                editButton.getStyleClass().add("button");
                editButton.setOnAction(event -> modifierReponse(reponse));

                // Créer une nouvelle HBox pour chaque ligne de réponse
                HBox row = new HBox(20);
                row.setStyle("-fx-padding: 10px;");
                row.getChildren().addAll(reponseLabel, titreLabel, editButton, deleteButton);
                reponsesVBox.getChildren().add(row);
            }

        } catch (SQLException e) {
            System.out.println("❌ SQL Error while fetching responses: " + e.getMessage());
        }
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
                loadEvennements(); // Recharger la liste après la suppression
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText("Suppression échouée");
                errorAlert.setContentText("Une erreur est survenue lors de la suppression de la réponse.");
                errorAlert.showAndWait();
            }
        }
    }
    @FXML
    private void modifierReponse(Reponse reponse) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReponse.fxml"));
            Parent root = loader.load();

            // Passer l'objet Reponse au contrôleur de modification
            ModifierReponseController controller = loader.getController();
            controller.setReponse(reponse); // Ensure this reponse has the correct DescriptionRep

            // Afficher la fenêtre de modification
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Réponse");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'ouverture de ModifierReponse.fxml : " + e.getMessage());
        }
    }
    private Label createStyledLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    public void setReclamationId(int idr) {
        this.reclamationId = idr;
        chargerDonnees();
    }

    private void chargerDonnees() {
        ObservableList<Reponse> reponses = FXCollections.observableArrayList(
                reponseServices.getReponsesByReclamation(reclamationId)
        );
        System.out.println("Loaded responses for reclamation " + reclamationId + ": " + reponses.size());
        loadEvennements();
    }

    private void refreshTable() {
        // Recharger les données depuis la base de données
        loadEvennements();
    }
}
