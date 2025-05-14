package gui;

import entities.Annonce;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.AnnonceService;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class annoncefo {
    @FXML
    private Button btnAjouter;

    @FXML
    private Label GrandTitre;

    @FXML
    private TableView<Annonce> tableview;

    @FXML
    private TableColumn<Annonce, String> titre;

    @FXML
    private TableColumn<Annonce, String> contenu;

    @FXML
    private TableColumn<Annonce, String> datepub;

    @FXML
    private TableColumn<Annonce, String> pj;

    @FXML
    private TableColumn<Annonce, Void> colAfficher;




    @FXML
    private DatePicker dateRecherchePicker;

    private final AnnonceService annonceService = new AnnonceService();


    @FXML
    public void initialize() {
        titre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        contenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        datepub.setCellValueFactory(new PropertyValueFactory<>("datePublication"));
        pj.setCellValueFactory(new PropertyValueFactory<>("pieceJointe"));

        ajouterBoutonAfficher();


        chargerAnnonces();

        // Par défaut, c'est le mode Backoffice (vous pouvez appeler la méthode toggleFrontofficeMode ici si nécessaire)

    }

    private void chargerAnnonces() {
        try {
            List<Annonce> annonces = annonceService.readAll();
            ObservableList<Annonce> observableList = FXCollections.observableList(annonces);
            tableview.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void rechercherParDate(ActionEvent event) {
        if (dateRecherchePicker.getValue() != null) {
            Date selectedDate = Date.valueOf(dateRecherchePicker.getValue());
            chargerAnnoncesFiltreesParDate(selectedDate);
        } else {
            chargerAnnonces(); // Si aucune date n'est sélectionnée, on recharge tout
        }
    }

    private void chargerAnnoncesFiltreesParDate(Date date) {
        try {
            List<Annonce> annonces = annonceService.readAll();
            List<Annonce> filtered = annonces.stream()
                    .filter(a -> a.getDatePublication() != null &&
                            a.getDatePublication().toLocalDate().equals(date.toLocalDate()))
                    .collect(Collectors.toList());
            tableview.setItems(FXCollections.observableList(filtered));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void resetTableView(ActionEvent event) {
        dateRecherchePicker.setValue(null); // Efface la date sélectionnée
        chargerAnnonces(); // Recharge toutes les annonces
    }

    private void ajouterBoutonAfficher() {
        colAfficher.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Afficher");

            {
                btn.setOnAction(event -> {
                    Annonce annonce = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsAnnonce.fxml"));
                        Parent root = loader.load();

                        DetailsAnnonce controller = loader.getController();
                        controller.setDetails(annonce);

                        Stage stage = new Stage();
                        stage.setTitle("Détails de l'annonce");
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }






    public void showNotification(Stage stage, String message, String color) {
        Popup popup = new Popup();
        Label label = new Label(message);

        // Utilisation de la couleur passée en paramètre pour le fond
        label.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 10px; -fx-font-size: 14px; -fx-background-radius: 5;");

        popup.getContent().add(label);
        popup.setAutoHide(true);

        // Positionnement de la popup en bas à droite
        popup.show(stage);
        popup.setX(stage.getX() + stage.getWidth() - 300);
        popup.setY(stage.getY() + stage.getHeight() - 100);

        // Délai avant la fermeture automatique de la notification
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    }

    @FXML
    private void ouvrirCalendrier(ActionEvent event) {
        try {
            Stage stageActuel = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActuel.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Calendrier.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Calendrier des Annonces");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


