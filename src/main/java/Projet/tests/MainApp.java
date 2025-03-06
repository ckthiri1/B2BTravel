package Projet.tests;

import Projet.tools.MyConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        Parent root = FXMLLoader.load(getClass().getResource("/Gestion Voyage/ListVoyage.fxml"));         //interface Utilisateur/PageHome.fxml

        Scene scene = new Scene(root, 1540, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX PI");
        primaryStage.show();
    }

    public static void main(String[] args) {

        MyConnection myConnection = MyConnection.getInstance();

        launch(args);
    }
}
