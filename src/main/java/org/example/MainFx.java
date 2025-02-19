package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.net.URL;

public class MainFx extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Ensure the correct resource path
            URL fxmlFile = getClass().getResource("/homepage.fxml");

            if (fxmlFile == null) {
                throw new RuntimeException("FXML file not found! Check the path");
            }

            FXMLLoader loader = new FXMLLoader(fxmlFile);
            Parent root = loader.load();

            primaryStage.setTitle("Ajouter Événement");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
