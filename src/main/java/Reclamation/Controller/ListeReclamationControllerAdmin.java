package Reclamation.Controller;

import Reclamation.entities.Reclamation;
import Reclamation.Services.ReclamationServices;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ListeReclamationControllerAdmin {

    @FXML
    private VBox reclamationsGrid; // VBox contenant les réclamations sous forme de GridPane

    private final ReclamationServices reclamationServices = new ReclamationServices();

    @FXML
    public void initialize() {
        loadReclamations(); // Charger les réclamations au démarrage
    }

    private void loadReclamations() {
        reclamationsGrid.getChildren().clear();

        List<Reclamation> reclamations = reclamationServices.getAllData();

        int rowIndex = 0;
        for (Reclamation reclamation : reclamations) {
            GridPane row = createReclamationRow(reclamation);
            reclamationsGrid.getChildren().add(row);
            rowIndex++;
        }
    }

    private GridPane createReclamationRow(Reclamation reclamation) {
        GridPane row = new GridPane();
        row.setHgap(10);
        row.setVgap(5);
        row.setStyle("-fx-border-color: #bdc3c7; -fx-border-width: 1px; -fx-padding: 10px;");

        Label titreLabel = new Label(reclamation.getTitre());
        titreLabel.setPrefWidth(150);

        Label descriptionLabel = new Label(reclamation.getDescription());
        descriptionLabel.setPrefWidth(200);

        Label dateLabel = new Label(reclamation.getDateR().toString());
        dateLabel.setPrefWidth(100);

        Button respondButton = new Button("Répondre");
        respondButton.setPrefWidth(150);
        respondButton.setOnAction(event -> repondreAReclamation(reclamation));

        row.add(titreLabel, 0, 0);
        row.add(descriptionLabel, 1, 0);
        row.add(dateLabel, 2, 0);
        row.add(respondButton, 3, 0);

        return row;
    }

    private void repondreAReclamation(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReponse.fxml"));
            Parent root = loader.load();

            AjouterReponseController controller = loader.getController();
            controller.setIDR(reclamation.getIDR());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une réponse");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}