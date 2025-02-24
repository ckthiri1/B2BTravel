package CRUD.controllers;

import CRUD.entities.Hebergement;
import CRUD.entities.Reservation;
import CRUD.services.HebergementService;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.itextpdf.text.Document;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.util.List;

import javafx.scene.Node;
import javafx.util.Duration;

public class AffichageHebergementC {

    @FXML
    private TableColumn<Hebergement, Void> actionColumn;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<Hebergement, String> adresseColumn;

    @FXML
    private Button ajouterHebergement;
    @FXML
    private Button listeR;

    @FXML
    private Button actualiser;
    @FXML
    private TableColumn<Hebergement, String > descriptionColumn;

    @FXML
    private TableColumn<Hebergement, String> nomColumn;

    @FXML
    private Button retour;
    @FXML
    private GridPane hebergementGrid;

    @FXML
    private TableView<Hebergement> tableView;
    @FXML
    private Label successMessage;
    @FXML
    private ImageView addIcon;
    @FXML
    private ImageView sortIcon, searchIcon, statsIcon, pdfIcon;
    @FXML
    private TableColumn<Hebergement, String> typeColumn;
    private final HebergementService hebergementService = new HebergementService();
/*
    @FXML
    void initialize() {
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        ImageView backIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/fleche-de-retour.png"))));
        backIcon.setFitWidth(15);
        backIcon.setFitHeight(15);

        retour.setGraphic(backIcon);
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView deleteIcon = new ImageView();
            private final ImageView editIcon = new ImageView();
            private final HBox buttonBox = new HBox(20, editIcon, deleteIcon);

            {
                // Charger les images (Assurez-vous que les fichiers sont bien placés dans /resources/images/)
                deleteIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bouton-supprimer.png"))));
                editIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/editer1.png"))));

                // Définir la taille des icônes
                deleteIcon.setFitWidth(25);
                deleteIcon.setFitHeight(25);
                editIcon.setFitWidth(25);
                editIcon.setFitHeight(25);

                // Ajouter les actions
                deleteIcon.setOnMouseClicked(event -> {
                    Hebergement hebergement = getTableView().getItems().get(getIndex());
                    boolean deleted = hebergementService.deleteEntity(hebergement);
                    if (deleted) {
                        getTableView().getItems().remove(hebergement);
                    } else {
                        System.out.println("Échec de la suppression de la réservation.");
                    }
                });

                editIcon.setOnMouseClicked(event -> {
                    try {
                        // Récupérer la réservation sélectionnée depuis la cellule actuelle
                        Hebergement selectedHebergement =tableView.getSelectionModel().getSelectedItem();

                        if (selectedHebergement == null) {
                            successMessage.setText("Erreur, veuillez sélectionner un hébergement à modifier.");
                            successMessage.setVisible(true);
                            new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    successMessage.setVisible(false);
                                } catch (InterruptedException ignored) {}
                            }).start();
                            return;
                        }

                        // Charger la page ModifierHebergement.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierHebergement.fxml"));
                        Parent root = loader.load();

                        // Récupérer le contrôleur de la page et passer les données
                        ModifierHebergement controller = loader.getController();
                        controller.initData(selectedHebergement, this);

                        Stage stage = (Stage) ((ImageView) event.getSource()).getScene().getWindow();
                        stage.getScene().setRoot(root);
                    } catch (IOException e) {
                        System.out.println("Erreur lors de l’ouverture de la modification : " + e.getMessage());
                    }
                });


            }



            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        });
        showHebergement();
    }

    private void showHebergement() {
        List<Hebergement> hebergements = hebergementService.getAllData(); // Récupération des données
        tableView.getItems().setAll(hebergements); // Mise à jour de la TableView
    }
*/
@FXML
void initialize() {
    if (searchField == null) {
        System.out.println("searchField is null");
    } else {
        System.out.println("searchField is initialized");
    }
    showHebergement();
    addIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/plus-symbole-noir1.png"))));
    addIcon.setFitWidth(20);
    addIcon.setFitHeight(20);
    addIcon.setOnMouseClicked(event -> ajouterHebergement(event));

    sortIcon.setOnMouseClicked(event -> sortHebergement());
    pdfIcon.setOnMouseClicked(event -> generatePDF(event));
    statsIcon.setOnMouseClicked(event -> showStats(event));
    // Assurez-vous que la méthode est appelée après l'initialisation
    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
        searchHebergement(new ActionEvent());
    });
}


    private void showHebergement() {
        hebergementGrid.getChildren().clear();
        List<Hebergement> hebergements = hebergementService.getAllData();

        int columns = 3; // Number of columns per row
        int row = 0;
        int col = 0;

        for (Hebergement hebergement : hebergements) {
            VBox hebergementCard = createHebergementCard(hebergement);
            hebergementGrid.add(hebergementCard, col, row);

            col++;
            if (col == columns) { // Move to the next row after X columns
                col = 0;
                row++;
            }
        }
    }

    private VBox createHebergementCard(Hebergement hebergement) {
        VBox card = new VBox(10); // Spacing between elements
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 10; -fx-padding: 15; -fx-alignment: center;");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200); // Fixed width for each card

        // Labels (centered)
        Label nomLabel = new Label(hebergement.getNom());
        Label adresseLabel = new Label("📍 " + hebergement.getAdresse());
        Label typeLabel = new Label("🏠 " + hebergement.getType());
        Label descriptionLabel = new Label("📃 " + hebergement.getDescription());

        nomLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        adresseLabel.setStyle("-fx-font-size: 12px;");
        typeLabel.setStyle("-fx-font-size: 12px;");
        descriptionLabel.setStyle("-fx-font-size: 12px;");

        // Edit and Delete icons
        ImageView editIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/editer1.png"))));
        editIcon.setFitWidth(25);
        editIcon.setFitHeight(25);
        editIcon.setOnMouseClicked(event -> editHebergement(hebergement));

        ImageView deleteIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bouton-supprimer.png"))));
        deleteIcon.setFitWidth(25);
        deleteIcon.setFitHeight(25);
        deleteIcon.setOnMouseClicked(event -> deleteHebergement(hebergement));

        HBox actionsBox = new HBox(15, editIcon, deleteIcon);
        actionsBox.setAlignment(Pos.CENTER);

        // Add elements to the card vertically
        card.getChildren().addAll(nomLabel, adresseLabel, typeLabel, descriptionLabel, actionsBox);

        return card;
    }

    private void editHebergement(Hebergement hebergement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierHebergement.fxml"));
            Parent root = loader.load();
            ModifierHebergement controller = loader.getController();
            controller.initData(hebergement, this);

            Stage stage = (Stage) hebergementGrid.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors de l’ouverture de la modification : " + e.getMessage());
        }
    }

    private void deleteHebergement(Hebergement hebergement) {
        boolean deleted = hebergementService.deleteEntity(hebergement);
        if (deleted) {
            showHebergement(); // Refresh the display after deletion
        } else {
            System.out.println("Échec de la suppression de l'hébergement.");
        }
    }

    @FXML
    void Retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterHebergement.fxml")); // Page précédente
            Parent root = loader.load();

            // Récupérer la scène actuelle et remplacer son contenu
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du retour : " + e.getMessage());
        }
    }


    @FXML
    void ajouterHebergement(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterHebergement.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle et remplacer son contenu
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page AjouterReservation: " + e.getMessage());
        }
    }
    @FXML
    void Listeres(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Affichage.fxml")); // Page précédente
            Parent root = loader.load();
            final ImageView Liste = new ImageView();
            Liste.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/liste-de-controle.png"))));
            Liste.setFitWidth(25);
            Liste.setFitHeight(25);
            // Récupérer la scène actuelle et remplacer son contenu
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println("Erreur lors du retour : " + e.getMessage());
        }
    }

    @FXML
    void actualiserH(ActionEvent event) {

        showHebergement();

    }


    public void generatePDF(MouseEvent event) {
        // Générez le PDF avec la liste des hébergements
        generateHebergementPDF(hebergementService.getAllData());
    }

    public static void generateHebergementPDF(List<Hebergement> allData) {
        String filePath = "Liste_Hebergement.pdf";

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, fileOutputStream);
            document.open();

            // Titre centré et stylisé
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
            Paragraph title = new Paragraph("Liste des Hebergements", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Création du tableau
            PdfPTable table = new PdfPTable(4); // 4 colonnes : Nom, Adresse, Type, Description
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{2, 3, 2, 4}); // Largeur relative des colonnes

            // Style des en-têtes de colonnes
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(0, 102, 204); // Bleu

            String[] headers = {"Nom", "Adresse", "Type", "Description"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                headerCell.setBackgroundColor(headerColor);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            }

            // Ajout des données dans le tableau
            Font cellFont = new Font(Font.FontFamily.HELVETICA, 10);
            for (Hebergement hebergement : allData) {
                table.addCell(new PdfPCell(new Phrase(hebergement.getNom(), cellFont)));
                table.addCell(new PdfPCell(new Phrase(hebergement.getAdresse(), cellFont)));
                table.addCell(new PdfPCell(new Phrase(hebergement.getType(), cellFont)));
                table.addCell(new PdfPCell(new Phrase(hebergement.getDescription(), cellFont)));
            }

            document.add(table);
            document.close();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "PDF généré avec succès : " + filePath);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        System.out.println(title + ": " + message);
    }



    public void sortHebergement() {
        // Trier par nom de manière alphabétique
        List<Hebergement> hebergements = hebergementService.getAllData();
        hebergements.sort(Comparator.comparing(Hebergement::getNom)); // Tri des hébergements par nom

        // Vider le GridPane avant d'ajouter les éléments triés
        hebergementGrid.getChildren().clear();

        int columns = 3; // Nombre de colonnes par ligne
        int row = 0, col = 0;

        for (Hebergement hebergement : hebergements) {
            VBox hebergementCard = createHebergementCard(hebergement); // Créer la carte pour chaque hébergement
            hebergementGrid.add(hebergementCard, col, row); // Ajouter la carte au GridPane

            col++;
            if (col == columns) { // Passer à la ligne suivante après X colonnes
                col = 0;
                row++;
            }
        }
    }






   /* public void showStats(MouseEvent actionEvent) {
        int totalHebergements = hebergementService.getAllData().size();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Statistiques");
        alert.setHeaderText("Total des Hébergements");
        alert.setContentText("Total: " + totalHebergements);
        alert.show();
    }*/
   public void showStats(MouseEvent event) {
       // Récupérer les données des réservations
       List<Hebergement> hebergements = hebergementService.getAllData();

       // Compter les réservations par statut
       Map<String, Integer> stats = new HashMap<>();
       for (Hebergement res : hebergements) {
           stats.put(res.getType(), stats.getOrDefault(res.getType(), 0) + 1);
       }

       // Créer le PieChart
       PieChart pieChart = new PieChart();
       for (Map.Entry<String, Integer> entry : stats.entrySet()) {
           PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
           pieChart.getData().add(slice);
       }

       // Ajouter une animation pour afficher progressivement les tranches
       SequentialTransition sequentialTransition = new SequentialTransition();
       for (PieChart.Data slice : pieChart.getData()) {
           TranslateTransition transition = new TranslateTransition(Duration.millis(500), slice.getNode());
           transition.setFromY(-20);
           transition.setToY(0);
           sequentialTransition.getChildren().add(transition);
       }

       // Ajouter un délai avant de commencer l'animation
       PauseTransition delay = new PauseTransition(Duration.millis(300));
       sequentialTransition.getChildren().add(0, delay);

       // Lancer l'animation après l'affichage
       sequentialTransition.play();

       // Ajouter le graphique à une nouvelle fenêtre
       StackPane root = new StackPane(pieChart);
       Stage stage = new Stage();
       stage.initModality(Modality.APPLICATION_MODAL);
       stage.setTitle("Statistiques des Hebergements");
       stage.setScene(new Scene(root, 500, 400));
       stage.show();
   }

    @FXML
    void searchHebergement(ActionEvent actionEvent) {
        String searchText = searchField.getText().toLowerCase(); // Récupérer la valeur du champ de recherche
        hebergementGrid.getChildren().clear();
        List<Hebergement> allHebergements = hebergementService.getAllData();

        List<Hebergement> filteredHebergements = allHebergements.stream()
                .filter(h -> h.getNom().toLowerCase().contains(searchText) ||
                        h.getAdresse().toLowerCase().contains(searchText) ||
                        h.getType().toLowerCase().contains(searchText) ||
                        h.getDescription().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        int columns = 3; // Nombre de colonnes par ligne
        int row = 0, col = 0;

        for (Hebergement hebergement : filteredHebergements) {
            VBox hebergementCard = createHebergementCard(hebergement);
            hebergementGrid.add(hebergementCard, col, row);

            col++;
            if (col == columns) { // Passer à la ligne suivante après X colonnes
                col = 0;
                row++;
            }
        }
    }

}


