package CRUD.controllers;

import CRUD.entities.Hebergement;
import CRUD.entities.Reservation;
import CRUD.services.HebergementService;
import CRUD.services.ReservationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import javafx.scene.Node;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.EventObject;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class AffichageReservation {
    @FXML
    private TableView<Reservation> tableView;

    @FXML
    private TableColumn<Reservation, String> hebergementColumn;
    @FXML
    private TableColumn<Reservation, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Reservation, Integer> prixColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;
    @FXML
    private TableColumn<Reservation, Void> actionTV;
    @FXML
    private Button retour;

    @FXML
    private Button listeH;
    @FXML
    private Label successMessage;
    @FXML
    private VBox reservationContainer;
    @FXML

    private ScrollPane scrollPane;
    @FXML
    private ImageView addIcon;
    @FXML
    private GridPane reservationGrid;

    private final ReservationService reservationService = new ReservationService();
    private final HebergementService hebergementService = new HebergementService();

/*
    @FXML
    void initialize() {
        hebergementColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHebergementName()));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_reservation"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        ImageView backIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/fleche-de-retour.png"))));
        backIcon.setFitWidth(15);
        backIcon.setFitHeight(15);

        retour.setGraphic(backIcon);
        actionTV.setCellFactory(param -> new TableCell<>() {
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
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    boolean deleted = reservationService.deleteEntity(reservation);
                    if (deleted) {
                        getTableView().getItems().remove(reservation);
                    } else {
                        System.out.println("Échec de la suppression de la réservation.");
                    }
                });

                editIcon.setOnMouseClicked(event -> {
                    try {
                        // Récupérer la réservation sélectionnée
                        Reservation selectedReservation = tableView.getSelectionModel().getSelectedItem();
                        if (selectedReservation == null) {
                            successMessage.setText( "Erreur ,Veuillez sélectionner une réservation à modifier.");
                            successMessage.setVisible(true);
                            new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    successMessage.setVisible(false);
                                } catch (InterruptedException ignored) {}
                            }).start();
                            return;
                        }

                        // Charger la page ModifierReservation.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
                        Parent root = loader.load();

                        // Récupérer le contrôleur de la page et passer les données
                        ModifierReservationC controller = loader.getController();
                        controller.initData(selectedReservation, this);

                        // Ouvrir la nouvelle fenêtre
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
        showReservations();
    }
   */

    @FXML
    void initialize() {
        showReservations();
        addIcon.setOnMouseClicked(event -> consulter(new ActionEvent(addIcon, null)));

        addIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/plus-symbole-noir1.png"))));
        addIcon.setFitWidth(20);
        addIcon.setFitHeight(20);
    }

    void showReservations() {
        reservationGrid.getChildren().clear();
        List<Reservation> reservations = reservationService.getAllData();

        int columns = 3; // Nombre de colonnes par ligne
        int row = 0;
        int col = 0;

        for (Reservation reservation : reservations) {
            Hebergement hebergement = hebergementService.getById(reservation.getHebergement_id());
            if (hebergement != null) {
                reservation.setHebergementName(hebergement.getNom());
            } else {
                reservation.setHebergementName("Hébergement supprimé");
            }

            // Créer une carte pour chaque réservation
            VBox reservationCard = createReservationCard(reservation);
            reservationGrid.add(reservationCard, col, row);

            col++;
            if (col == columns) { // Passer à la ligne suivante après X colonnes
                col = 0;
                row++;
            }
        }
    }

    private VBox createReservationCard(Reservation reservation) {
        VBox card = new VBox(10); // Espacement entre les éléments
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 10; -fx-padding: 15; -fx-alignment: center;");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200); // Largeur fixe pour chaque carte

        // Labels (centrés)
        Label hebergementLabel = new Label(reservation.getHebergementName());
        Label dateLabel = new Label("📅 " + reservation.getDate_reservation().toString());
        Label prixLabel = new Label("💰 " + reservation.getPrix() + " €");
        Label statusLabel = new Label("📌 " + reservation.getStatus());

        hebergementLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        dateLabel.setStyle("-fx-font-size: 12px;");
        prixLabel.setStyle("-fx-font-size: 12px;");
        statusLabel.setStyle("-fx-font-size: 12px;");

        // Icônes Modifier et Supprimer
        ImageView editIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/editer1.png"))));
        editIcon.setFitWidth(25);
        editIcon.setFitHeight(25);
        editIcon.setOnMouseClicked(event -> editReservation(reservation));

        ImageView deleteIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bouton-supprimer.png"))));
        deleteIcon.setFitWidth(25);
        deleteIcon.setFitHeight(25);
        deleteIcon.setOnMouseClicked(event -> deleteReservation(reservation));

        HBox actionsBox = new HBox(15, editIcon, deleteIcon);
        actionsBox.setAlignment(Pos.CENTER);

        // Ajouter les éléments à la carte verticalement
        card.getChildren().addAll(hebergementLabel, dateLabel, prixLabel, statusLabel, actionsBox);

        return card;
    }


    private void editReservation(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            Parent root = loader.load();
            ModifierReservationC controller = loader.getController();
            controller.initData(reservation, this);

            Stage stage = (Stage) reservationGrid.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors de l’ouverture de la modification : " + e.getMessage());
        }
    }

    private void deleteReservation(Reservation reservation) {
        boolean deleted = reservationService.deleteEntity(reservation);
        if (deleted) {
            showReservations(); // Rafraîchir l'affichage après suppression
        } else {
            System.out.println("Échec de la suppression de la réservation.");
        }
    }

   /* void showReservations() {
        tableView.getItems().clear();
        List<Reservation> reservations = reservationService.getAllData();
        for (Reservation reservation : reservations) {
            Hebergement hebergement = hebergementService.getById(reservation.getHebergement_id());
            if (hebergement != null) {
                reservation.setHebergementName(hebergement.getNom());
            } else {
                reservation.setHebergementName("Hebergement supprimé");
            }
            tableView.getItems().add(reservation);
        }
    }*/


    public void consulter(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle et remplacer son contenu
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page AjouterReservation: " + e.getMessage());
        }
    }


    public void Retour(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml")); // Page précédente
            Parent root = loader.load();

            // Récupérer la scène actuelle et remplacer son contenu
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du retour : " + e.getMessage());
        }
    }

    public void ListeH(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageHebergement.fxml")); // Page précédente
            Parent root = loader.load();
            final ImageView Liste = new ImageView();
            Liste.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/liste-de-controle.png"))));
            Liste.setFitWidth(25);
            Liste.setFitHeight(25);
            // Récupérer la scène actuelle et remplacer son contenu
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du retour : " + e.getMessage());
        }
    }
}
