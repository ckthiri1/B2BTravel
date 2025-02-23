package Projet.controllers.GestionUserController;


import Projet.entities.User;
import Projet.services.UserService;
import Projet.tools.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login implements Initializable {
    public static User user = new User();



    @FXML
    private TextField email;
    @FXML
    private TextField password;

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordVisible;
    @FXML
    private ImageView eyeIcon;
    @FXML
    private Label error;
    @FXML
    private Button signUp;
    @FXML
    private Button login;

    private boolean isPasswordVisible = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login.setOnMouseEntered(event -> {
            login.setEffect(new DropShadow());
        });

        login.setOnMouseExited(event -> {
            login.setEffect(null);
        });
    }

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
    void signUp(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Gestion User/signUp.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) signUp.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            error.setText("Failed to load sign up page.");
        }
    }

    @FXML
    void ForgetPwd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion User/forgotPassword.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) error.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            error.setText("Failed to load the reset password page.");
        }
    }


    @FXML
    void login(ActionEvent event) throws SQLException {
        UserService su = new UserService();
        String Email = email.getText();
        String Password = isPasswordVisible ? passwordVisible.getText() : passwordField.getText();

        User user = su.login(Email, Password);
        if (user == null) {
            error.setText("Email ou mot de passe incorrecte");
        } else {
            // Store the logged-in user in UserSession
            UserSession.getInstance().setUser(user);

            try {
                FXMLLoader loader;
                Parent root;
                Scene scene;
                Stage stage = (Stage) signUp.getScene().getWindow();

                if (user.getRole().toString().equals("user")) {

                    loader = new FXMLLoader(getClass().getResource("/interface Utilisateur/PageHome.fxml"));
                    root = loader.load();
                } else if (user.getRole().toString().equals("admin")) {

                    loader = new FXMLLoader(getClass().getResource("/DashboardAdmin.fxml"));
                    root = loader.load();
                } else {
                    error.setText("Unauthorized role.");
                    return;
                }

                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
                error.setText("Failed to load the page.");
            }
        }
    }
}