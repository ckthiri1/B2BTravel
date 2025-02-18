package CRUD.controllers;

import CRUD.entities.Hebergement;
import CRUD.entities.Reservation;
import CRUD.services.ReservationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ModifierReservationC {

    @FXML
    private DatePicker dateT;

    @FXML
    private SplitMenuButton idH;

    @FXML
    private Button modifier;

    @FXML
    private TextField prixT;

    @FXML
    private Button retour;

    @FXML
    private SplitMenuButton status;

    @FXML
    void modifier(ActionEvent event) {
        try {
            // Retrieve and validate the selected date
            LocalDate date = dateT.getValue();
            if (date == null) {
                showAlert("Erreur", "Veuillez sélectionner une date.");
                return;
            }
            LocalDateTime dateTime = date.atTime(LocalTime.now());

            // Retrieve and validate the price
            int prix = Integer.parseInt(prixT.getText());

            // Retrieve and validate the selected hebergement
            Integer idHValue = (Integer) idH.getUserData();
            if (idHValue == null) {
                showAlert("Erreur", "Veuillez sélectionner un hébergement.");
                return;
            }

            // Retrieve the selected status
            String statusValue = status.getText();

            // Update the reservation object
            selectedReservation.setDate_reservation(dateTime);
            selectedReservation.setPrix(prix);
            selectedReservation.setHebergement_id(idHValue);
            selectedReservation.setHebergementName(idH.getText()); // Ensure the name is updated
            selectedReservation.setStatus(statusValue);

            // Update the reservation in the database
            ReservationService reservationService = new ReservationService();
            reservationService.updateEntity(selectedReservation.getId_reservation(), selectedReservation);

            // Close the current window
            Stage stage = (Stage) modifier.getScene().getWindow();
            stage.close();

            // Refresh the reservation list in the parent controller
            parentController.showReservations();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }

    }

    private void showAlert(String erreur, String s) {
    }

    private Reservation selectedReservation;
    private AjouterReservationC parentController; // Référence pour rafraîchir la TableView

    public void initData(Reservation reservation, AjouterReservationC parent) {
        selectedReservation = reservation;
        parentController = parent;

        // Set date and price
        dateT.setValue(reservation.getDate_reservation().toLocalDate());
        prixT.setText(String.valueOf(reservation.getPrix()));

        // Fetch available accommodations and populate menu
        ReservationService reservationService = new ReservationService();
        List<Hebergement> hebergements = reservationService.getAllHebergements();
        setupHebergementMenu(hebergements);

        // Pre-select the current accommodation
        idH.setText(reservation.getHebergementName());
        idH.setUserData(reservation.getHebergement_id());

        // Set up status menu
        setupStatusMenu();
    }
    private void setupStatusMenu() {
        status.getItems().clear(); // Clear existing items

        // Add status options
        String[] statusOptions = {"EnAttente","Resolue"};
        for (String option : statusOptions) {
            MenuItem item = new MenuItem(option);
            item.setOnAction(event -> {
                status.setText(option); // Update the button's displayed text
                status.setUserData(option); // Store the selected value
            });
            status.getItems().add(item);
        }
    }
    private void setupHebergementMenu(List<Hebergement> hebergements) {
        idH.getItems().clear(); // Clear existing items

        for (Hebergement hebergement : hebergements) {
            MenuItem item = new MenuItem(hebergement.getNom());
            item.setOnAction(event -> {
                idH.setText(hebergement.getNom()); // Update displayed text
                idH.setUserData(hebergement.getId_hebergement()); // Store the selected value
            });
            idH.getItems().add(item);
        }
    }

    @FXML
    void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Affichage.fxml")); // Page précédente
            Parent root = loader.load();

            // Récupérer la scène actuelle et remplacer son contenu
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du retour : " + e.getMessage());
        }
    }

}
