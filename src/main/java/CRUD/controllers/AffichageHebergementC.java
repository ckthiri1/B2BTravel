package CRUD.controllers;

import CRUD.entities.Hebergement;
import CRUD.services.HebergementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javafx.scene.Node;
public class AffichageHebergementC {

    @FXML
    private TableColumn<Hebergement, Void> actionColumn;


    @FXML
    private TableColumn<Hebergement, String> adresseColumn;

    @FXML
    private Button ajouterHebergement;
    @FXML
    private Button listeR;

    @FXML
    private Button actualiser;
    @FXML
    private TableColumn<Hebergement, String > descriptionColumn;

    @FXML
    private TableColumn<Hebergement, String> nomColumn;

    @FXML
    private Button retour;
    @FXML
    private GridPane hebergementGrid;

    @FXML
    private TableView<Hebergement> tableView;
    @FXML
    private Label successMessage;
    @FXML
    private ImageView addIcon;
    @FXML
    private TableColumn<Hebergement, String> typeColumn;
    private final HebergementService hebergementService = new HebergementService();
/*
    @FXML
    void initialize() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        ImageView backIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/fleche-de-retour.png"))));
        backIcon.setFitWidth(15);
        backIcon.setFitHeight(15);

        retour.setGraphic(backIcon);
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView deleteIcon = new ImageView();
            private final ImageView editIcon = new ImageView();
            private final HBox buttonBox = new HBox(20, editIcon, deleteIcon);

            {
                // Charger les images (Assurez-vous que les fichiers sont bien placés dans /resources/images/)
                deleteIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bouton-supprimer.png"))));
                editIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/editer1.png"))));

                // Définir la taille des icônes
                deleteIcon.setFitWidth(25);
                deleteIcon.setFitHeight(25);
                editIcon.setFitWidth(25);
                editIcon.setFitHeight(25);

                // Ajouter les actions
                deleteIcon.setOnMouseClicked(event -> {
                    Hebergement hebergement = getTableView().getItems().get(getIndex());
                    boolean deleted = hebergementService.deleteEntity(hebergement);
                    if (deleted) {
                        getTableView().getItems().remove(hebergement);
                    } else {
                        System.out.println("Échec de la suppression de la réservation.");
                    }
                });

                editIcon.setOnMouseClicked(event -> {
                    try {
                        // Récupérer la réservation sélectionnée depuis la cellule actuelle
                        Hebergement selectedHebergement =tableView.getSelectionModel().getSelectedItem();

                        if (selectedHebergement == null) {
                            successMessage.setText("Erreur, veuillez sélectionner un hébergement à modifier.");
                            successMessage.setVisible(true);
                            new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    successMessage.setVisible(false);
                                } catch (InterruptedException ignored) {}
                            }).start();
                            return;
                        }

                        // Charger la page ModifierHebergement.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierHebergement.fxml"));
                        Parent root = loader.load();

                        // Récupérer le contrôleur de la page et passer les données
                        ModifierHebergement controller = loader.getController();
                        controller.initData(selectedHebergement, this);

                        Stage stage = (Stage) ((ImageView) event.getSource()).getScene().getWindow();
                        stage.getScene().setRoot(root);
                    } catch (IOException e) {
                        System.out.println("Erreur lors de l’ouverture de la modification : " + e.getMessage());
                    }
                });


            }



            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        });
        showHebergement();
    }

    private void showHebergement() {
        List<Hebergement> hebergements = hebergementService.getAllData(); // Récupération des données
        tableView.getItems().setAll(hebergements); // Mise à jour de la TableView
    }
*/
@FXML
void initialize() {
    showHebergement();
    addIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/plus-symbole-noir1.png"))));
    addIcon.setFitWidth(20);
    addIcon.setFitHeight(20);
    addIcon.setOnMouseClicked(event -> ajouterHebergement(event));

}
    private void showHebergement() {
        hebergementGrid.getChildren().clear();
        List<Hebergement> hebergements = hebergementService.getAllData();

        int columns = 3; // Number of columns per row
        int row = 0;
        int col = 0;

        for (Hebergement hebergement : hebergements) {
            VBox hebergementCard = createHebergementCard(hebergement);
            hebergementGrid.add(hebergementCard, col, row);

            col++;
            if (col == columns) { // Move to the next row after X columns
                col = 0;
                row++;
            }
        }
    }

    private VBox createHebergementCard(Hebergement hebergement) {
        VBox card = new VBox(10); // Spacing between elements
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 10; -fx-padding: 15; -fx-alignment: center;");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200); // Fixed width for each card

        // Labels (centered)
        Label nomLabel = new Label(hebergement.getNom());
        Label adresseLabel = new Label("📍 " + hebergement.getAdresse());
        Label typeLabel = new Label("🏠 " + hebergement.getType());
        Label descriptionLabel = new Label("📃 " + hebergement.getDescription());

        nomLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        adresseLabel.setStyle("-fx-font-size: 12px;");
        typeLabel.setStyle("-fx-font-size: 12px;");
        descriptionLabel.setStyle("-fx-font-size: 12px;");

        // Edit and Delete icons
        ImageView editIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/editer1.png"))));
        editIcon.setFitWidth(25);
        editIcon.setFitHeight(25);
        editIcon.setOnMouseClicked(event -> editHebergement(hebergement));

        ImageView deleteIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bouton-supprimer.png"))));
        deleteIcon.setFitWidth(25);
        deleteIcon.setFitHeight(25);
        deleteIcon.setOnMouseClicked(event -> deleteHebergement(hebergement));

        HBox actionsBox = new HBox(15, editIcon, deleteIcon);
        actionsBox.setAlignment(Pos.CENTER);

        // Add elements to the card vertically
        card.getChildren().addAll(nomLabel, adresseLabel, typeLabel, descriptionLabel, actionsBox);

        return card;
    }

    private void editHebergement(Hebergement hebergement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierHebergement.fxml"));
            Parent root = loader.load();
            ModifierHebergement controller = loader.getController();
            controller.initData(hebergement, this);

            Stage stage = (Stage) hebergementGrid.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors de l’ouverture de la modification : " + e.getMessage());
        }
    }

    private void deleteHebergement(Hebergement hebergement) {
        boolean deleted = hebergementService.deleteEntity(hebergement);
        if (deleted) {
            showHebergement(); // Refresh the display after deletion
        } else {
            System.out.println("Échec de la suppression de l'hébergement.");
        }
    }

    @FXML
    void Retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterHebergement.fxml")); // Page précédente
            Parent root = loader.load();

            // Récupérer la scène actuelle et remplacer son contenu
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du retour : " + e.getMessage());
        }
    }


    @FXML
    void ajouterHebergement(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterHebergement.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle et remplacer son contenu
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page AjouterReservation: " + e.getMessage());
        }
    }
    @FXML
    void Listeres(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Affichage.fxml")); // Page précédente
            Parent root = loader.load();
            final ImageView Liste = new ImageView();
            Liste.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/liste-de-controle.png"))));
            Liste.setFitWidth(25);
            Liste.setFitHeight(25);
            // Récupérer la scène actuelle et remplacer son contenu
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("Erreur lors du retour : " + e.getMessage());
        }
    }

    @FXML
    void actualiserH(ActionEvent event) {

        showHebergement();

    }

}


