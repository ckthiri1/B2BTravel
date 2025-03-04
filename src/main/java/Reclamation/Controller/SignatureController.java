package Reclamation.Controller;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class SignatureController {
    private Stage stage; // Reference to the stage

    @FXML
    private Canvas signatureCanvas;

    private GraphicsContext gc;
    private boolean isDrawing = false;
    private Consumer<WritableImage> signatureConfirmedCallback;

    @FXML
    public void initialize() {
        // Set up the canvas for drawing
        gc = signatureCanvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK); // Set the drawing color to black
        gc.setLineWidth(2); // Set the line thickness

        // Add event handlers for drawing
        signatureCanvas.setOnMousePressed(this::startDrawing);
        signatureCanvas.setOnMouseDragged(this::draw);
        signatureCanvas.setOnMouseReleased(this::stopDrawing);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void startDrawing(MouseEvent event) {
        isDrawing = true;
        gc.beginPath();
        gc.moveTo(event.getX(), event.getY()); // Start drawing at the mouse position
    }

    private void draw(MouseEvent event) {
        if (isDrawing) {
            gc.lineTo(event.getX(), event.getY()); // Draw a line to the new mouse position
            gc.stroke(); // Render the line
        }
    }

    private void stopDrawing(MouseEvent event) {
        isDrawing = false;
        gc.closePath(); // Stop drawing
    }


    @FXML
    private void confirmSignature() {
        // Check if the canvas is empty
        if (isCanvasEmpty()) {
            // Show a warning message if the canvas is empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Signature manquante");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez signer avant de confirmer.");
            alert.showAndWait();
            return; // Exit the method without closing the window
        }

        // Capture the signature as an image
        WritableImage signatureImage = signatureCanvas.snapshot(null, null);

        // Pass the signature image back to the main controller
        if (signatureConfirmedCallback != null) {
            signatureConfirmedCallback.accept(signatureImage);
        }

        // Close the signature window
        if (stage != null) {
            stage.close(); // Close the window
        }

        // Optionally, clear the canvas for future use
        clearCanvas();
    }




    private void clearCanvas() {
        gc.clearRect(0, 0, signatureCanvas.getWidth(), signatureCanvas.getHeight());
    }

    public void setSignatureConfirmedCallback(Consumer<WritableImage> callback) {
        this.signatureConfirmedCallback = callback;
    }

    private boolean isCanvasEmpty() {
        // Create a temporary image to check if the canvas is empty
        WritableImage tempImage = signatureCanvas.snapshot(null, null);
        for (int x = 0; x < tempImage.getWidth(); x++) {
            for (int y = 0; y < tempImage.getHeight(); y++) {
                if (tempImage.getPixelReader().getArgb(x, y) != 0) {
                    return false; // Canvas is not empty
                }
            }
        }
        return true; // Canvas is empty
    }
}