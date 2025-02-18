package CRUD.controllers;

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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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


    private final ReservationService reservationService = new ReservationService();
    private final HebergementService hebergementService = new HebergementService();


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
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml")); // Page précédente
                        Parent root = loader.load();

                        // Récupérer la scène actuelle et remplacer son contenu
                        Stage stage = (Stage) ((ImageView) event.getSource()).getScene().getWindow();
                        stage.getScene().setRoot(root);
                    } catch (IOException e) {
                        System.out.println("Erreur lors du retour : " + e.getMessage());
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

    private void showReservations() {
        tableView.getItems().clear();
        List<Reservation> reservations = reservationService.getAllData();
        for (Reservation reservation : reservations) {
            reservation.setHebergementName(hebergementService.getById(reservation.getHebergement_id()).getNom());
            tableView.getItems().add(reservation);
        }
    }


    public void consulter(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root)); // Remplace la scène actuelle
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterHebergement.fxml")); // Page précédente
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
