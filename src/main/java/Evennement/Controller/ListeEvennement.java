package Evennement.Controller;

import Evennement.entities.Evennement;
import Evennement.entities.EventType;
import Evennement.entities.Organisateur;
import Evennement.services.EvennementService;
import Evennement.tools.MyConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ListeEvennement {

    @FXML
    private VBox eventListContainer;

    @FXML
    private VBox detailsContainer;

    @FXML
    private TextField searchField;
    @FXML
    private void toggleTheme() {
        Scene scene = themeIcon.getScene();
        if (scene == null) return;

        System.out.println("Toggling theme...");

        if (isDarkMode) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/light.css").toExternalForm());
            themeIcon.setImage(new Image(getClass().getResourceAsStream("/Images/dark_mode.png")));
            isDarkMode = false;
            System.out.println("Switched to light mode.");
        } else {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/dark.css").toExternalForm());
            themeIcon.setImage(new Image(getClass().getResourceAsStream("/Images/light_mode.png")));
            isDarkMode = true;
            System.out.println("Switched to dark mode.");
        }

        // Force the detailsContainer to refresh its styles
        detailsContainer.getStyleClass().clear();
        detailsContainer.getStyleClass().add("details-container");
    }
    @FXML
    public void initialize() {
        // Set default icon
        themeIcon.setImage(new Image(getClass().getResourceAsStream("/Images/dark_mode.png")));
        // Apply initial theme
        Scene scene = themeIcon.getScene();
        if (scene != null) {
            scene.getStylesheets().add(getClass().getResource("/light.css").toExternalForm());
        }
    }

    private Evennement selectedEvennement;


    @FXML
    private ImageView themeIcon;

    private boolean isDarkMode = false;

    private final EvennementService evennementService = new EvennementService();





    @FXML
    private void searchEvennements() {
        String searchText = searchField.getText().trim().toLowerCase();
        loadEvennements(searchText);
    }

    @FXML
    private void loadEvennements1() {
        try {
            eventListContainer.getChildren().clear();

            List<Evennement> events = new ArrayList<>();
            String query = "SELECT e.IDE, e.NomE, e.Local, e.DateE, e.DesE, e.event_type, " +
                    "o.IDOr, o.NomOr, o.Contact " +
                    "FROM Evennement e " +
                    "JOIN Organisateur o ON e.IDOr = o.IDOr";

            Statement stmt = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int IDE = rs.getInt("IDE");
                String nomE = rs.getString("NomE");
                String local = rs.getString("Local");
                String desE = rs.getString("DesE");
                Date dateE = new Date(rs.getTimestamp("DateE").getTime());

                // Retrieve event type and handle invalid or null cases
                String eventTypeString = rs.getString("event_type");

                // Normalize the event type string to match the enum values
                if (eventTypeString != null) {
                    eventTypeString = eventTypeString.toUpperCase(); // Convert to uppercase
                }

                EventType eventType = null;
                if (eventTypeString != null) {
                    try {
                        eventType = EventType.valueOf(eventTypeString);  // Convert String to EventType enum
                        System.out.println("Successfully converted to EventType: " + eventType); // Debugging log
                    } catch (IllegalArgumentException e) {
                        System.out.println("❌ Invalid event type: " + eventTypeString);
                        System.out.println("Valid event types are: " + Arrays.toString(EventType.values())); // Log all valid enum values
                    }
                }

                if (eventType == null) {
                    eventType = EventType.DEFAULT; // Default event type
                    System.out.println("Event type set to default: " + eventType); // Debugging log
                }

                int IDOr = rs.getInt("IDOr");
                String nomOr = rs.getString("NomOr");
                int contact = rs.getInt("Contact");

                Organisateur organisateur = new Organisateur(IDOr, nomOr, contact);
                Evennement evennement = new Evennement(nomE, local, desE, dateE, organisateur, eventType);
                evennement.setIDE(IDE);

                // Debugging: Verify the event type set in Evennement
                System.out.println("EventType set in Evennement: " + evennement.getEventType());

                // Dynamically add event to VBox
                eventListContainer.getChildren().add(createEventItem(evennement));
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Error while fetching events: " + e.getMessage());
            showAlert("Erreur", "Erreur lors de la récupération des événements.");
        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            showAlert("Erreur", "Une erreur inattendue est survenue.");
        }
    }




    private void loadEvennements(String searchText) {
        try {
            eventListContainer.getChildren().clear();

            List<Evennement> events = new ArrayList<>();
            String query = "SELECT e.IDE, e.NomE, e.Local, e.DateE, e.DesE, e.event_type, " +
                    "o.IDOr, o.NomOr, o.Contact " +
                    "FROM Evennement e " +
                    "JOIN Organisateur o ON e.IDOr = o.IDOr";

            Statement stmt = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int IDE = rs.getInt("IDE");
                String nomE = rs.getString("NomE").toLowerCase(); // Convert to lowercase for case-insensitive search
                String local = rs.getString("Local");
                String desE = rs.getString("DesE");
                Date dateE = new Date(rs.getTimestamp("DateE").getTime());

                String eventTypeString = rs.getString("event_type");
                if (eventTypeString != null) {
                    eventTypeString = eventTypeString.toUpperCase();
                }
                EventType eventType = null;
                if (eventTypeString != null) {
                    try {
                        eventType = EventType.valueOf(eventTypeString);
                    } catch (IllegalArgumentException e) {
                        eventType = EventType.DEFAULT;
                    }
                }

                int IDOr = rs.getInt("IDOr");
                String nomOr = rs.getString("NomOr");
                int contact = rs.getInt("Contact");

                Organisateur organisateur = new Organisateur(IDOr, nomOr, contact);
                Evennement evennement = new Evennement(nomE, local, desE, dateE, organisateur, eventType);
                evennement.setIDE(IDE);

                // Apply search filter
                if (searchText.isEmpty() || nomE.contains(searchText)) {
                    eventListContainer.getChildren().add(createEventItem(evennement));
                }
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la récupération des événements.");
        }
    }


    private HBox createEventItem(Evennement evennement) {
        HBox eventBox = new HBox(20);
        eventBox.getStyleClass().add("event-item");

        // Create labels for event information
        Label nameLabel = new Label("📌 " + evennement.getNomE());
        nameLabel.getStyleClass().add("event-label");

        Label locationLabel = new Label("📍 " + evennement.getLocal());
        locationLabel.getStyleClass().add("event-label");

        Label dateLabel = new Label("📅 " + evennement.getDateE().toString());
        dateLabel.getStyleClass().add("event-label");

        Label organizerLabel = new Label("👤 " + evennement.getOrganisateur().getNomOr());
        organizerLabel.getStyleClass().add("event-label");

        // New label for event type
        Label eventTypeLabel;
        if (evennement.getEventType() != null) {
            eventTypeLabel = new Label("📅 Type: " + evennement.getEventType().toString());
        } else {
            eventTypeLabel = new Label("📅 Type: Unknown");
        }
        eventTypeLabel.getStyleClass().add("event-label");

        // Buttons for modifying and deleting the event
        Button updateButton = new Button("Modifier");
        updateButton.getStyleClass().add("update-button");
        updateButton.setOnAction(e -> InterfaceUpdate(evennement));

        Button deleteButton = new Button("Supprimer");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setOnAction(e -> deleteEvennement(evennement));

        // Add all elements to the event box
        eventBox.getChildren().addAll(nameLabel, locationLabel, dateLabel, organizerLabel, eventTypeLabel, updateButton, deleteButton);

        // Add click event handler to the eventBox
        eventBox.setOnMouseClicked(event -> showEventDetails(evennement));

        return eventBox;
    }
    private void showEventDetails(Evennement evennement) {
        // Store the selected event
        this.selectedEvennement = evennement;

        // Clear the details container to remove any previous content
        detailsContainer.getChildren().clear();

        // Apply a style class to the details container for consistent styling
        detailsContainer.getStyleClass().add("details-container");

        // Create and style labels for event details
        Label nameLabel = new Label("Nom: " + evennement.getNomE());
        nameLabel.getStyleClass().add("title"); // Apply the "title" style class

        Label locationLabel = new Label("Localisation: " + evennement.getLocal());
        locationLabel.getStyleClass().add("detail"); // Apply the "detail" style class

        Label dateLabel = new Label("Date: " + evennement.getDateE().toString());
        dateLabel.getStyleClass().add("detail"); // Apply the "detail" style class

        Label descriptionLabel = new Label("Description: " + evennement.getDesE());
        descriptionLabel.getStyleClass().add("description"); // Apply the "description" style class

        Label organizerLabel = new Label("Organisateur: " + evennement.getOrganisateur().getNomOr());
        organizerLabel.getStyleClass().add("detail"); // Apply the "detail" style class

        Label eventTypeLabel = new Label("Type: " + evennement.getEventType().toString());
        eventTypeLabel.getStyleClass().add("detail"); // Apply the "detail" style class

        // Create the "Export to PDF" button
        Button exportButton = new Button("Export to PDF");
        exportButton.getStyleClass().add("export-button"); // Apply the "export-button" style class
        exportButton.setOnAction(this::exportToPDF); // Set action for the button

        // Add all elements to the details container
        detailsContainer.getChildren().addAll(
                nameLabel, locationLabel, dateLabel, descriptionLabel, organizerLabel, eventTypeLabel, exportButton
        );
    }


    @FXML
    private void exportToPDF(ActionEvent event) {
        if (selectedEvennement == null) {
            showAlert("Error", "No event selected!");
            return;
        }

        // Create a FileChooser to allow the user to choose the save location
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        // Set the default file name
        fileChooser.setInitialFileName("event_details.pdf");

        // Show the save dialog
        File file = fileChooser.showSaveDialog(detailsContainer.getScene().getWindow());

        // If the user cancels the dialog, return
        if (file == null) {
            return;
        }

        // Get the selected file path
        String filePath = file.getAbsolutePath();

        // Create the PDF document
        Document document = new Document();

        try {
            // Write the PDF to the selected file path
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            // Open the document
            document.open();

            // Add the logo and header
            com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(getClass().getResource("/Images/b2b.png"));
            logo.scaleToFit(100, 100); // Resize the logo
            logo.setAlignment(com.itextpdf.text.Image.ALIGN_CENTER);
            document.add(logo);

            // Add the application name as a header
            com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD);
            Paragraph header = new Paragraph("B2B Travel", headerFont);
            header.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            header.setSpacingAfter(10); // Add space after the header
            document.add(header);

            // Add a separator line
            document.add(new com.itextpdf.text.pdf.draw.LineSeparator());

            // Add event details in a table
            com.itextpdf.text.Font tableHeaderFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font tableContentFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12);

            com.itextpdf.text.pdf.PdfPTable table = new com.itextpdf.text.pdf.PdfPTable(2);
            table.setWidthPercentage(100); // Table width as 100% of the page
            table.setSpacingBefore(10); // Add space before the table
            table.setSpacingAfter(10); // Add space after the table

            // Add table headers
            table.addCell(new com.itextpdf.text.Phrase("Field", tableHeaderFont));
            table.addCell(new com.itextpdf.text.Phrase("Details", tableHeaderFont));

            // Add event details to the table
            table.addCell(new com.itextpdf.text.Phrase("Nom", tableContentFont));
            table.addCell(new com.itextpdf.text.Phrase(selectedEvennement.getNomE(), tableContentFont));

            table.addCell(new com.itextpdf.text.Phrase("Localisation", tableContentFont));
            table.addCell(new com.itextpdf.text.Phrase(selectedEvennement.getLocal(), tableContentFont));

            table.addCell(new com.itextpdf.text.Phrase("Date", tableContentFont));
            table.addCell(new com.itextpdf.text.Phrase(selectedEvennement.getDateE().toString(), tableContentFont));

            table.addCell(new com.itextpdf.text.Phrase("Description", tableContentFont));
            table.addCell(new com.itextpdf.text.Phrase(selectedEvennement.getDesE(), tableContentFont));

            table.addCell(new com.itextpdf.text.Phrase("Organisateur", tableContentFont));
            table.addCell(new com.itextpdf.text.Phrase(selectedEvennement.getOrganisateur().getNomOr(), tableContentFont));

            table.addCell(new com.itextpdf.text.Phrase("Type", tableContentFont));
            table.addCell(new com.itextpdf.text.Phrase(selectedEvennement.getEventType().toString(), tableContentFont));

            // Add the table to the document
            document.add(table);

            // Add a footer
            com.itextpdf.text.Font footerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.ITALIC);
            Paragraph footer = new Paragraph("Generated by B2B Travel", footerFont);
            footer.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            footer.setSpacingBefore(10); // Add space before the footer
            document.add(footer);

            // Close the document
            document.close();

            // Show a success message
            showAlert("Success", "Event details exported to PDF successfully at:\n" + filePath);
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to export event details to PDF.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the logo image.");
        }
    }
    @FXML
    private void InterfaceUpdate(Evennement evennement) {
        if (evennement == null) {
            showAlert("Erreur", "Aucun événement sélectionné !");
            return;
        }

        try {
            // Load the UpdateEvennement.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEvennement.fxml"));
            Parent root = loader.load();

            // Pass data to the update controller
            UpdateEvennement updateController = loader.getController();
            updateController.setEvennement(evennement); // Pass the selected event to the update controller

            // Open new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'événement");
            stage.show();


            // Close current window (optional)
            Stage currentStage = (Stage) eventListContainer.getScene().getWindow();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface de mise à jour.");
        }
    }

    private void deleteEvennement(Evennement evennement) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cet événement ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                evennementService.delete(evennement.getIDE());
                showAlert("Succès", "L'événement a été supprimé avec succès !");
            }
        });
    }

    @FXML
    private void interfaceAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvennement.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Evennement");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir l'interface d'ajout.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}