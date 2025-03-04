package Reclamation.Controller;

import Reclamation.entities.Reponse;
import Reclamation.Services.ReponseServices;
import Reclamation.tools.MyConnection;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ListeReponseController {


    @FXML
    private VBox reponsesVBox; // Conteneur principal

    @FXML
    private TextField txtSearch; // Champ de recherche

    @FXML
    private ComboBox<String> triComboBox; // ComboBox pour le tri

    private final ReponseServices reponseServices = new ReponseServices();

    // Recharge toutes les réponses depuis la base de données
    @FXML
    public void initialize() {
        // Initialiser le ComboBox avec les options de tri
        triComboBox.getItems().addAll("Croissant", "Décroissant");
        triComboBox.setValue("Croissant"); // Valeur par défaut

        // Gérer l'événement de sélection du ComboBox
        triComboBox.setOnAction(event -> {
            String ordreTri = triComboBox.getValue();
            loadEvennements(ordreTri); // Recharger les réponses avec le tri sélectionné
        });

        // Charger les réponses avec le tri par défaut (croissant)
        loadEvennements("Croissant");
    }

    public void loadEvennements(String ordreTri) {
        reponsesVBox.getChildren().clear(); // Effacer l'affichage actuel
        ObservableList<Reponse> reponses = FXCollections.observableArrayList();

        String query = "SELECT r.IDRep, r.DescriptionRep, r.dateRep, rec.Titre AS reclamationTitre " +
                "FROM Reponse r " +
                "JOIN Reclamation rec ON r.IDR = rec.IDR";

        try (Statement stmt = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Réinitialiser la liste des réponses
            reponses.clear();

            while (rs.next()) {
                Reponse reponse = new Reponse(
                        rs.getInt("IDRep"),
                        rs.getString("DescriptionRep"),
                        rs.getDate("dateRep").toLocalDate(),
                        rs.getString("reclamationTitre")
                );

                // Vérifier si la réponse existe déjà dans la liste
                if (!reponses.contains(reponse)) {
                    reponses.add(reponse);
                    System.out.println("[DEBUG] Réponse chargée : " + reponse);
                }
            }

            // Trier les réponses par date
            if ("Croissant".equals(ordreTri)) {
                reponses.sort((r1, r2) -> r1.getDateRep().compareTo(r2.getDateRep()));
            } else if ("Décroissant".equals(ordreTri)) {
                reponses.sort((r1, r2) -> r2.getDateRep().compareTo(r1.getDateRep()));
            }

            // Afficher les réponses triées
            afficherReponses(reponses);

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    }

    private void afficherReponses(ObservableList<Reponse> reponses) {
        reponsesVBox.getChildren().clear();

        // En-tête de la table
        HBox header = new HBox(20);
        header.setStyle("-fx-padding: 10px; -fx-background-color: #3498db;");
        header.getChildren().addAll(
                createStyledLabel("Réponse", "header-label", 200),
                createStyledLabel("Titre réclamation", "header-label", 200),
                createStyledLabel("Date", "header-label", 150),
                createStyledLabel("Modifier", "header-label", 100),
                createStyledLabel("Supprimer", "header-label", 100)
        );
        reponsesVBox.getChildren().add(header);

        // Lignes de données
        for (Reponse reponse : reponses) {
            HBox row = createReponseItem(reponse);
            reponsesVBox.getChildren().add(row);
        }
    }

    private HBox createReponseItem(Reponse reponse) {
        HBox row = new HBox(20);
        row.setStyle("-fx-padding: 10px; -fx-alignment: center-left;");

        Label reponseLabel = new Label(reponse.getDescriptionRep());
        reponseLabel.setStyle("-fx-min-width: 200;");

        Label titreLabel = new Label(reponse.getReclamationTitre());
        titreLabel.setStyle("-fx-min-width: 200;");

        String dateText = (reponse.getDateRep() != null) ? reponse.getDateRep().toString() : "N/A";
        Label dateLabel = new Label(dateText);
        dateLabel.setStyle("-fx-min-width: 150;");

        Button editButton = new Button("Modifier");
        editButton.getStyleClass().add("button");
        editButton.setOnAction(e -> modifierReponse(reponse));

        Button deleteButton = new Button("Supprimer");
        deleteButton.getStyleClass().addAll("button", "delete-button");
        deleteButton.setOnAction(e -> supprimerReponse(reponse));

        row.getChildren().addAll(reponseLabel, titreLabel, dateLabel, editButton, deleteButton);
        return row;
    }

    // Gestion de la recherche
    @FXML
    private void rechercherReponses() {
        String searchText = txtSearch.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadEvennements(triComboBox.getValue()); // Recharger toutes les réponses avec le tri actuel
        } else {
            ObservableList<Reponse> reponsesFiltrees = FXCollections.observableArrayList();
            for (Reponse reponse : reponseServices.getAllData()) {
                String titre = reponse.getReclamationTitre() != null ? reponse.getReclamationTitre().toLowerCase() : "";
                String description = reponse.getDescriptionRep() != null ? reponse.getDescriptionRep().toLowerCase() : "";
                if (titre.contains(searchText) || description.contains(searchText)) {
                    reponsesFiltrees.add(reponse);
                }
            }

            if (reponsesFiltrees.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Aucun résultat");
                alert.setHeaderText(null);
                alert.setContentText("Aucune réponse ne correspond à votre recherche.");
                alert.showAndWait();
            } else {
                afficherReponses(reponsesFiltrees);
            }
        }
    }

    // Crée un label stylisé avec largeur fixe
    private Label createStyledLabel(String text, String styleClass, double width) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        label.setStyle("-fx-min-width: " + width + "; -fx-pref-width: " + width + ";");
        return label;
    }

    // Supprimer une réponse
    @FXML
    private void supprimerReponse(Reponse reponse) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la réponse");
        alert.setContentText("Êtes-vous sûr ?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (reponseServices.deleteEntity(reponse)) {
                loadEvennements(triComboBox.getValue()); // Actualiser après suppression avec le tri actuel
            }
        }
    }

    // Modifier une réponse
    @FXML
    private void modifierReponse(Reponse reponse) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReponse.fxml"));
            Parent root = loader.load();

            ModifierReponseController controller = loader.getController();
            controller.setReponse(reponse); // Passer la réponse à modifier

            // Définir le rappel pour rafraîchir la liste après la modification
            controller.setOnModificationSuccess(() -> {
                loadEvennements(triComboBox.getValue()); // Recharger la liste des réponses avec le tri actuel
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Retour à la page d'ajout de réponse
    @FXML
    private void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReponse.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void exporterEnPDF(ActionEvent event) {
        // Récupérer la liste des réponses déjà chargée dans le contrôleur
        ObservableList<Reponse> reponses = FXCollections.observableArrayList();

        // Parcourir les éléments affichés dans reponsesVBox pour récupérer les réponses
        for (javafx.scene.Node node : reponsesVBox.getChildren()) {
            if (node instanceof HBox) {
                HBox row = (HBox) node;
                // Récupérer les labels de la ligne
                Label descriptionLabel = (Label) row.getChildren().get(0); // Réponse (Description)
                Label titreLabel = (Label) row.getChildren().get(1);      // Titre de la réclamation

                // Créer un objet Reponse à partir des labels
                Reponse reponse = new Reponse();
                reponse.setDescriptionRep(descriptionLabel.getText()); // Réponse (Description)
                reponse.setReclamationTitre(titreLabel.getText());    // Titre de la réclamation

                reponses.add(reponse);
            }
        }

        try {
            // Load the Signature.fxml window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Signature.fxml"));
            Parent root = loader.load();

            // Get the SignatureController
            SignatureController signatureController = loader.getController();

            // Set the callback for when the signature is confirmed
            signatureController.setSignatureConfirmedCallback(signatureImage -> {
                // Ouvrir un FileChooser pour choisir l'emplacement du fichier
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Enregistrer le PDF");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
                File file = fileChooser.showSaveDialog(new Stage());

                if (file != null) {
                    // Exporter en PDF avec la signature
                    exporterEnPDF(reponses, file.getAbsolutePath(), signatureImage);

                    // Afficher une confirmation à l'utilisateur
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Exportation réussie");
                    alert.setHeaderText(null);
                    alert.setContentText("La liste des réponses a été exportée en PDF : " + file.getAbsolutePath());
                    alert.showAndWait();
                }
            });

            // Show the Signature.fxml window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Signature");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour exporter en PDF avec la signature
    public void exporterEnPDF(ObservableList<Reponse> reponses, String filePath, WritableImage signatureImage) {
        Document document = new Document();

        try {
            // Créer un fichier PDF
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Ajouter un titre au document
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Paragraph title = new Paragraph("Liste des Réponses", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20); // Espace après le titre
            document.add(title);

            // Créer un tableau pour afficher les données (2 colonnes : Réponse et Titre de la réclamation)
            PdfPTable table = new PdfPTable(2); // 2 colonnes
            table.setWidthPercentage(100); // Utiliser 100% de la largeur de la page

            // Ajouter les en-têtes du tableau
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            PdfPCell header1 = new PdfPCell(new Phrase("Réponse", headerFont));
            PdfPCell header2 = new PdfPCell(new Phrase("Titre de la réclamation", headerFont));

            // Styliser les en-têtes
            header1.setBackgroundColor(BaseColor.DARK_GRAY);
            header1.setHorizontalAlignment(Element.ALIGN_CENTER);
            header1.setPadding(5);

            header2.setBackgroundColor(BaseColor.DARK_GRAY);
            header2.setHorizontalAlignment(Element.ALIGN_CENTER);
            header2.setPadding(5);

            table.addCell(header1);
            table.addCell(header2);

            // Ajouter les données des réponses
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            for (Reponse reponse : reponses) {
                String description = reponse.getDescriptionRep();
                String titreReclamation = reponse.getReclamationTitre();

                // Gérer les valeurs nulles ou vides
                if (description == null || description.trim().isEmpty()) {
                    description = "N/A";
                }
                if (titreReclamation == null || titreReclamation.trim().isEmpty()) {
                    titreReclamation = "N/A";
                }

                // Ajouter les cellules au tableau
                PdfPCell cell1 = new PdfPCell(new Phrase(description, cellFont));
                PdfPCell cell2 = new PdfPCell(new Phrase(titreReclamation, cellFont));

                cell1.setPadding(5);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);

                cell2.setPadding(5);
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);

                table.addCell(cell1);
                table.addCell(cell2);
            }

            // Ajouter le tableau au document
            document.add(table);

            // Ajouter la signature section
            document.add(new Paragraph(" ")); // Ajouter un espace
            document.add(new Paragraph("Admin", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK)));

            // Convertir la signature en image et l'ajouter au PDF
            File tempFile = Files.createTempFile("signature", ".png").toFile();
            BufferedImage bufferedImage = convertWritableImageToBufferedImage(signatureImage);
            ImageIO.write(bufferedImage, "png", tempFile);

            Image signaturePdfImage = Image.getInstance(tempFile.getAbsolutePath());
            signaturePdfImage.scaleToFit(200, 100); // Redimensionner l'image de la signature
            signaturePdfImage.setAlignment(Element.ALIGN_LEFT);
            document.add(signaturePdfImage);

            // Fermer le document
            document.close();

            System.out.println("✅ Fichier PDF généré avec succès : " + filePath);
        } catch (DocumentException | IOException e) {
            System.err.println("❌ Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }

    // Helper method to convert WritableImage to BufferedImage
    private BufferedImage convertWritableImageToBufferedImage(WritableImage writableImage) {
        int width = (int) writableImage.getWidth();
        int height = (int) writableImage.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferedImage.setRGB(x, y, writableImage.getPixelReader().getArgb(x, y));
            }
        }

        return bufferedImage;
    }
}