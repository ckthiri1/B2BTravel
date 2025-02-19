package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            Parent parent =  FXMLLoader.load(getClass().getResource("/AjouterReclamation.fxml"));
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ajouter Reclamation");
            primaryStage.show();

        } catch (IOException e) {
           // throw new RuntimeException(e);
            // Print the stack trace for debugging
            e.printStackTrace();
            throw new RuntimeException("Failed to load FXML file: " + e.getMessage());
        }
        }

    }