package Projet.controllers.GestionUserController;


import Projet.entities.User;
import Projet.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddAdmin implements Initializable {

    @FXML
    private Button UploadImage;

    @FXML
    private TextField email;

    @FXML
    private ImageView imageView;

    @FXML
    private TextField nom;

    @FXML
    private TextField password;

    @FXML
    private TextField prenom;

    @FXML
    private SplitMenuButton role;

    @FXML
    private Button signUp;

    @FXML
    private Button LogOut;

    private File selectedImageFile;
    private String selectedRole; // Variable to store the selected role
    private UserService serviceUser = new UserService();

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

    public boolean validateInputsAndProceed() {

        if (
                selectedImageFile == null) {
            showAlert("Sélectionner une image s'il vous plaît.");
            return false; // Stop the process
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
            showAlert("Email est requis");
            return false;
        }
        if (password.getText().isEmpty()) {
            showAlert("Le mot de passe est requiq");
            return false;
        }
        String pass  = password.getText();
        if (pass.length() < 8 ) {
            showAlert("Le mot de passe doit contenir au moins 8 caractères");
            return false;
        }
        // Vérifier si l'adresse email est valide et afficher une erreur si elle ne l'est pas
        String Email = email.getText();
        if (!Email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showAlert("L'adresse email est invalide");
            return false;
        }


        return true;
    }

    @FXML
    void signUp(ActionEvent event) {
        if (!validateInputsAndProceed()) return;

        String Nom = nom.getText();
        String Prenom = prenom.getText();
        String Email = email.getText();
        String Password = password.getText();

        try {
            // Create a User object
            User user = new User();
            user.setNom(Nom);
            user.setPrenom(Prenom);
            user.setEmail(Email);
            user.setPwd(Password);
            user.setRole(selectedRole); // Set the selected role
            user.setNbrVoyage(0);

            // Set the image URL if an image was selected
            if (selectedImageFile != null) {
                user.setImage_url(selectedImageFile.toURI().toString());
            }

            // Add the user to the database
            serviceUser.addEntity2(user);
            showSuccessMessage("L'administrateur a été ajouté avec succès");

            // Clear the form or navigate to another page if needed
            clearForm();
            try {
                // Load the signUp.fxml file
                Parent root = FXMLLoader.load(getClass().getResource("/Gestion User/user_dashboard.fxml"));
                Scene scene = new Scene(root);

                // Get the current stage
                Stage stage = (Stage) signUp.getScene().getWindow();

                // Set the new scene
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ajout de l'administrateur.");
        }

    }

    private void clearForm() {
        nom.clear();
        prenom.clear();
        email.clear();
        password.clear();
        role.setText("Role"); // Reset the role button text
        selectedRole = null;
        imageView.setImage(null);
        selectedImageFile = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add menu items to the SplitMenuButton
        MenuItem adminItem = new MenuItem("Admin");
        MenuItem userItem = new MenuItem("User");

        // Set action handlers for menu items
        adminItem.setOnAction(event -> {
            selectedRole = "admin";
            role.setText("Admin");
        });

        userItem.setOnAction(event -> {
            selectedRole = "user";
            role.setText("User");
        });

        // Add menu items to the SplitMenuButton
        role.getItems().addAll(adminItem, userItem);

    }

    @FXML
    void LogOut(ActionEvent event) {
        try {
            // Load the signUp.fxml file
            Parent root = FXMLLoader.load(getClass().getResource("/Gestion User/login.fxml"));
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) LogOut.getScene().getWindow();

            // Set the new scene
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
