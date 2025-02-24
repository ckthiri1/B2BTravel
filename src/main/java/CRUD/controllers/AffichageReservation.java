package CRUD.controllers;

import CRUD.entities.Hebergement;
import CRUD.entities.Reservation;
import CRUD.services.HebergementService;
import CRUD.services.ReservationService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import javafx.scene.Node;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.scene.control.Alert;
import java.io.FileOutputStream;
import java.util.List;

import java.util.Objects;
import java.util.stream.Collectors;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AffichageReservation {
    @FXML
    private TableView<Reservation> tableView;

    @FXML
    private TableColumn<Reservation, String> hebergementColumn;
    @FXML
    private TableColumn<Reservation, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Reservation, Integer> prixColumn;
    @FXML
    private TableColumn<Reservation, String> statusColumn;
    @FXML
    private TableColumn<Reservation, Void> actionTV;
    @FXML
    private Button retour;

    @FXML
    private Button listeH;
    @FXML
    private Label successMessage;
    @FXML
    private VBox reservationContainer;
    @FXML

    private ScrollPane scrollPane;
    @FXML
    private ImageView addIcon;
    @FXML
    private GridPane reservationGrid;
    @FXML
    private TextField searchField;
    @FXML
    private ImageView sortIcon, searchIcon, statsIcon, pdfIcon;
    private final ReservationService reservationService = new ReservationService();
    private final HebergementService hebergementService = new HebergementService();

/*
    @FXML
    void initialize() {
        hebergementColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHebergementName()));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_reservation"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        ImageView backIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/fleche-de-retour.png"))));
        backIcon.setFitWidth(15);
        backIcon.setFitHeight(15);

        retour.setGraphic(backIcon);
        actionTV.setCellFactory(param -> new TableCell<>() {
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
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    boolean deleted = reservationService.deleteEntity(reservation);
                    if (deleted) {
                        getTableView().getItems().remove(reservation);
                    } else {
                        System.out.println("Échec de la suppression de la réservation.");
                    }
                });

                editIcon.setOnMouseClicked(event -> {
                    try {
                        // Récupérer la réservation sélectionnée
                        Reservation selectedReservation = tableView.getSelectionModel().getSelectedItem();
                        if (selectedReservation == null) {
                            successMessage.setText( "Erreur ,Veuillez sélectionner une réservation à modifier.");
                            successMessage.setVisible(true);
                            new Thread(() -> {
                                try {
                                    Thread.sleep(3000);
                                    successMessage.setVisible(false);
                                } catch (InterruptedException ignored) {}
                            }).start();
                            return;
                        }

                        // Charger la page ModifierReservation.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
                        Parent root = loader.load();

                        // Récupérer le contrôleur de la page et passer les données
                        ModifierReservationC controller = loader.getController();
                        controller.initData(selectedReservation, this);

                        // Ouvrir la nouvelle fenêtre
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
        showReservations();
    }
   */

    @FXML
    void initialize() {
        if (searchField == null) {
            System.out.println("searchField is null");
        } else {
            System.out.println("searchField is initialized");
        }
        showReservations();
        addIcon.setOnMouseClicked(event -> consulter(new ActionEvent(addIcon, null)));

        addIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/plus-symbole-noir1.png"))));
        addIcon.setFitWidth(20);
        addIcon.setFitHeight(20);

        sortIcon.setOnMouseClicked(event -> sortReservation());
        pdfIcon.setOnMouseClicked(event -> generatePDF(event));
        statsIcon.setOnMouseClicked(event -> showStats(event));
        // Assurez-vous que la méthode est appelée après l'initialisation
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchReservation(new ActionEvent());
        });
    }

    void showReservations() {
        reservationGrid.getChildren().clear();
        List<Reservation> reservations = reservationService.getAllData();

        int columns = 3; // Nombre de colonnes par ligne
        int row = 0;
        int col = 0;

        for (Reservation reservation : reservations) {
            Hebergement hebergement = hebergementService.getById(reservation.getHebergement_id());
            if (hebergement != null) {
                reservation.setHebergementName(hebergement.getNom());
            } else {
                reservation.setHebergementName("Hébergement supprimé");
            }

            // Créer une carte pour chaque réservation
            VBox reservationCard = createReservationCard(reservation);
            reservationGrid.add(reservationCard, col, row);

            col++;
            if (col == columns) { // Passer à la ligne suivante après X colonnes
                col = 0;
                row++;
            }
        }
    }

    private VBox createReservationCard(Reservation reservation) {
        VBox card = new VBox(10); // Espacement entre les éléments
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #dddddd; -fx-border-radius: 10; -fx-padding: 15; -fx-alignment: center;");
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200); // Largeur fixe pour chaque carte

        // Labels (centrés)
        Label hebergementLabel = new Label(reservation.getHebergementName());
        Label dateLabel = new Label("📅 " + reservation.getDate_reservation().toString());
        Label prixLabel = new Label("💰 " + reservation.getPrix() + " €");
        Label statusLabel = new Label("📌 " + reservation.getStatus());

        hebergementLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        dateLabel.setStyle("-fx-font-size: 12px;");
        prixLabel.setStyle("-fx-font-size: 12px;");
        statusLabel.setStyle("-fx-font-size: 12px;");

        // Icônes Modifier et Supprimer
        ImageView editIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/editer1.png"))));
        editIcon.setFitWidth(25);
        editIcon.setFitHeight(25);
        editIcon.setOnMouseClicked(event -> editReservation(reservation));

        ImageView deleteIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/bouton-supprimer.png"))));
        deleteIcon.setFitWidth(25);
        deleteIcon.setFitHeight(25);
        deleteIcon.setOnMouseClicked(event -> deleteReservation(reservation));

        HBox actionsBox = new HBox(15, editIcon, deleteIcon);
        actionsBox.setAlignment(Pos.CENTER);

        // Ajouter les éléments à la carte verticalement
        card.getChildren().addAll(hebergementLabel, dateLabel, prixLabel, statusLabel, actionsBox);

        return card;
    }


    private void editReservation(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReservation.fxml"));
            Parent root = loader.load();
            ModifierReservationC controller = loader.getController();
            controller.initData(reservation, this);

            Stage stage = (Stage) reservationGrid.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors de l’ouverture de la modification : " + e.getMessage());
        }
    }

    private void deleteReservation(Reservation reservation) {
        boolean deleted = reservationService.deleteEntity(reservation);
        if (deleted) {
            showReservations(); // Rafraîchir l'affichage après suppression
        } else {
            System.out.println("Échec de la suppression de la réservation.");
        }
    }

   /* void showReservations() {
        tableView.getItems().clear();
        List<Reservation> reservations = reservationService.getAllData();
        for (Reservation reservation : reservations) {
            Hebergement hebergement = hebergementService.getById(reservation.getHebergement_id());
            if (hebergement != null) {
                reservation.setHebergementName(hebergement.getNom());
            } else {
                reservation.setHebergementName("Hebergement supprimé");
            }
            tableView.getItems().add(reservation);
        }
    }*/


    public void consulter(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle et remplacer son contenu
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page AjouterReservation: " + e.getMessage());
        }
    }


    public void Retour(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReservation.fxml")); // Page précédente
            Parent root = loader.load();

            // Récupérer la scène actuelle et remplacer son contenu
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du retour : " + e.getMessage());
        }
    }

    public void ListeH(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageHebergement.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle et remplacer son contenu
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de AffichageHebergement.fxml : " + e.getMessage());
        }
    }

    public void generatePDF(MouseEvent event) {
        // Générez le PDF avec la liste des hébergements
        generateResercationPDF(reservationService.getAllData());
    }
    public void generateResercationPDF(List<Reservation> allData) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("reservation_list.pdf"));
            document.open();

            // Service pour récupérer le nom d'hébergement
            HebergementService hebergementService = new HebergementService();

            // Ajout du titre en couleur et en gras
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLUE);
            Paragraph title = new Paragraph("Liste des Réservations", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("\n"));

            // Création du tableau avec 4 colonnes
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Définition des largeurs des colonnes
            float[] columnWidths = {2f, 2f, 2f, 3f};
            table.setWidths(columnWidths);

            // Style d'en-tête
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(52, 152, 219); // Bleu

            String[] headers = {"Date", "Prix", "Statut", "Nom Hébergement"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(headerColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Remplissage des données avec alternance de couleurs
            boolean alternateColor = false;
            BaseColor rowColor1 = new BaseColor(224, 242, 255); // Bleu clair
            BaseColor rowColor2 = BaseColor.WHITE; // Blanc

            for (Reservation reservation : allData) {
                BaseColor rowColor = alternateColor ? rowColor1 : rowColor2;
                alternateColor = !alternateColor;

                // Récupération du nom de l'hébergement
                Hebergement hebergement = hebergementService.getById(reservation.getHebergement_id());
                String nomHebergement = (hebergement != null) ? hebergement.getNom() : "Non défini";

                addCellToTable(table, reservation.getDate_reservation().toString(), rowColor);
                addCellToTable(table, String.valueOf(reservation.getPrix()), rowColor);
                addCellToTable(table, reservation.getStatus(), rowColor);
                addCellToTable(table, nomHebergement, rowColor);
            }

            document.add(table);
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "PDF généré avec succès !");
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la génération du PDF : " + e.getMessage());
            alert.show();
        }
    }

    private void addCellToTable(PdfPTable table, String content, BaseColor backgroundColor) {
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
        PdfPCell cell = new PdfPCell(new Phrase(content, cellFont));
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
    public void sortReservation() {
        List<Reservation> reservations = reservationService.getAllData();
        reservations.sort(Comparator.comparing(Reservation::getDate_reservation)); // Tri par date

        reservationGrid.getChildren().clear(); // Efface l'affichage actuel

        int columns = 3;
        int row = 0;
        int col = 0;

        for (Reservation reservation : reservations) {
            Hebergement hebergement = hebergementService.getById(reservation.getHebergement_id());
            if (hebergement != null) {
                reservation.setHebergementName(hebergement.getNom());
            } else {
                reservation.setHebergementName("Hébergement supprimé");
            }

            VBox reservationCard = createReservationCard(reservation);
            reservationGrid.add(reservationCard, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }
    }

    /* public void showStats(MouseEvent actionEvent) {
        int totalReservation = reservationService.getAllData().size();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Statistiques");
        alert.setHeaderText("Total des Reservations");
        alert.setContentText("Total: " + totalReservation);
        alert.show();
    }*/
   public void showStats(MouseEvent event) {
       // Récupérer les données des réservations
       List<Reservation> reservations = reservationService.getAllData();

       // Compter les réservations par statut
       Map<String, Integer> stats = new HashMap<>();
       for (Reservation res : reservations) {
           stats.put(res.getStatus(), stats.getOrDefault(res.getStatus(), 0) + 1);
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
       stage.setTitle("Statistiques des Réservations");
       stage.setScene(new Scene(root, 500, 400));
       stage.show();
   }
    @FXML
    void searchReservation(ActionEvent actionEvent) {
        String searchText = searchField.getText().toLowerCase().trim(); // Récupérer la valeur du champ de recherche
        reservationGrid.getChildren().clear(); // Nettoyer la grille

        List<Reservation> allReservations = reservationService.getAllData(); // Récupérer toutes les réservations

        List<Reservation> filteredReservations = allReservations.stream()
                .filter(r ->
                        r.getStatus().toLowerCase().contains(searchText) || // Filtrer par statut
                                String.valueOf(r.getPrix()).contains(searchText) || // Filtrer par prix
                                r.getDate_reservation().toString().contains(searchText) || // Filtrer par date
                                getHebergementName(r.getHebergement_id()).toLowerCase().contains(searchText) // Filtrer par nom hébergement
                )
                .collect(Collectors.toList());

        int columns = 3;
        int row = 0, col = 0;

        for (Reservation reservation : filteredReservations) {
            VBox reservationCard = createReservationCard(reservation);
            reservationGrid.add(reservationCard, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }
    }

    // Fonction pour récupérer le nom de l'hébergement
    private String getHebergementName(int hebergementId) {
        Hebergement hebergement = hebergementService.getById(hebergementId);
        return (hebergement != null) ? hebergement.getNom() : "Hébergement supprimé";
    }

}
