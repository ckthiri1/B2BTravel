package Projet.controllers.GestionUserController;

import Projet.entities.User;
import Projet.services.UserService;
import Projet.tools.MyConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

public class UpdateProfile {

    @FXML
    private Button Delete;

    @FXML
    private Button Confirm;

    @FXML
    private TextField Email_textfield;

    @FXML
    private Label Nom_Label;

    @FXML
    private TextField Nom_textfield;

    @FXML
    private TextField Prenom_textfield;

    @FXML
    private TextField Confirmpassword_textField;

    @FXML
    private TextField password_textField;

    @FXML
    private TextField Voyage_textfield;

    @FXML
    private Button Upload;

    @FXML
    private ImageView imageView;

    private User user; // Store the current user

    // Method to set the user's data
    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            // Pre-fill the fields with the user's data
            Email_textfield.setText(user.getEmail());
            Nom_Label.setText(user.getNom());
            Nom_textfield.setText(user.getNom());
            Prenom_textfield.setText(user.getPrenom());

            // Pre-populate the password field with the current password (unhashed)
            password_textField.setText(user.getPwd());  // Setting the current password

            // Load profile image
            if (user.getImage_url() != null && !user.getImage_url().isEmpty()) {
                try {
                    String imageUrl = user.getImage_url();

                    // For local files, prepend "file:/"
                    if (imageUrl.startsWith("C:") || imageUrl.startsWith("/")) {
                        imageUrl = "file:/" + imageUrl.replace("\\", "/");
                    }

                    javafx.scene.image.Image image = new javafx.scene.image.Image(imageUrl, false);
                    imageView.setImage(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void Delete(ActionEvent event) {
        try {
            // Delete the user from the database
            UserService userService = new UserService();
            userService.deleteEntity(user.getUser_id()); // Delete the user

            // Redirect to the login page
            Parent root = FXMLLoader.load(getClass().getResource("/Gestion User/login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) Delete.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Confirm(ActionEvent event) {
        if (!validateInputsAndProceed()) return;
        try {
            // Update the user in the database
            UserService userService = new UserService();
            // Update the user's data from the text fields
            user.setNom(Nom_textfield.getText());
            user.setPrenom(Prenom_textfield.getText());
            user.setEmail(Email_textfield.getText());

            // Update password if user has provided a new one
            if (!password_textField.getText().isEmpty()) {
                String newPassword = password_textField.getText();
                String confirmPassword = Confirmpassword_textField.getText();

                if (!newPassword.equals(confirmPassword)) {
                    showAlert("Les mots de passe ne correspondent pas");
                    return;
                }

                // Update password in the database
                userService.updatePasswordd(user.getUser_id(), newPassword);
            }

            userService.updateEntity(user.getUser_id(), user);

            // Check if a new image has been selected
            if (user.getImage_url() != null && !user.getImage_url().isEmpty()) {
                // Update the image URL in the database
                String updateImageQuery = "UPDATE user SET image_url=? WHERE user_id=?";
                java.sql.PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(updateImageQuery);
                pst.setString(1, user.getImage_url());
                pst.setInt(2, user.getUser_id());
                pst.executeUpdate();
            }

            showSuccessMessage("Votre mise à jour a été enregistrée avec succès");

            // Redirect back to the Profile page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion User/profile.fxml"));
            Parent root = loader.load();

            // Get the Profile controller and pass the updated user
            Profile profileController = loader.getController();
            profileController.setUser(user);

            // Set the new scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) Confirm.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateInputsAndProceed() {
        if (Nom_textfield.getText().isEmpty()) {
            showAlert("Le nom est requis");
            return false;
        }
        if (Prenom_textfield.getText().isEmpty()) {
            showAlert("Le prénom est requis");
            return false;
        }
        if (Email_textfield.getText().isEmpty()) {
            showAlert("L'email est requis");
            return false;
        }

        // Vérifier si l'adresse email est valide et afficher une erreur si elle ne l'est pas
        String email = Email_textfield.getText();
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showAlert("L'adresse email est invalide");
            return false;
        }

        return true;
    }

    @FXML
    void Upload(ActionEvent event) {
        Stage stage = (Stage) Upload.getScene().getWindow();
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Choisir une image de profil");
        fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        java.io.File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            user.setImage_url(imagePath);  // Set new image path in user object

            // Display new image
            javafx.scene.image.Image newImage = new javafx.scene.image.Image("file:/" + imagePath.replace("\\", "/"), false);
            imageView.setImage(newImage);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Message");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
