package Projet.controllers.GestionVoyageController;

import Projet.entities.Rank;
import Projet.entities.Voyage;
import Projet.services.VoyageService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;


public class VoyageController {

    public TextField Depart;
    public TextField Destination;
    public TextArea Description;
    public Button btnAdd;
    public VBox voyageContainer;
    @FXML
    private Button ajouterVoyageButton;

    private final VoyageService voyageService = new VoyageService();

    @FXML
    void addVoyage(ActionEvent event) {
        String depart = Depart.getText();;
        String destination = Destination.getText();;
        String description = Description.getText();

        if (depart.isEmpty() || destination.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "⚠️ Error", "All fields must be filled!");
            return;
        }

        if (voyageService.checkExisistance(depart.trim(),destination.trim(), -1)) {
            showAlert(Alert.AlertType.WARNING, "⚠️ Warning", "This rank name already exists!");
            return;
        }

        Voyage voyage = new Voyage();
        voyage.setDepart(depart);
        voyage.setDestination(destination);
        voyage.setDescription(description);

        voyageService.addVoyage(voyage);

        showAlert(Alert.AlertType.INFORMATION, "✅ Success", "Voyage added successfully!");

        Depart.clear();
        Destination.clear();
        Description.clear();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion Voyage/ListVoyage.fxml"));
            Parent listVoyageRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(listVoyageRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "⚠️ Navigation Error", "Could not load ListVoyage.fxml");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        if (voyageContainer != null) {
            loadVoyages();
        }
    }

    private void loadVoyages() {
        List<Voyage> voyages = voyageService.getAllVoyages();
        voyageContainer.getChildren().clear();

        for (Voyage voyage : voyages) {
            Pane voyagePane = createVoyagePane(voyage);
            voyageContainer.getChildren().add(voyagePane);
        }
    }

    private Pane createVoyagePane(Voyage voyage) {
        Pane pane = new Pane();
        pane.setPrefSize(1258, 100);
        pane.setStyle("-fx-background-color: #03045f; -fx-background-radius: 20;");

        Text departText = new Text("Departure : " + voyage.getDepart());
        departText.setFill(Color.WHITE);
        departText.setLayoutX(135);
        departText.setLayoutY(58);
        departText.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Text destinationText = new Text("Arriver : " + voyage.getDestination());
        destinationText.setFill(Color.WHITE);
        destinationText.setLayoutX(521);
        destinationText.setLayoutY(57);
        destinationText.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Button deleteButton = new Button("DELETE");
        deleteButton.setLayoutX(956);
        deleteButton.setLayoutY(30);
        deleteButton.setPrefSize(60, 40);
        deleteButton.setStyle("-fx-background-color: #f6f6f6; -fx-background-radius: 10;");
        deleteButton.setOnAction(event -> DeleteVoyage(voyage));

        Button updateButton = new Button("UPDATE");
        updateButton.setLayoutX(880);
        updateButton.setLayoutY(30);
        updateButton.setPrefSize(60, 40);
        updateButton.setStyle("-fx-background-color: #f6f6f6; -fx-background-radius: 10;");
        updateButton.setOnAction(event -> showUpdateVoyageDialog(voyage));

        Button checkButton = new Button("CHECK VOL");
        checkButton.setLayoutX(1030);
        checkButton.setLayoutY(30);
        checkButton.setPrefSize(160, 40);
        checkButton.setStyle("-fx-background-color: #f6f6f6; -fx-background-radius: 10;");
        checkButton.setOnAction(event -> showFlightsForVoyage(voyage));

        ImageView starImageView1 = new ImageView(new Image(getClass().getResourceAsStream("/Images/star.png")));
        starImageView1.setFitHeight(10);
        starImageView1.setFitWidth(10);
        starImageView1.setLayoutX(115);
        starImageView1.setLayoutY(46);

        ImageView starImageView2 = new ImageView(new Image(getClass().getResourceAsStream("/Images/star.png")));
        starImageView2.setFitHeight(10);
        starImageView2.setFitWidth(10);
        starImageView2.setLayoutX(503);
        starImageView2.setLayoutY(45);

        Line line = new Line(56, 52, 56, 52);
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(3);

        Text numberText = new Text(String.valueOf(voyage.getVID()));
        numberText.setFill(Color.WHITE);
        numberText.setLayoutX(28);
        numberText.setLayoutY(59);
        numberText.setFont(Font.font("Arial Bold", 22));

        pane.getChildren().addAll(departText, destinationText,updateButton, deleteButton,checkButton, starImageView1, starImageView2, line, numberText);

        return pane;
    }

    private void showUpdateVoyageDialog(Voyage voyage) {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Update Voyage");
            dialog.setHeaderText(null);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField departField = new TextField(voyage.getDepart());
            TextField destinationField = new TextField(voyage.getDestination());
            TextArea descriptionArea = new TextArea(voyage.getDescription());
            descriptionArea.setPrefRowCount(3);

            grid.add(new Label("Departure:"), 0, 0);
            grid.add(departField, 1, 0);
            grid.add(new Label("Destination:"), 0, 1);
            grid.add(destinationField, 1, 1);
            grid.add(new Label("Description:"), 0, 2);
            grid.add(descriptionArea, 1, 2);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getStylesheets().add(getClass().getResource("/Style/RankStyle.css").toExternalForm());

            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, cancelButtonType);

            dialog.getDialogPane().lookupButton(updateButtonType).setStyle(
                    "-fx-background-color: #03045f; -fx-text-fill: white; -fx-background-radius: 10;"
            );
            dialog.getDialogPane().lookupButton(cancelButtonType).setStyle(
                    "-fx-background-color: #f6f6f6; -fx-background-radius: 10;"
            );

            // Show dialog and handle result
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == updateButtonType) {
                // Check if any field is empty
                if (departField.getText().trim().isEmpty() ||
                        destinationField.getText().trim().isEmpty() ||
                        descriptionArea.getText().trim().isEmpty()) {

                    showAlert(
                            Alert.AlertType.WARNING,
                            "Warning",
                            "Veuillez remplir tous les champs"
                    );
                    return;
                }

                // Check if voyage with same depart and destination exists (excluding current voyage)
                String newDepart = departField.getText().trim();
                String newDestination = destinationField.getText().trim();

                boolean voyageExists = voyageService.checkExisistance(
                        newDepart,
                        newDestination,
                        voyage.getVID()  // Pass current voyage ID to exclude it from check
                );

                if (voyageExists) {
                    showAlert(
                            Alert.AlertType.WARNING,
                            "Warning",
                            "This voyage already exists!"
                    );
                    return;
                }

                // Update voyage object with new values
                voyage.setDepart(newDepart);
                voyage.setDestination(newDestination);
                voyage.setDescription(descriptionArea.getText().trim());

                try {
                    voyageService.updateVoyage(voyage);
                    loadVoyages();

                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Voyage updated successfully!"
                    );
                } catch (Exception e) {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            "Failed to update voyage: " + e.getMessage()
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "An error occurred while opening the update dialog"
            );
        }
    }

    private void DeleteVoyage(Voyage voyage) {
        voyageService.deleteVoyage(voyage.getVID());
        System.out.println(voyage.getVID());
        loadVoyages();
    }
    @FXML
    private void handleAjouterVoyageButtonAction() {
        try {
            Parent ajouterVoyagePage = FXMLLoader.load(getClass().getResource("/Gestion Voyage/ajouterVoyage.fxml"));

            Scene ajouterVoyageScene = new Scene(ajouterVoyagePage);
            Stage currentStage = (Stage) ajouterVoyageButton.getScene().getWindow();
            currentStage.setScene(ajouterVoyageScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void showFlightsForVoyage(Voyage voyage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion Voyage/ListVol.fxml"));
            Parent listVolPage = loader.load();

            VolController volController = loader.getController();
            volController.setVoyage(voyage);

            Stage stage = (Stage) voyageContainer.getScene().getWindow();
            stage.setScene(new Scene(listVolPage));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "⚠️ Error", "Could not load flights view: " + e.getMessage());
        }
    }
}