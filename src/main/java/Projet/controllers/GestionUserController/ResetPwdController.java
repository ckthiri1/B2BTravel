package Projet.controllers.GestionUserController;

import Projet.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class ResetPwdController {

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Button handlePasswordChange;

    @FXML
    private Label statusLabel;

    // The token field where the user must paste the token received by email.
    @FXML
    private TextField tokenField;

    private final UserService userService = new UserService();

    // (Optional) setToken method for debugging purposes.
    public void setToken(String token) {
        System.out.println("Received token (ignored for manual entry): " + token);
        // Do NOT pre-fill the tokenField; user must copy it manually.
    }

    @FXML
    void handlePasswordChange(ActionEvent event) {
        String token = tokenField.getText().trim();
        String newPassword = newPasswordField.getText().trim();

        if (token.isEmpty() || newPassword.isEmpty()) {
            statusLabel.setText("Veuillez saisir le jeton et le nouveau mot de passe.");
            return;
        }

        // Validate the token using the one the user has entered.
        if (userService.isValidToken(token)) {
            // Update the password.
            userService.updatePassword(token, newPassword);
            statusLabel.setText("Mot de passe réinitialisé avec succès !");

            try {
                // After a successful reset, load the login page.
                Parent root = FXMLLoader.load(getClass().getResource("/Gestion User/login.fxml"));
                Scene scene = new Scene(root);

                // Get the current stage using the event source.
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            statusLabel.setText("Jeton invalide ou expiré.");
        }
    }
}
