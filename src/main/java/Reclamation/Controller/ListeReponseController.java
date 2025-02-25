package Reclamation.Controller;

import Reclamation.entities.Reponse;
import Reclamation.Services.ReponseServices;
import Reclamation.tools.MyConnection;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;



public class ListeReponseController {

    @FXML
    private VBox reponsesVBox; // Conteneur principal

    @FXML
    private TextField txtSearch; // Champ de recherche

    private final ReponseServices reponseServices = new ReponseServices();
    private int reclamationId;

    // Chargement initial
    @FXML
    public void initialize() {
        loadEvennements();
    }

    // Recharge toutes les réponses depuis la base de données
    public void loadEvennements() {
        reponsesVBox.getChildren().clear();
        ObservableList<Reponse> reponses = FXCollections.observableArrayList();

        String query = "SELECT r.IDRep, rec.Titre AS reclamationTitre, r.DescriptionRep " +
                "FROM Reponse r " +
                "JOIN Reclamation rec ON r.IDR = rec.IDR";

        try (Statement stmt = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Reponse reponse = new Reponse(
                        rs.getInt("IDRep"),
                        rs.getString("DescriptionRep"),
                        rs.getString("reclamationTitre")
                );
                reponses.add(reponse);
            }

            afficherReponses(reponses);

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    }

    // Affiche les réponses dans le VBox
    private void afficherReponses(ObservableList<Reponse> reponses) {
        reponsesVBox.getChildren().clear();

        // En-tête de la table
        HBox header = new HBox(20);
        header.setStyle("-fx-padding: 10px; -fx-background-color: #3498db;");
        header.getChildren().addAll(
                createStyledLabel("Réponse", "header-label", 300),
                createStyledLabel("Titre réclamation", "header-label", 300),
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

    // Crée une ligne pour une réponse
    private HBox createReponseItem(Reponse reponse) {
        HBox row = new HBox(20);
        row.setStyle("-fx-padding: 10px; -fx-alignment: center-left;");

        Label reponseLabel = new Label(reponse.getDescriptionRep());
        reponseLabel.setStyle("-fx-min-width: 300;");

        Label titreLabel = new Label(reponse.getReclamationTitre());
        titreLabel.setStyle("-fx-min-width: 300;");

        Button editButton = new Button("Modifier");
        editButton.getStyleClass().add("button");
        editButton.setOnAction(e -> modifierReponse(reponse));

        Button deleteButton = new Button("Supprimer");
        deleteButton.getStyleClass().addAll("button", "delete-button");
        deleteButton.setOnAction(e -> supprimerReponse(reponse));

        row.getChildren().addAll(reponseLabel, titreLabel, editButton, deleteButton);
        return row;
    }

    // Gestion de la recherche
    @FXML
    private void rechercherReponses() {
        String searchText = txtSearch.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            loadEvennements(); // Recharge toutes les réponses
        } else {
            ObservableList<Reponse> reponsesFiltrees = FXCollections.observableArrayList();
            for (Reponse reponse : reponseServices.getAllData()) {
                String titre = reponse.getReclamationTitre() != null ? reponse.getReclamationTitre().toLowerCase() : "";
                String description = reponse.getDescriptionRep() != null ? reponse.getDescriptionRep().toLowerCase() : "";
                if (titre.contains(searchText) || description.contains(searchText)) {
                    reponsesFiltrees.add(reponse);
                }
            }
            afficherReponses(reponsesFiltrees);
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
                loadEvennements(); // Actualiser après suppression
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
                loadEvennements(); // Recharger la liste des réponses
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

    // Exporter la liste des réponses en PDF
    @FXML
    private void exporterEnPDF(ActionEvent event) {
        // Récupérer la liste des réponses
        List<Reponse> reponses = reponseServices.getAllData();

        // Ouvrir un FileChooser pour choisir l'emplacement du fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            // Exporter en PDF
            exporterEnPDF(reponses, file.getAbsolutePath());

            // Afficher une confirmation à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportation réussie");
            alert.setHeaderText(null);
            alert.setContentText("La liste des réponses a été exportée en PDF : " + file.getAbsolutePath());
            alert.showAndWait();
        }
    }

    // Méthode pour exporter en PDF
    public void exporterEnPDF(List<Reponse> reponses, String filePath) {
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

            // Créer un tableau pour afficher les données
            PdfPTable table = new PdfPTable(2); // 2 colonnes : Description et Titre de la réclamation
            table.setWidthPercentage(100); // Utiliser 100% de la largeur de la page
            table.setSpacingBefore(10); // Espace avant le tableau
            table.setSpacingAfter(10); // Espace après le tableau

            // Définir la largeur des colonnes
            float[] columnWidths = {70f, 30f}; // 70% pour la description, 30% pour le titre
            table.setWidths(columnWidths);

            // Ajouter les en-têtes du tableau
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            PdfPCell header1 = new PdfPCell(new Phrase("Description", headerFont));
            PdfPCell header2 = new PdfPCell(new Phrase("Titre de la Réclamation", headerFont));

            // Styliser les en-têtes
            header1.setBackgroundColor(BaseColor.DARK_GRAY);
            header1.setHorizontalAlignment(Element.ALIGN_CENTER);
            header1.setPadding(5);
            header1.setBorderWidth(1); // Ajouter une bordure

            header2.setBackgroundColor(BaseColor.DARK_GRAY);
            header2.setHorizontalAlignment(Element.ALIGN_CENTER);
            header2.setPadding(5);
            header2.setBorderWidth(1); // Ajouter une bordure

            table.addCell(header1);
            table.addCell(header2);

            // Ajouter les données des réponses
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            for (Reponse reponse : reponses) {
                // Vérifier si le titre de la réclamation est vide
                String titreReclamation = reponse.getReclamationTitre();
                if (titreReclamation == null || titreReclamation.trim().isEmpty()) {
                    titreReclamation = "N/A"; // Valeur par défaut
                }

                PdfPCell cell1 = new PdfPCell(new Phrase(reponse.getDescriptionRep(), cellFont));
                PdfPCell cell2 = new PdfPCell(new Phrase(titreReclamation, cellFont));

                // Styliser les cellules
                cell1.setPadding(5);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell1.setBorderWidth(1); // Ajouter une bordure

                cell2.setPadding(5);
                cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell2.setBorderWidth(1); // Ajouter une bordure

                table.addCell(cell1);
                table.addCell(cell2);
            }

            // Ajouter le tableau au document
            document.add(table);

            // Fermer le document
            document.close();

            System.out.println("✅ Fichier PDF généré avec succès : " + filePath);
        } catch (DocumentException | IOException e) {
            System.err.println("❌ Erreur lors de la génération du PDF : " + e.getMessage());

        }
    }
}