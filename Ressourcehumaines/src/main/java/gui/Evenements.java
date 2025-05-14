package gui;

import entities.Evenement;
import entities.user;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDate;

import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import services.EvenementService;
import services.mailService;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Evenements {

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
    private TableColumn<Evenement, Void> colModifier;
    @FXML
    private TableColumn<Evenement, Void> colSupprimer;
    @FXML
    private TableColumn<Evenement, Void> colParticiper;


    @FXML
    private Button btnAjouter;

    @FXML
    private Button Réinitialiser;

    @FXML
    private Button ouvrirCalend;

    @FXML
    private Button btnSidebarToggle;

    @FXML
    private VBox sidebar;

    @FXML
    private DatePicker dateRecherchePicker;

    private boolean isSidebarOpen = true;
    private boolean isTransitioning = false;
    private PauseTransition autoCloseTimer;


    private final Set<Integer> evenementsParticipe = new HashSet<>();

    private final EvenementService evenementService = new EvenementService();

    @FXML
    public void initialize() {
        nomEvenement.setCellValueFactory(new PropertyValueFactory<>("nomEvenement"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        lieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        organisateur.setCellValueFactory(new PropertyValueFactory<>("organisateur"));
        participantsMax.setCellValueFactory(new PropertyValueFactory<>("participantsMax"));
        statut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        photo.setCellValueFactory(new PropertyValueFactory<>("photo"));

        ajouterBoutonAfficher();
        ajouterBoutonModifier();
        ajouterBoutonSupprimer();
        ajouterBoutonParticiper();


        chargerEvenements();

        if (btnSidebarToggle != null) {
            btnSidebarToggle.setOnAction(event -> toggleSidebar());
        }

        setupEventHandlers();
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
    private void toggleSidebar() {
        if (sidebar == null) return;

        TranslateTransition transition = new TranslateTransition(Duration.millis(300), sidebar);
        if (isSidebarOpen) {
            transition.setToX(-250);
            isSidebarOpen = false;
        } else {
            transition.setToX(0);
            isSidebarOpen = true;
        }
        transition.play();
    }

    private void setupEventHandlers() {
        if (btnSidebarToggle != null) {
            btnSidebarToggle.setOnAction(event -> toggleSidebar());
        } else {
            System.err.println("btnSidebarToggle is null");
        }

        // === Nouveau comportement de la sidebar ===
        if (btnAjouter != null)
            btnAjouter.setOnAction(e -> btnAjouterEvenement(e));
        if (Réinitialiser != null)
            Réinitialiser.setOnAction(e -> resetTableView(e));
        if (ouvrirCalend != null)
            ouvrirCalend.setOnAction(e -> ouvrirCalendrier(e) );
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

    private void ajouterBoutonModifier() {
        colModifier.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");

            {
                btn.setOnAction(event -> {
                    Evenement ev = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEvenement.fxml"));
                        Parent root = loader.load();

                        ModifierEvenement controller = loader.getController();
                        controller.initialize(ev);

                        Stage stage = new Stage();
                        stage.setTitle("Modifier l'événement");
                        stage.setScene(new Scene(root));
                        stage.show();

                        stage.setOnHidden(e -> chargerEvenements());
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
                    Evenement ev = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Voulez-vous vraiment supprimer cet événement ?");
                    alert.setContentText("Cette action est irréversible.");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            evenementService.delete(ev);
                            chargerEvenements();
                            showNotification((Stage) ((Node) event.getSource()).getScene().getWindow(),
                                    "Événement supprimé avec succès !", "#4CAF50");
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

    private void ajouterBoutonParticiper() {
        colParticiper.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Je participe");

            {
                btn.setOnAction(event -> {
                    Evenement ev = getTableView().getItems().get(getIndex());

                    if (evenementsParticipe.contains(ev.getIdEvenement())) {
                        // Déjà participé
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Déjà inscrit");
                        alert.setHeaderText(null);
                        alert.setContentText("Vous avez déjà participé à cet événement.");
                        alert.showAndWait();
                        return;
                    }

                    if (ev.getParticipantsMax() > 0) {
                        ev.setParticipantsMax(ev.getParticipantsMax() - 1);

                        try {
                            evenementService.update(ev);
                            chargerEvenements();

                            // Marquer l’événement comme "rejoint"
                            evenementsParticipe.add(ev.getIdEvenement());

                            evenementsParticipe.add(ev.getIdEvenement());

                            user currentUser = new user();
                            currentUser.setPrenom("Maram");
                            currentUser.setEmail("ghribimaram24@gmail.com");


                            // 🔔 ENVOI DU MAIL APRÈS VALIDATION
                            sendParticipationConfirmationEmail(currentUser, ev);
                            showNotification((Stage) ((Node) event.getSource()).getScene().getWindow(),
                                    "Participation confirmée !", "#4CAF50");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Participation impossible");
                        alert.setHeaderText(null);
                        alert.setContentText("Le nombre maximal de participants est atteint !");
                        alert.showAndWait();
                    }
                });
            }

            private void sendParticipationConfirmationEmail(user user, Evenement event) {
                mailService emailService = new mailService();
                String subject = "Confirmation de participation à l'événement";
                String body = "Bonjour " + user.getPrenom() + ",\n\n"
                        + "Vous avez confirmé votre participation à l'événement : '" + event.getNomEvenement() + "'.\n"
                        + "Date : " + event.getDateDebut() + "\n"
                        + "Lieu : " + event.getLieu() + "\n\n"
                        + "Merci et à bientôt !\n"
                        + "L'équipe d'organisation.";

                emailService.sendEmail(user.getEmail(), subject, body);
            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    Evenement ev = getTableView().getItems().get(getIndex());

                    LocalDate dateDebut = ev.getDateDebut().toLocalDate();
                    LocalDate dateFin = ev.getDateFin().toLocalDate();
                    LocalDate aujourdHui = LocalDate.now();

                    boolean evenementDejaCommence = dateDebut.isBefore(aujourdHui) && dateFin.isAfter(aujourdHui);
                    boolean evenementTermine = dateFin.isBefore(aujourdHui) || dateFin.isEqual(aujourdHui);
                    boolean plusDePlaces = ev.getParticipantsMax() <= 0;
                    boolean dejaParticipe = evenementsParticipe.contains(ev.getIdEvenement());

                    btn.setDisable(evenementDejaCommence || evenementTermine || plusDePlaces || dejaParticipe);
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
