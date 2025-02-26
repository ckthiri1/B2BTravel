package Projet.controllers.GestionUserController;


import Projet.entities.User;
import Projet.services.GoogleAuthService;
import Projet.services.VoiceAuthService;
import Projet.tools.AudioRecorder;
import com.google.api.client.auth.oauth2.Credential;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import Projet.services.UserService;
import Projet.tools.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.sound.sampled.LineUnavailableException;


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
    @FXML
    private Button googleLoginBtn; // New Google login button

    private static final String PYTHON_PATH = System.getProperty("os.name").toLowerCase().contains("win")
            ? "C:\\Users\\Acer\\AppData\\Local\\Programs\\Python\\Python312\\python.exe" : "python3";
    private static final String TEMP_AUDIO_PATH = "login_voice.wav";
    private static final int MAX_VOICE_ATTEMPTS = 3;

    private int failedVoiceAttempts = 0;
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
    void loginWithVoice() {
        error.setText("");
        String userEmail = email.getText().trim();

        if (userEmail.isEmpty()) {
            error.setText("Please enter your email first");
            return;
        }

        File audioFile = null;
        try {
            // Record audio
            audioFile = AudioRecorder.recordVoice(TEMP_AUDIO_PATH, 5);
            if (!audioFile.exists() || audioFile.length() == 0) {
                error.setText("Failed to record audio sample");
                return;
            }

            UserService userService = new UserService();
            User user = userService.getUserByEmail(userEmail);
            if (user == null) {
                error.setText("User not found");
                return;
            }

            List<List<Double>> voiceFeatures = user.getVoiceFeatures();
            if (voiceFeatures == null || voiceFeatures.isEmpty()) {
                error.setText("No voice profile registered");
                return;
            }

            boolean verified = VoiceAuthService.verifyUser(
                    PYTHON_PATH,
                    audioFile,
                    voiceFeatures
            );

            if (verified) {
                handleSuccessfulLogin(user);
            } else {
                handleFailedVoiceAttempt();
            }
        } catch (Exception e) {
            error.setText("Authentication error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (audioFile != null && audioFile.exists()) {
                audioFile.delete();
            }
        }
    }
    private void handleSuccessfulLogin(User user) throws IOException {
        UserSession.getInstance().setUser(user);
        failedVoiceAttempts = 0;
        redirectToHomePage();
    }

    private void handleFailedVoiceAttempt() {
        error.setText("Voice verification failed - Please try again");
        if (++failedVoiceAttempts >= MAX_VOICE_ATTEMPTS) {
            error.setText("Too many failed attempts - Please use password login");

        }
    }

    private void cleanupTempFile(File audioFile) {
        if (audioFile != null && audioFile.exists()) {
            if (!audioFile.delete()) {
                System.err.println("Failed to delete temporary audio file");
            }
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
    @FXML
    void handleGoogleLogin(ActionEvent event) {
        try {
            // Initialize the GoogleAuthService
            GoogleAuthService googleAuth = new GoogleAuthService();
            String authUrl = googleAuth.getAuthorizationUrl();

            // Open the system default browser with the auth URL
            Desktop.getDesktop().browse(new URI(authUrl));

            // Prompt the user to enter the authorization code from the browser
            String code = getUserInput("Enter the authorization code provided by Google:");

            if (code == null || code.trim().isEmpty()) {
                error.setText("Authorization code is required.");
                return;
            }

            // Exchange the code for credentials (access token)
            Credential credential = googleAuth.getCredentials(code);

            // Use the access token to retrieve the user's information
            String userInfoJson = getUserInfo(credential.getAccessToken());
            System.out.println("User Info: " + userInfoJson);

            // Parse JSON to extract email and name
            JsonObject jsonObject = JsonParser.parseString(userInfoJson).getAsJsonObject();
            String emailFromGoogle = jsonObject.get("email").getAsString();
            String nameFromGoogle = jsonObject.has("name") ? jsonObject.get("name").getAsString() : "Google User";

            // Check if the user already exists in your system using email
            UserService userService = new UserService();
            User existingUser = userService.getUserByEmaill(emailFromGoogle);

            if (existingUser == null) {
                // Create a new user if not exists
                User newUser = new User();
                newUser.setEmail(emailFromGoogle);
                newUser.setNom(nameFromGoogle);
                newUser.setPwd("GoogleAuth"); // Placeholder password; you may flag this account as Google-based
                newUser.setRole("user");

                userService.addEntity(newUser);
                existingUser = newUser;
            }

            // Store the logged-in user in your session
            UserSession.getInstance().setUser(existingUser);

            // Redirect to the home page
            redirectToHomePage();

        } catch (Exception e) {
            e.printStackTrace();
            error.setText("Google login failed: " + e.getMessage());
        }
    }

    // Utility method to prompt user for the authorization code
    private String getUserInput(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Google Authorization");
        dialog.setHeaderText(prompt);
        return dialog.showAndWait().orElse(null);
    }

    // Use Java's built-in HttpClient (Java 11+) to get user info from Google
    private String getUserInfo(String accessToken) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.googleapis.com/oauth2/v2/userinfo"))
                .header("Authorization", "Bearer " + accessToken)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Redirect to the home page after successful login
    private void redirectToHomePage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface Utilisateur/PageHome.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) googleLoginBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}