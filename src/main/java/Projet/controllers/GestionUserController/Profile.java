package Projet.controllers.GestionUserController;


import Projet.tools.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import Projet.entities.User;

import static Projet.controllers.GestionUserController.Login.user;


public class Profile {

    @FXML
    private TextField Email_textfield;

    @FXML
    private Label Nom_Label;

    @FXML
    private TextField Nom_textfield;
    @FXML
    private TextField Prenom_textfield;
    @FXML
    private TextField Voyage_textfield;
    @FXML
    private Button Update;
    @FXML
    private Button logOut;
    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        // Load the current user's data when the profile page initializes
        User currentUser = UserSession.getInstance().getUser();
        if (currentUser != null) {
            setUser(currentUser);
        }
    }

    @FXML
    void logOut(ActionEvent event) {
        // Clear the session when logging out
        UserSession.getInstance().clearSession();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Gestion User/login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) logOut.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Update(ActionEvent event) {
        try {
            // Load the UpdateProfile.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestion User/UpdateProfile.fxml"));
            Parent root = loader.load();

            // Get the UpdateProfile controller
            UpdateProfile updateProfileController = loader.getController();

            // Pass the current user's data to the UpdateProfile controller
            updateProfileController.setUser(UserSession.getInstance().getUser()); // Assuming 'user' is the current logged-in user

            // Set the new scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) Update.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setUser(User user) {
        if (user != null) {
            // Debug: Print the image URL
            System.out.println("Image URL: " + user.getImage_url());

            // Set the user's information in the text fields and label
            Email_textfield.setText(user.getEmail());
            Nom_Label.setText(user.getNom());
            Nom_textfield.setText(user.getNom());
            Prenom_textfield.setText(user.getPrenom());
            Voyage_textfield.setText(String.valueOf(user.getNbrVoyage()));

            if (user.getImage_url() != null && !user.getImage_url().isEmpty()) {
                try {
                    System.out.println("Loading image from: " + user.getImage_url());

                    String imageUrl = user.getImage_url();

                    // For local files, prepend "file:/"
                    if (imageUrl.startsWith("C:") || imageUrl.startsWith("/")) {
                        imageUrl = "file:/" + imageUrl.replace("\\", "/");
                    }

                    // Debug URL after adjustment
                    System.out.println("Adjusted Image URL: " + imageUrl);

                    // Disable caching to force reload
                    Image image = new Image(imageUrl, false);
                    imageView.setImage(image);
                } catch (Exception e) {
                    e.printStackTrace();
                    Image placeholder = new Image(getClass().getResource("/images/userprofile.png").toString());
                    imageView.setImage(placeholder);
                }
            } else {
                System.out.println("No image URL provided.");
                Image placeholder = new Image(getClass().getResource("/images/userprofile.png").toString());
                imageView.setImage(placeholder);
            }
        }
    }

}
