package Reclamation.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SideBarController {

    @FXML
    void NavigateToGestionVoyage(ActionEvent event){
        loadPage("/Voyage/ListVoyage.fxml", "Gestion des Voyages", event);
    }

    @FXML
    void NavigateToGestionFidelite(ActionEvent event){
        loadPage("/Fidelite/ListRank.fxml", "Gestion de la Fidélité", event);
    }

    @FXML
    void NavigateToGestionReclamation(ActionEvent event){
        loadPage("/Reclamation/AjouterReclamation.fxml", "Gestion des Réclamations", event);
    }

    // Méthode générique pour charger une page
    private void loadPage(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent contentRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(contentRoot));
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
