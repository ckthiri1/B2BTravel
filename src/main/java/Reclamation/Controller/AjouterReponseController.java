package Reclamation.Controller;

import Reclamation.entities.Reponse;
import Reclamation.Services.ReponseServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AjouterReponseController {

    private static final Logger logger = Logger.getLogger(AjouterReponseController.class.getName());

    @FXML
    private TextField DescriptionRepTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button alo;

    private int IDR; // ID de la réclamation associée

    private final ReponseServices reponseServices = new ReponseServices();


    @FXML
    private void enregistrerReponse() {
        // Retrieve values from fields
        String DescriptionRep = DescriptionRepTextField.getText().trim();
        LocalDate date = datePicker.getValue();

        // Check if all fields are filled
        if (DescriptionRep.isEmpty() || date == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        // Check if the date is today
        if (!date.isEqual(LocalDate.now())) {
            showAlert("Erreur", "La date doit être celle d'aujourd'hui !");
            return;
        }

        // Create and save the response
        Reponse reponse = new Reponse(DescriptionRep, date, this.IDR);
        int generatedId = reponseServices.addEntity(reponse);

        if (generatedId != -1) {
            showAlert("Succès", "Réponse ajoutée avec succès !");

            // Navigate to the list of responses
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeReponses.fxml"));
                Parent root = loader.load();

                // Display the new window
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Liste des Réponses");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Erreur lors du chargement de la vue ListeReponses.fxml : " + e.getMessage());
            }
        } else {
            showAlert("Erreur", "Erreur lors de l'ajout de la réponse.");
        }
    }
    /*private void naviguerVersListeReponse(int IDR) {
        try {
            // Charger la vue ListeReponse.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeReponses.fxml"));
            Parent root = loader.load();

            // Passer l'ID de la réclamation au contrôleur ListeReponseController
            ListeReponseController controller = loader.getController();
            controller.setReclamationId(IDR); // Assurez-vous que cette méthode existe dans ListeReponseController

            // Afficher la nouvelle fenêtre
            Stage stage = (Stage) DescriptionRepTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Réclamation et Réponse");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de ListeReponse.fxml : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur inattendue : " + e.getMessage());
        }
    }*/

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void ajouterReponse(ActionEvent actionEvent) {
        enregistrerReponse();
    }


    public void setIDR(int IDR) {
        this.IDR = IDR;
        logger.log(Level.INFO, "ID de la réclamation défini : " + this.IDR);
    }


    public void setReclamationId(int idr) {
    }
}