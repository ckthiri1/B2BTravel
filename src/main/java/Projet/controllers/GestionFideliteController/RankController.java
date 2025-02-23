package Projet.controllers.GestionFideliteController;

import Projet.entities.Rank;
import Projet.services.RankService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.Optional;

public class RankController {

    private final RankService rankService = new RankService();
    public TextField NomRank;
    public TextArea Points;
    public Button btnAdd;
    public Button RankButton;
    public VBox rankContainer;

    @FXML
    public void initialize() {
        if (rankContainer != null) {
            loadRanks();
        }
    }
    @FXML
    void addRank(ActionEvent event) {
        try {
            String nomRankText = NomRank.getText();
            String pointsText = Points.getText();


            if (nomRankText == null || nomRankText.trim().isEmpty() ||
                    pointsText == null || pointsText.trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "⚠️ Error", "Veuillez remplir tous les champs!");
                return;
            }


            int points;
            try {
                points = Integer.parseInt(pointsText.trim());
                if (points <= 0) {
                    showAlert(Alert.AlertType.ERROR, "⚠️ Error", "Points must be greater than 0!");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "⚠️ Error", "Points must be a valid number!");
                return;
            }

            // Check if rank name already exists, using -1 as excludeId since this is a new rank
            if (rankService.existsByRankName(nomRankText.trim(), -1)) {
                showAlert(Alert.AlertType.WARNING, "⚠️ Warning", "This rank name already exists!");
                return;
            }


            Rank r = new Rank();
            r.setNomRank(nomRankText.trim());
            r.setPoints(points);


            rankService.addRank(r);

            showAlert(Alert.AlertType.INFORMATION, "✅ Success", "Rank added successfully!");

            // Clear fields
            NomRank.clear();
            Points.clear();

            // Navigate to list view
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion Fidélité/ListRank.fxml"));
                Parent listVoyageRoot = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(listVoyageRoot));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "⚠️ Navigation Error", "Could not load ListRank.fxml");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "⚠️ Error", "An error occurred while adding the rank");
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
    private void handleRankButtonAction() {
        try {
            Parent ajouterPage = FXMLLoader.load(getClass().getResource("/Gestion Fidélité/AjouterRank.fxml"));

            Scene ajouterScene = new Scene(ajouterPage);
            Stage currentStage = (Stage) RankButton.getScene().getWindow();
            currentStage.setScene(ajouterScene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRanks() {
        List<Rank> ranks = rankService.getAllRank();
        rankContainer.getChildren().clear();

        for (Rank rank : ranks) {
            Pane rankPane = createRankPane(rank);
            rankContainer.getChildren().add(rankPane);
        }
    }

    private Pane createRankPane(Rank rank) {
        Pane pane = new Pane();
        pane.setPrefSize(1250, 100);
        pane.setStyle("-fx-background-color: #03045f; -fx-background-radius: 15;");


        Text numberText = new Text(String.valueOf(rank.getIDRang()));
        numberText.setFill(Color.WHITE);
        numberText.setLayoutX(40);
        numberText.setLayoutY(55);
        numberText.setFont(Font.font("Arial Bold", 23));

        // Vertical Line
        Line line = new Line(110, 20, 110, 80);
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(3.0);

        // "RANK:" Label
        Text rankLabel = new Text("RANK:");
        rankLabel.setFill(Color.WHITE);
        rankLabel.setLayoutX(160);
        rankLabel.setLayoutY(55);
        rankLabel.setFont(Font.font("Arial Bold", 23));

        // Rank Name
        Text rankName = new Text(rank.getNomRank());
        rankName.setFill(Color.WHITE);
        rankName.setLayoutX(280);
        rankName.setLayoutY(55);
        rankName.setFont(Font.font("Arial Bold", 21));

        // Buttons (Spaced Out)
        Button updateButton = createStyledButton("UPDATE", 1000, 30, 110, 40);
        updateButton.setOnAction(event -> showUpdateDialog(rank));

        Button deleteButton = createStyledButton("DELETE", 1120, 30, 110, 40);
        deleteButton.setOnAction(event -> DeleteRank(rank));

        pane.getChildren().addAll(numberText, line, rankLabel, rankName, updateButton, deleteButton);
        return pane;
    }

    private Button createStyledButton(String text, double x, double y, double width, double height) {
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setPrefSize(width, height);
        button.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: white; " +
                "-fx-border-width: 2; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 10; " +
                "-fx-cursor: hand;");

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2); " +
                        "-fx-border-color: white; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;" +
                        "-fx-scale-x: 1.05; " +
                        "-fx-scale-y: 1.05;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-border-color: white; " +
                        "-fx-border-width: 2; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        ));

        return button;
    }

    private void DeleteRank(Rank rank) {
        try {
            rankService.deleteRank(rank.getIDRang());
            loadRanks();
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Rank deleted successfully!"
            );
        } catch (Exception e) {
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Failed to delete rank: " + e.getMessage()
            );
        }
    }

    private void showUpdateDialog(Rank rank) {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Update Rank");
            dialog.setHeaderText(null);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField rankNameField = new TextField(rank.getNomRank());

            // Change TextArea to TextField for points and initialize with current points
            TextField pointsField = new TextField(String.valueOf(rank.getPoints()));

            grid.add(new Label("Rank Name:"), 0, 0);
            grid.add(rankNameField, 1, 0);
            grid.add(new Label("Points:"), 0, 1);
            grid.add(pointsField, 1, 1);

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

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == updateButtonType) {

                if (rankNameField.getText() == null || rankNameField.getText().trim().isEmpty() ||
                        pointsField.getText() == null || pointsField.getText().trim().isEmpty()) {

                    showAlert(
                            Alert.AlertType.WARNING,
                            "Warning",
                            "Veuillez remplir tous les champs"
                    );
                    return;
                }

                try {
                    Integer.parseInt(pointsField.getText().trim());
                } catch (NumberFormatException e) {
                    showAlert(
                            Alert.AlertType.WARNING,
                            "Warning",
                            "Points must be a valid number"
                    );
                    return;
                }

                String newRankName = rankNameField.getText().trim();
                boolean rankExists = rankService.existsByRankName(newRankName, rank.getIDRang());

                if (rankExists) {
                    showAlert(
                            Alert.AlertType.WARNING,
                            "Warning",
                            "This rank name already exists!"
                    );
                    return;
                }

                rank.setNomRank(newRankName);
                rank.setPoints(Integer.parseInt(pointsField.getText().trim()));

                try {
                    rankService.updateRank(rank);
                    loadRanks();

                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Rank updated successfully!"
                    );
                } catch (Exception e) {
                    showAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            "Failed to update rank: " + e.getMessage()
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

}
