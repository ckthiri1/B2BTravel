package Projet.controllers.GestionUserController;
import Projet.entities.User;
import Projet.services.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class UserDashboard {

    @FXML
    private Button AddAdmin;
    @FXML
    private Button LogOut;
    @FXML
    private Label error;
    @FXML
    private GridPane grid;
    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        fetchDataFromDatabase();
    }

    private void fetchDataFromDatabase() {
        // Fetch user data
        List<User> users = userService.getAllData();

        // Clear existing elements
        grid.getChildren().clear();

        int row = 0;
        int col = 0;

        for (User user : users) {
            VBox userCard = createUserCard(user);
            grid.add(userCard, col, row);
            col++;
            if (col == 2) {  // Adjust based on desired column count
                col = 0;
                row++;
            }
        }
    }

    private VBox createUserCard(User user) {
        VBox card = new VBox();
        card.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-radius: 10; -fx-border-radius: 10;");

        Label nameLabel = new Label(user.getNom() + " " + user.getPrenom());
        Label emailLabel = new Label("Email: " + user.getEmail());
        Label roleLabel = new Label("Role: " + user.getRole());
        Label travelCountLabel = new Label("Trips: " + user.getNbrVoyage());

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5;");
        deleteButton.setOnAction(event -> {
            deleteUser(user);
            fetchDataFromDatabase();  // Refresh grid
        });

        card.getChildren().addAll(nameLabel, emailLabel, roleLabel, travelCountLabel, deleteButton);
        return card;
    }

    private void deleteUser(User user) {
        userService.deleteEntity(user.getUser_id());

        Platform.runLater(() -> {
            fetchDataFromDatabase();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User deleted successfully!");
            alert.showAndWait();
        });
    }



    @FXML
    void NavigateToUserAdd(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion User/addAdmin.fxml"));
            Parent listVoyageRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(listVoyageRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
