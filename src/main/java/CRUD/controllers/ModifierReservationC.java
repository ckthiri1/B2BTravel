package CRUD.controllers;

import CRUD.entities.Hebergement;
import CRUD.entities.Reservation;
import CRUD.services.ReservationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private Label successMessage, msg1, msg2, msg3, msg4;

    private Reservation selectedReservation;

    @FXML
    void modifier(ActionEvent event) {
        try {
            // Validation de la date (doit être sélectionnée et ne pas être dans le passé)
            LocalDate date = dateT.getValue();
            if (date == null || date.isBefore(LocalDate.now())) {
                afficherMessageErreur(msg1, "Veuillez entrer une date valide (future ou aujourd'hui).");
                return;
            }
            LocalDateTime dateTime = date.atStartOfDay(); // Ajout d'une heure par défaut

            // Validation du prix (doit être un nombre positif)
            String prixTexte = prixT.getText().trim();
            if (prixTexte.isEmpty() || !prixTexte.matches("\\d+")) {
                afficherMessageErreur(msg2, "Veuillez entrer un prix valide.");
                return;
            }
            int prix = Integer.parseInt(prixTexte);

            // Vérification qu'un hébergement est sélectionné
            if (idH.getUserData() == null) {
                afficherMessageErreur(msg4, "Veuillez sélectionner un hébergement.");
                return;
            }
            Integer idHValue = (Integer) idH.getUserData();

            // Vérification qu'un statut est sélectionné
            if (status.getUserData() == null) {
                afficherMessageErreur(msg3, "Veuillez sélectionner un statut.");
                return;
            }
            String statusValue = (String) status.getUserData();

            // Mise à jour de l'objet réservation
            selectedReservation.setDate_reservation(dateTime);
            selectedReservation.setPrix(prix);
            selectedReservation.setHebergement_id(idHValue);
            selectedReservation.setHebergementName(idH.getText()); // Mise à jour du nom
            selectedReservation.setStatus(statusValue);

            // Mise à jour dans la base de données
            ReservationService reservationService = new ReservationService();
            reservationService.updateEntity(selectedReservation.getId_reservation(), selectedReservation);

            // Affichage du message de succès
            afficherMessageSucces("Réservation modifiée avec succès !");

        } catch (Exception e) {
            afficherMessageErreur(successMessage, "Une erreur est survenue : " + e.getMessage());
        }
    }

    private void afficherMessageErreur(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                label.setVisible(false);
            } catch (InterruptedException ignored) {}
        }).start();
    }

    private void afficherMessageSucces(String message) {
        successMessage.setText(message);
        successMessage.setVisible(true);
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                successMessage.setVisible(false);
            } catch (InterruptedException ignored) {}
        }).start();
    }

    public void initData(Reservation reservation, AffichageReservation tableCell) {
        selectedReservation = reservation;

        // Pré-remplissage des champs
        dateT.setValue(reservation.getDate_reservation().toLocalDate());
        prixT.setText(String.valueOf(reservation.getPrix()));

        // Chargement des hébergements et du statut
        ReservationService reservationService = new ReservationService();
        List<Hebergement> hebergements = reservationService.getAllHebergements();
        setupHebergementMenu(hebergements);
        setupStatusMenu();

        // Pré-sélectionner l'hébergement et le statut actuel
        idH.setText(reservation.getHebergementName());
        idH.setUserData(reservation.getHebergement_id());
        status.setText(reservation.getStatus());
        status.setUserData(reservation.getStatus());
    }

    private void setupStatusMenu() {
        status.getItems().clear();
        String[] statusOptions = {"EnAttente", "Resolue"};
        for (String option : statusOptions) {
            MenuItem item = new MenuItem(option);
            item.setOnAction(event -> {
                status.setText(option);
                status.setUserData(option);
            });
            status.getItems().add(item);
        }
    }

    private void setupHebergementMenu(List<Hebergement> hebergements) {
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

    @FXML
    void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Affichage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du retour : " + e.getMessage());
        }
    }
}
