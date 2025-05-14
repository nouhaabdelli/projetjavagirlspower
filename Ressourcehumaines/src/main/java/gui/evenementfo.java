package gui;

import entities.Evenement;
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
import java.time.LocalDate;

import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import services.EvenementService;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class evenementfo {

    @FXML
    private TableView<Evenement> tableview;

    @FXML
    private TableColumn<Evenement, String> nomEvenement;
    @FXML
    private TableColumn<Evenement, String> description;
    @FXML
    private TableColumn<Evenement, String> dateDebut;
    @FXML
    private TableColumn<Evenement, String> dateFin;
    @FXML
    private TableColumn<Evenement, String> lieu;
    @FXML
    private TableColumn<Evenement, String> organisateur;
    @FXML
    private TableColumn<Evenement, Integer> participantsMax;
    @FXML
    private TableColumn<Evenement, String> statut;
    @FXML
    private TableColumn<Evenement, String> photo;

    @FXML
    private TableColumn<Evenement, Void> colAfficher;

    @FXML
    private TableColumn<Evenement, Void> colParticiper;


    @FXML
    private Button btnAjouter;

    @FXML
    private DatePicker dateRecherchePicker;

    private final EvenementService evenementService = new EvenementService();

    @FXML
    public void initialize() {
        nomEvenement.setCellValueFactory(new PropertyValueFactory<>("nom"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        lieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        organisateur.setCellValueFactory(new PropertyValueFactory<>("organisateur"));
        participantsMax.setCellValueFactory(new PropertyValueFactory<>("participantsMax"));
        statut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        photo.setCellValueFactory(new PropertyValueFactory<>("photo"));

        ajouterBoutonAfficher();
        ajouterBoutonParticiper();


        chargerEvenements();
    }

    private void chargerEvenements() {
        try {
            List<Evenement> evenements = evenementService.readAll();
            tableview.setItems(FXCollections.observableList(evenements));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void chargerEvenementsFiltresParDate(Date date) {
        try {
            List<Evenement> evenements = evenementService.readAll();
            List<Evenement> filtres = evenements.stream()
                    .filter(e -> e.getDateDebut() != null &&
                            e.getDateDebut().toLocalDate().equals(date.toLocalDate()))
                    .collect(Collectors.toList());
            tableview.setItems(FXCollections.observableList(filtres));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void rechercherParDate(ActionEvent event) {
        if (dateRecherchePicker.getValue() != null) {
            Date selectedDate = Date.valueOf(dateRecherchePicker.getValue());
            chargerEvenementsFiltresParDate(selectedDate);
        } else {
            chargerEvenements(); // Si aucune date n'est sélectionnée, on recharge tout
        }
    }

    private void ajouterBoutonAfficher() {
        colAfficher.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Afficher");

            {
                btn.setOnAction(event -> {
                    Evenement ev = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsEvent.fxml"));
                        Parent root = loader.load();

                        DetailsEvenement controller = loader.getController();
                        controller.setDetails(ev);

                        Stage stage = new Stage();
                        stage.setTitle("Détails de l'événement");
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



    private void ajouterBoutonParticiper() {
        colParticiper.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Je participe");

            {
                btn.setOnAction(event -> {
                    Evenement ev = getTableView().getItems().get(getIndex());

                    // Vérifie si des places sont encore disponibles
                    if (ev.getParticipantsMax() > 0) {
                        // Diminue de 1
                        ev.setParticipantsMax(ev.getParticipantsMax() - 1);

                        try {
                            // Met à jour dans la base de données
                            evenementService.update(ev); // Assure-toi que cette méthode existe dans EvenementService
                            chargerEvenements(); // Recharge les données
                            showNotification((Stage) ((Node) event.getSource()).getScene().getWindow(),
                                    "Participation confirmée !", "#4CAF50");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Affiche une alerte : plus de places
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Participation impossible");
                        alert.setHeaderText(null);
                        alert.setContentText("Le nombre maximal de participants est atteint !");
                        alert.showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    Evenement ev = getTableView().getItems().get(getIndex());

                    // Récupère les dates
                    LocalDate dateDebut = ev.getDateDebut().toLocalDate();
                    LocalDate dateFin = ev.getDateFin().toLocalDate();
                    LocalDate aujourdHui = LocalDate.now();

                    // Conditions pour désactiver le bouton
                    boolean evenementDejaCommence = dateDebut.isBefore(aujourdHui) && dateFin.isAfter(aujourdHui);
                    boolean evenementTermine = dateFin.isBefore(aujourdHui) || dateFin.isEqual(aujourdHui);
                    boolean plusDePlaces = ev.getParticipantsMax() <= 0;

                    // Désactiver si l’événement a déjà commencé, est terminé, ou plus de places
                    btn.setDisable(evenementDejaCommence || evenementTermine || plusDePlaces);

                    setGraphic(btn);
                } else {
                    setGraphic(null);
                }
            }

        });
    }


    @FXML
    void btnAjouterEvenement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvenement.fxml"));
            Parent root = loader.load();

            AjouterEvenement controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un événement");
            stage.setScene(new Scene(root));
            stage.show();

            stage.setOnHidden(e -> {
                chargerEvenements();
                if (controller.isEvenementAjoute()) {
                    showNotification((Stage) ((Node) event.getSource()).getScene().getWindow(),
                            "Événement ajouté avec succès !", "#4CAF50");
                } else {
                    showNotification((Stage) ((Node) event.getSource()).getScene().getWindow(),
                            "Erreur lors de l'ajout de l'événement.", "#F44336");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void resetTableView(ActionEvent event) {
        dateRecherchePicker.setValue(null); // Efface la date sélectionnée
        chargerEvenements(); // Recharge toutes les annonces
    }

    public void showNotification(Stage stage, String message, String color) {
        Popup popup = new Popup();
        Label label = new Label(message);

        label.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-padding: 10px; -fx-font-size: 14px; -fx-background-radius: 5;");
        popup.getContent().add(label);
        popup.setAutoHide(true);

        popup.show(stage);
        popup.setX(stage.getX() + stage.getWidth() - 300);
        popup.setY(stage.getY() + stage.getHeight() - 100);

        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> popup.hide());
        delay.play();
    }

    @FXML
    private void ouvrirCalendrier(ActionEvent event) {
        try {
            // Ferme la fenêtre actuelle
            Stage stageActuel = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageActuel.close();

            // Charge le FXML du calendrier des événements
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CalendrierEvent.fxml"));
            Parent root = loader.load();

            // Crée une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Calendrier des Événements");  // Titre de la fenêtre
            stage.setScene(new Scene(root)); // Applique le FXML à la nouvelle scène
            stage.show();  // Affiche la nouvelle fenêtre

        } catch (IOException e) {
            e.printStackTrace();  // En cas d'erreur, afficher les détails
        }
    }

}
