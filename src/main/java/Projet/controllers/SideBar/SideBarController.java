package Projet.controllers.SideBar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class SideBarController {

    @FXML
    private ImageView logoImageView;


    @FXML
    private void NavigationLogoClick(MouseEvent event) {
        try {
            // Load the new page
            Parent newPage = FXMLLoader.load(getClass().getResource("/DashboardAdmin.fxml"));
            Scene newScene = new Scene(newPage);

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(newScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void NavigateToGestionVoyage(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion Voyage/ListVoyage.fxml"));
            Parent listVoyageRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(listVoyageRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void NavigateToGestionFidelite(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion Fidélité/ListRank.fxml"));
            Parent listVoyageRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(listVoyageRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void NavigateToProfile(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion User/profile.fxml"));
            Parent listVoyageRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(listVoyageRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void NavigateToReservation(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interface Utilisateur/ReservationPage.fxml"));
            Parent listVoyageRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(listVoyageRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void NavigateToFidelite(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion Fidélité/ConsulterFideliter.fxml"));
            Parent listVoyageRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(listVoyageRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void NavigateToUser(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gestion User/user_dashboard.fxml"));
            Parent listVoyageRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(listVoyageRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void NavigateToHome(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface Utilisateur/PageHome.fxml"));
            Parent listVoyageRoot = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(listVoyageRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }








}
