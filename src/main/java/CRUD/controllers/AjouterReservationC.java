package CRUD.controllers;

import CRUD.entities.Hebergement;
import CRUD.entities.Reservation;
import CRUD.services.HebergementService;
import CRUD.services.ReservationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class AjouterReservationC {

    @FXML
    private DatePicker dateT;
    @FXML
    private SplitMenuButton idH;
    @FXML
    private TextField prixT;
    @FXML
    private SplitMenuButton status;
    @FXML
    private Label successMessage;
    @FXML
    private Label msg1, msg2, msg3, msg4;

    private String selectedStatus ;
    private final ReservationService reservationService = new ReservationService();
    private final HebergementService hebergementService = new HebergementService();

    @FXML
    void initialize() {
        loadHebergements();
        setupStatusMenu();
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
        clearMessages();
        try {
            LocalDate date = dateT.getValue();
            if (date == null || date.isBefore(LocalDate.now())) {
                showMessage(msg1, "Veuillez entrer une date valide (future ou aujourd'hui).", 3000);
                return;
            }
            LocalDateTime dateTime = date.atTime(LocalTime.now());

            if (prixT.getText().isEmpty() || !prixT.getText().matches("\\d+")) {
                showMessage(msg2, "Veuillez entrer un prix valide.", 3000);
                return;
            }
            int prix = Integer.parseInt(prixT.getText());
            if (prix <= 0) {
                showMessage(msg2, "Le prix doit être supérieur à 0.", 3000);
                return;
            }

            if (idH.getUserData() == null) {
                showMessage(msg4, "Veuillez sélectionner un hébergement.", 3000);
                return;
            }
            int idHValue = (int) idH.getUserData();
            if (selectedStatus == null || selectedStatus.isEmpty()) {
                showMessage(msg3, "Veuillez choisir un statut.", 3000);
                return;
            }
            List<String> validStatuses = Arrays.asList("EnAttente", "Resolue"); // Vérifier avec la BD
            if (!validStatuses.contains(selectedStatus)) {
                msg3.setText("Statut invalide. Choisissez EnAttente ou Resolue.");
                msg3.setVisible(true);
                return;
            }
            String statusValue = status.getText();

            Reservation reservation = new Reservation(dateTime, prix, selectedStatus, idHValue);
            reservationService.addEntity(reservation);

            showMessage(successMessage, "La réservation a été ajoutée avec succès.", 3000);
        } catch (Exception e) {
            showMessage(successMessage, "Erreur: " + e.getMessage(), 3000);
        }
    }

    private void showMessage(Label label, String message, int duration) {
        label.setText(message);
        label.setVisible(true);
        new Thread(() -> {
            try {
                Thread.sleep(duration);
                label.setVisible(false);
            } catch (InterruptedException ignored) {}
        }).start();
    }

    private void clearMessages() {
        msg1.setVisible(false);
        msg2.setVisible(false);
        msg3.setVisible(false);
        msg4.setVisible(false);
        successMessage.setVisible(false);
    }

    @FXML
    void List(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Affichage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page Affichage: " + e.getMessage());
        }
    }
}
