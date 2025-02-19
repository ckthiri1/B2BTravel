package Evennement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class homepage {

    @FXML
    private void interfaceEvennement(ActionEvent event) {
        try {
            // Load ListEvennement.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeEvennement.fxml"));
            Parent root = loader.load();

            // Create a new stage for the "List Evennement" window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Événements");
            stage.show();
            stage.setFullScreen(true);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface des événements.");
        }
    }

    @FXML
    private void interfaceOrganisateur(ActionEvent event) {
        try {
            // Load ListOrganisateur.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeOrganisateur.fxml"));
            Parent root = loader.load();

            // Create a new stage for the "List Organisateur" window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Organisateurs");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface des organisateurs.");
        }
    }

    /**
     * Displays an alert dialog.
     *
     * @param title   The title of the alert.
     * @param content The content message of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}