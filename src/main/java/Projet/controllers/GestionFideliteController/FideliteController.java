package Projet.controllers.GestionFideliteController;

import Projet.entities.Fidelite;
import Projet.entities.User;
import Projet.services.FideliteService;
import Projet.services.UserService;
import Projet.tools.UserSession;
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

        public FideliteController() {
                this.fideliteService = new FideliteService();
                this.userService = new UserService();
        }

        @FXML
        public void initialize() {
                System.out.println("FideliteController initialized.");
                int userId = UserSession.getInstance().getUserId();
                if (userId != -1) {
                        loadUserData();
                } else {
                        System.out.println("No user logged in.");
                }
        }


        private void loadUserData() {
                try {
                        User user = userService.getCurrentUser();
                        System.out.println("Loading data for current user");

                        System.out.println("User found: " + user.getUser_id());
                        incrementTripsAndUpdateFidelite();

                        // Refresh user data after update
                        user = userService.getCurrentUser();
                        Fidelite fidelite = fideliteService.getFideliteByUser(user.getUser_id());

                        if (fidelite != null) {
                                txtNbVoyage.setText(String.valueOf(user.getNbrVoyage()));
                                txtPoints.setText(String.valueOf(fidelite.getPoints()));
                                txtRemise.setText(String.valueOf(fidelite.getRemise()));
                                txtRank.setText(fidelite.getRang().getNomRank());
                                System.out.println("Data bound to UI.");
                        } else {
                                System.out.println("Fidelite not found for current user");
                        }
                } catch (RuntimeException e) {
                        System.err.println("Error loading user data: " + e.getMessage());
                        e.printStackTrace();
                }
        }

        private void incrementTripsAndUpdateFidelite() {
                try {
                        User user = userService.getCurrentUser();

                        user.incrementNbrVoyage();
                        System.out.println("User trips incremented. New trip count: " + user.getNbrVoyage());

                        Fidelite fidelite = fideliteService.getFideliteByUser(user.getUser_id());
                        if (fidelite == null) {
                                System.out.println("No existing fidelity record. Creating new record for current user");
                                fideliteService.addFidelite(user);
                        } else {
                                System.out.println("Existing fidelity record found. Updating record for current user");
                                fideliteService.updateFidelite(user);
                        }
                } catch (RuntimeException e) {
                        System.out.println("Error updating fidelity: " + e.getMessage());
                        e.printStackTrace();
                }
        }
}
