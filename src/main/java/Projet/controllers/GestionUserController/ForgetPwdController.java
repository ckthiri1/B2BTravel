package Projet.controllers.GestionUserController;

import Projet.services.EmailService;
import Projet.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ForgetPwdController {

    @FXML
    private TextField emailField;

    @FXML
    private Label statusLabel;

    @FXML
    private Button handlePasswordReset;

    @FXML
    private Label error;

    private final UserService userService = new UserService();
    private final EmailService emailService = new EmailService();

    @FXML
    void handlePasswordReset(ActionEvent event) {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            statusLabel.setText("Veuillez entrer votre email.");
            return;
        }

        if (userService.isEmailExist(email)) {
            // Generate and store the token
            String token = userService.generateToken();
            userService.storeResetToken(email, token);

            // Send reset email with instructions.
            emailService.sendResetEmail(email, token);

            // Inform the user.
            statusLabel.setText("Lien de réinitialisation envoyé. Veuillez vérifier votre email et copier le jeton.");

            // Automatically load the reset page (the token field remains empty)
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Gestion User/resetPwd.fxml"));
                Scene scene = new Scene(root);

                // Get the current stage
                Stage stage = (Stage) handlePasswordReset.getScene().getWindow();

                // Set the new scene
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Erreur de chargement de la page de réinitialisation.");
            }
        } else {
            statusLabel.setText("Email non enregistré.");
        }
    }
}
