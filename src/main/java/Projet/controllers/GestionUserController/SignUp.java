package Projet.controllers.GestionUserController;

import Projet.entities.User;
import Projet.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.management.relation.Role;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SignUp implements Initializable {

    @FXML
    private TextField email;

    @FXML
    private TextField nom;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisible;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField confirmPasswordVisible;

    @FXML
    private ImageView eyeIcon;

    @FXML
    private ImageView eyeIconConfirm;

    @FXML
    private TextField password;

    @FXML
    private TextField confirmpwd;


    @FXML
    private TextField prenom;

    @FXML
    private Button signUp;

    @FXML
    private Button UploadImage;

    @FXML
    private ImageView imageView;

    private File selectedImageFile;

    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @FXML
    void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        if (isPasswordVisible) {
            passwordVisible.setText(passwordField.getText());
            passwordVisible.setVisible(true);
            passwordField.setVisible(false);
            eyeIcon.setImage(new Image(getClass().getResourceAsStream("/Images/visible.png")));
        } else {
            passwordField.setText(passwordVisible.getText());
            passwordField.setVisible(true);
            passwordVisible.setVisible(false);
            eyeIcon.setImage(new Image(getClass().getResourceAsStream("/Images/eye.png")));
        }
    }

    @FXML
    void toggleConfirmPasswordVisibility() {
        isConfirmPasswordVisible = !isConfirmPasswordVisible;
        if (isConfirmPasswordVisible) {
            confirmPasswordVisible.setText(confirmPasswordField.getText());
            confirmPasswordVisible.setVisible(true);
            confirmPasswordField.setVisible(false);
            eyeIconConfirm.setImage(new Image(getClass().getResourceAsStream("/Images/visible.png")));
        } else {
            confirmPasswordField.setText(confirmPasswordVisible.getText());
            confirmPasswordField.setVisible(true);
            confirmPasswordVisible.setVisible(false);
            eyeIconConfirm.setImage(new Image(getClass().getResourceAsStream("/Images/eye.png")));
        }
    }



    private UserService serviceUser = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void signUp(ActionEvent event) throws SQLException, FileNotFoundException {
        if(!validateInputsAndProceed()) return;
        String Nom = nom.getText();
        String Prenom = prenom.getText();
        String Email = email.getText();
        String Password;

        // Capture password based on visibility state
        if (isPasswordVisible) {
            Password = passwordVisible.getText();
        } else {
            Password = passwordField.getText();
        }
        try {


            // Create an Activity object
            User user = new User();
            user.setNom(Nom);
            user.setPrenom(Prenom);
            user.setEmail(Email);
            user.setPwd(Password);
            user.setRole("user");
            user.setNbrVoyage(0);

            // Set the image URL if an image was selected
            if (selectedImageFile != null) {
                user.setImage_url(selectedImageFile.toURI().toString());
            }

            serviceUser.addEntity2(user);
            showSuccessMessage("Votre inscription a été enregistrée avec succès");

            try {
                // Load the signUp.fxml file
                Parent root = FXMLLoader.load(getClass().getResource("/Gestion User/login.fxml"));
                Scene scene = new Scene(root);

                // Get the current stage
                Stage stage = (Stage) signUp.getScene().getWindow();

                // Set the new scene
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public boolean validateInputsAndProceed() {

        if (selectedImageFile == null) {
            showAlert("Sélectionner une image s'il vous plaît.");
            return false;
        }
        if (nom.getText().isEmpty()) {
            showAlert("Le nom est requis");
            return false;
        }
        if (prenom.getText().isEmpty()) {
            showAlert("Le prénom est requis");
            return false;
        }
        if (email.getText().isEmpty()) {
            showAlert("L'email est requis");
            return false;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert("Le mot de passe est requis");
            return false;
        }
        String pass = passwordField.getText();
        if (pass.length() < 8) {
            showAlert("Le mot de passe doit contenir au moins 8 caractères");
            return false;
        }
        // Check if confirm password matches
        String confirmPass = confirmPasswordField.getText();
        if (confirmPass.isEmpty()) {
            showAlert("Veuillez confirmer votre mot de passe");
            return false;
        }
        if (!pass.equals(confirmPass)) {
            showAlert("Les mots de passe ne correspondent pas");
            return false;
        }
        // Validate email format
        String Email = email.getText();
        if (!Email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showAlert("L'adresse email est invalide");
            return false;
        }

        return true;
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

    @FXML
    void UploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show the file chooser dialog
        Stage stage = (Stage) UploadImage.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            // Display the selected image in the ImageView
            Image image = new Image(selectedImageFile.toURI().toString());
            imageView.setImage(image);
        } else {
            showAlert("No image file selected.");
        }

    }


}
