package Projet.controllers.Dashboards;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DashboardadminController {

    @FXML
    void NavigateToUser(MouseEvent event){
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
    void NavigateToVoyage(MouseEvent event){
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
    void NavigateToFidelite(MouseEvent event){
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





}
