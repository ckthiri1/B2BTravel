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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ListeReponseController {

    @FXML
    private TableView<Reponse> reponsesTable;

    // Column to display the reclamation title (retrieved via join)
    @FXML
    private TableColumn<Reponse, String> colRec;

    // Column to display the response description
    @FXML
    private TableColumn<Reponse, String> colRep;

    private final ReponseServices reponseServices = new ReponseServices();
    private int reclamationId;

    @FXML
    void retour(ActionEvent event) {
        try {
            // Charger la vue AjouterReponse.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReponse.fxml"));
            Parent root = loader.load();


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

    @FXML
    public void initialize() {
        // Bind the columns to the corresponding properties of Reponse.
        colRec.setCellValueFactory(new PropertyValueFactory<>("reclamationTitre"));
        colRep.setCellValueFactory(new PropertyValueFactory<>("descriptionRep"));

        // Optionally, load all responses if no specific reclamation is selected.
        if (reclamationId <= 0) {
            loadEvennements();
        }
    }

    /**
     * Loads all responses with their reclamation title.
     */
    private ObservableList<Reponse> loadEvennements() {
        ObservableList<Reponse> reponses = FXCollections.observableArrayList();
        String query = "SELECT rec.Titre AS reclamationTitre, r.DescriptionRep " +
                "FROM Reponse r " +
                "JOIN Reclamation rec ON r.IDR = rec.IDR";
        try (Statement stmt = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String reclamationTitre = rs.getString("reclamationTitre");
                String descriptionRep = rs.getString("DescriptionRep");
                Reponse reponse = new Reponse(reclamationTitre, descriptionRep);
                reponses.add(reponse);
            }

            System.out.println("Loaded responses (all): " + reponses.size());
            reponsesTable.setItems(reponses);

        } catch (SQLException e) {
            System.out.println("❌ SQL Error while fetching responses: " + e.getMessage());
        }
        return reponses;
    }

    /**
     * Loads the responses for a specific reclamation.
     */
    private void chargerDonnees() {
        ObservableList<Reponse> reponses = FXCollections.observableArrayList(
                reponseServices.getReponsesByReclamation(reclamationId)
        );
        System.out.println("Loaded responses for reclamation " + reclamationId + ": " + reponses.size());
        reponsesTable.setItems(reponses);
    }

    /**
     * Sets the reclamation ID and refreshes the table.
     *
     * @param idr the reclamation ID passed from the previous page.
     */
    public void setReclamationId(int idr) {
        this.reclamationId = idr;
        chargerDonnees();
    }
}
