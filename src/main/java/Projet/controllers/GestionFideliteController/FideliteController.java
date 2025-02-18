package Projet.controllers.GestionFideliteController;

import Projet.entities.Fidelite;
import Projet.entities.Utilisateur;
import Projet.services.FideliteService;
import Projet.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class FideliteController {

        @FXML
        private Text txtNbVoyage;
        @FXML
        private Text txtPoints;
        @FXML
        private Text txtRemise;
        @FXML
        private Text txtRank;

        private FideliteService fideliteService;
        private UserService userService;
        private int userId = 1;

        public FideliteController() {
                this.fideliteService = new FideliteService();
                this.userService = new UserService();
        }

        @FXML
        public void initialize() {
                System.out.println("FideliteController initialized.");
                loadUserData();
        }

        private void loadUserData() {
                if (userId > 0) {
                        try {
                                System.out.println("Loading data for user id: " + userId);

                                Utilisateur user = userService.getUserById(userId);
                                if (user != null) {

                                        System.out.println("User found: " + user.getIdUser());
                                        incrementTripsAndUpdateFidelite(userId);

                                        user = userService.getUserById(userId);
                                        Fidelite fidelite = fideliteService.getFideliteByUser(userId);

                                        if (fidelite != null) {
                                                txtNbVoyage.setText(String.valueOf(user.getNbrVoyage()));
                                                txtPoints.setText(String.valueOf(fidelite.getPoints()));
                                                txtRemise.setText(String.valueOf(fidelite.getRemise()));
                                                txtRank.setText(fidelite.getRang().getNomRank());
                                                System.out.println("Data bound to UI.");
                                        } else {
                                                System.out.println("Fidelite not found for user ID: " + userId);
                                        }
                                } else {
                                        System.out.println("User not found with ID: " + userId);
                                }
                        } catch (Exception e) {
                                System.err.println("Error loading user data: " + e.getMessage());
                                e.printStackTrace();
                        }
                } else {
                        System.out.println("User ID is not set.");
                }
        }

        private void incrementTripsAndUpdateFidelite(int userId) {
                Utilisateur user = userService.getUserById(userId);
                if (user != null) {

                        user.incrementNbrVoyage();
                        System.out.println("User trips incremented. New trip count: " + user.getNbrVoyage());

                        Fidelite fidelite = fideliteService.getFideliteByUser(userId);
                        if (fidelite == null) {
                                System.out.println("No existing fidelity record. Creating new record for user " + userId);
                                fideliteService.addFidelite(user);
                        } else {
                                System.out.println("Existing fidelity record found. Updating record for user " + userId);
                                fideliteService.updateFidelite(user);
                        }
                } else {
                        System.out.println("User not found with ID: " + userId);
                }
        }
}
