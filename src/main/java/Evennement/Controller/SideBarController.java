package Evennement.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;



public class SideBarController {



    @FXML
    public Button button_evennement;
    @FXML
    public Button button_user;
    @FXML
    public Button button_reclamation;
    @FXML
    public Button button_voyage;
    @FXML
    public Button button_fidelite;
    @FXML
    public Button button_reservation;
    @FXML
    public Button log_out;









    public void handleMouseDragOver(DragEvent event) {
        if (event.getGestureSource() != event.getSource() && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    @FXML
    void handleMouseEnteredEvent() {
        button_evennement.setStyle("-fx-background-color: #0077b6; -fx-background-radius: 20;");




    }
    @FXML
    void handleMouseEnteredFidelite() {
        button_fidelite.setStyle("-fx-background-color: #0077b6; -fx-background-radius: 20;");
    }
    @FXML
    void handleMouseEnteredReclamation() {
        button_reclamation.setStyle("-fx-background-color: #0077b6; -fx-background-radius: 20;");
    }
    @FXML
    void handleMouseEnteredReservation() {
        button_reservation.setStyle("-fx-background-color: #0077b6; -fx-background-radius: 20;");

    }

    @FXML
    void handleMouseEntereduser() {
        button_user.setStyle("-fx-background-color: #0077b6; -fx-background-radius: 20;");
    }

    @FXML
    void handleMouseEnteredvoyage() {
        button_voyage.setStyle("-fx-background-color: #0077b6; -fx-background-radius: 20;");
    }

    @FXML
    void handleMouseEnteredlogout() {
        log_out.setStyle("-fx-background-color: #FF0000; -fx-background-radius: 20;");
    }













    @FXML
    void handleMouseExitedEvent() {
        button_evennement.setStyle("-fx-background-color: #03045f; -fx-background-radius: 20;");

    }


    @FXML
    void handleMouseExitedVoyage() {
        button_voyage.setStyle("-fx-background-color: #03045f; -fx-background-radius: 20;");

    }

    @FXML
    void handleMouseExitedUser() {
        button_user.setStyle("-fx-background-color: #03045f; -fx-background-radius: 20;");

    }

    @FXML
    void handleMouseExitedReservation() {
        button_reservation.setStyle("-fx-background-color: #03045f; -fx-background-radius: 20;");

    }


    @FXML
    void handleMouseExitedFidelite() {
        button_fidelite.setStyle("-fx-background-color: #03045f; -fx-background-radius: 20;");

    }





    @FXML
    void handleMouseExitedReclamation() {
        button_reclamation.setStyle("-fx-background-color: #03045f; -fx-background-radius: 20;");

    }

    @FXML
    void handleMouseExitedLogout() {
        log_out.setStyle("-fx-background-color: #03045f; -fx-background-radius: 20;");

    }








}
