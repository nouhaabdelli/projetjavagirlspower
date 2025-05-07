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

public class Annonces {

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
    private TableColumn<Annonce, Void> colModifier;

    @FXML
    private TableColumn<Annonce, Void> colSupprimer;

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
        ajouterBoutonModifier();
        ajouterBoutonSupprimer();

        chargerAnnonces();
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

    private void ajouterBoutonModifier() {
        colModifier.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setOnAction(event -> {
                    Annonce annonce = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAnnonce.fxml"));
                        Parent root = loader.load();

                        ModifierAnnonce controller = loader.getController();
                        controller.initialize(annonce);

                        Stage stage = new Stage();
                        stage.setTitle("Modifier l'annonce");
                        stage.setScene(new Scene(root));
                        stage.show();

                        stage.setOnHidden(e -> chargerAnnonces());
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

    private void ajouterBoutonSupprimer() {
        colSupprimer.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");

            {
                btn.setOnAction(event -> {
                    Annonce annonce = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Voulez-vous vraiment supprimer cette annonce ?");
                    alert.setContentText("Cette action est irréversible.");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            annonceService.delete(annonce);
                            chargerAnnonces();
                            Alert success = new Alert(Alert.AlertType.INFORMATION);
                            success.setHeaderText("Suppression réussie");
                            success.setContentText("L'annonce a été supprimée.");
                            success.showAndWait();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
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

    @FXML
    void btnajout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAnnonce.fxml"));
            Parent root = loader.load();

            AjouterAnnonce controller = loader.getController(); // Récupérer le contrôleur de la fenêtre d'ajout

            Stage stage = new Stage();
            stage.setTitle("Ajouter une annonce");
            stage.setScene(new Scene(root));
            stage.show();

            // Lorsque la fenêtre d'ajout est fermée, charger les annonces et afficher la notification si nécessaire
            stage.setOnHidden(e -> {
                chargerAnnonces();

                // Vérifie si l'annonce a été ajoutée avec succès
                if (controller.isAnnonceAjoutee()) {
                    // Utilise le Stage principal pour afficher la notification en vert (succès)
                    showNotification((Stage) ((Node) event.getSource()).getScene().getWindow(), "Annonce ajoutée avec succès !", "#4CAF50");
                } else {
                    // En cas d'échec, afficher une notification en rouge (erreur)
                    showNotification((Stage) ((Node) event.getSource()).getScene().getWindow(), "Erreur lors de l'ajout de l'annonce.", "#F44336");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
