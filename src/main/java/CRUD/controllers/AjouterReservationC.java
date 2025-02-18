package CRUD.controllers;

import CRUD.entities.Hebergement;
import CRUD.entities.Reservation;
import CRUD.services.HebergementService;
import CRUD.services.ReservationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AjouterReservationC {

    @FXML
    private DatePicker dateT;

    @FXML
    private SplitMenuButton idH;

    @FXML
    private TextField prixT;

    @FXML
    private TableView<Reservation> tableView;

    @FXML
    private SplitMenuButton status;

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

    private String selectedStatus = "EnAttente";
    private ReservationService reservationService = new ReservationService();
    private HebergementService hebergementService = new HebergementService();


    @FXML
    void initialize() {

        loadHebergements(); // Charger les hébergements disponibles
        setupStatusMenu();  // Initialiser le menu des statuts
    }

    private void loadHebergements() {
        List<Hebergement> hebergements = hebergementService.getAllData();
        idH.getItems().clear();

        for (Hebergement hebergement : hebergements) {
            MenuItem item = new MenuItem(hebergement.getNom());
            item.setOnAction(event -> {
                idH.setText(hebergement.getNom());
                idH.setUserData(hebergement.getId_hebergement());
            });
            idH.getItems().add(item);
        }
    }

    private void setupStatusMenu() {
        for (MenuItem item : status.getItems()) {
            item.setOnAction(event -> {
                selectedStatus = item.getText();
                status.setText(selectedStatus);
            });
        }
    }

    @FXML
    void ajouterR() {
        try {
            LocalDate date = dateT.getValue();
            if (date == null) {
                showAlert("Erreur", "Veuillez sélectionner une date.");
                return;
            }
            LocalDateTime dateTime = date.atTime(LocalTime.now());

            if (prixT.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez entrer un prix.");
                return;
            }
            int prix = Integer.parseInt(prixT.getText());

            if (idH.getUserData() == null) {
                showAlert("Erreur", "Veuillez sélectionner un hébergement.");
                return;
            }
            int idHValue = (int) idH.getUserData();

            String statusValue = status.getText();

            Reservation reservation = new Reservation(dateTime, prix, statusValue, idHValue);
            reservationService.addEntity(reservation);

            showAlert("Succès", "La réservation a été ajoutée avec succès.");

            //showReservations(); // Actualiser la liste après ajout

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }

    @FXML
    void List(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Affichage.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root)); // Remplace la scène actuelle
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page Affichage: " + e.getMessage());
        }
    }




    public  void showReservations() {
        tableView.getItems().clear();
        List<Reservation> reservations = reservationService.getAllData();

        for (Reservation reservation : reservations) {
            Hebergement hebergement = hebergementService.getById(reservation.getHebergement_id());
            reservation.setHebergementName(hebergement != null ? hebergement.getNom() : "Inconnu");
            tableView.getItems().add(reservation);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }



}
