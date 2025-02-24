package CRUD.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static javafx.application.Application.launch;

public class mainFx extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MeteoU.fxml"));
        try {
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            // Ajoutez ici votre fichier CSS
            scene.getStylesheets().add(getClass().getResource("/RS.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("Ajouter Reservation");
            primaryStage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
