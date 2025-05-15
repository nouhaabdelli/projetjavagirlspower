package gui;

import entities.avance;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.avanceservice;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class listeavancecontroller {

    @FXML
    private AnchorPane chatbotPane;

    @FXML
    private Button btnChatbot;
    @FXML
    private AnchorPane mainContent;
    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnConsulter;

    @FXML
    private Button btnReponse;
    @FXML
    private Button btnstatistiques;

    @FXML
    private Button btnSearch;

    @FXML
    private AnchorPane contentArea;
    @FXML
    private Button btnSidebarToggle;
    @FXML
    private AnchorPane dynamicContent;
    @FXML
    private VBox sidebar;
    @FXML
    private TableView<avance> avanceTable;

    @FXML
    private TableColumn<avance, String> montantColumn;

    @FXML
    private TableColumn<avance, String> dureeColumn;

    @FXML
    private TableColumn<avance, String> dateColumn;

    @FXML
    private TableColumn<avance, String> niveauUrgenceColumn;

    @FXML
    private TableColumn<avance, String> etatColumn;

    @FXML
    private TableColumn<avance, Void> colModifier;

    @FXML
    private TableColumn<avance, Void> colSupprimer;

    @FXML
    private TableColumn<avance, Void> colAfficher;

    @FXML
    private TextField searchField;

    @FXML
    private ToggleButton themeToggle;

    private ObservableList<avance> avanceList;

    private boolean isDarkTheme = false;

    private PauseTransition autoCloseTimer;
    private boolean isTransitioning = false;
    private int currentUserId; // Ajout du champ pour stocker l'ID de l'utilisateur
    private boolean isSidebarOpen = false;
    private boolean chatbotVisible = false;
    private boolean isAdmin = true;

    public void setUserRole(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @FXML
    private void initialize() {
// Initialisation de autoCloseTimer
        if (btnReponse != null && !isAdmin) {
            btnReponse.setVisible(false);
            btnReponse.setManaged(false); // Pour ne pas laisser d’espace vide
        }
        autoCloseTimer = new PauseTransition(Duration.seconds(20));
        autoCloseTimer.setOnFinished(event -> {
            if (isSidebarOpen && !isTransitioning) {
                toggleSidebar();
                System.out.println("Sidebar auto-collapsed after 3 seconds");
            }
        });
        setupEventHandlers(); // s'assurer que tout est prêt        // Initialiser les colonnes du TableView
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dureeColumn.setCellValueFactory(new PropertyValueFactory<>("duree"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAvance"));
        niveauUrgenceColumn.setCellValueFactory(new PropertyValueFactory<>("niveauUrgence"));
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Ajouter des boutons dans les colonnes Modifier, Supprimer et Afficher
        colModifier.setCellFactory(col -> new TableCell<>() {
            private final Button modifierButton = new Button("Modifier");

            {
                modifierButton.setOnAction(event -> {
                    avance avance = getTableView().getItems().get(getIndex());
                    modifierAvance(avance);
                });
                modifierButton.setStyle("-fx-background-color: #1e3a8a; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : modifierButton);
            }
        });

        colSupprimer.setCellFactory(col -> new TableCell<>() {
            private final Button supprimerButton = new Button("Supprimer");

            {
                supprimerButton.setOnAction(event -> {
                    avance avance = getTableView().getItems().get(getIndex());
                    supprimerAvance(avance);
                });
                supprimerButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : supprimerButton);
            }
        });

        colAfficher.setCellFactory(col -> new TableCell<>() {
            private final Button afficherButton = new Button("Afficher");

            {
                afficherButton.setOnAction(event -> {
                    avance avance = getTableView().getItems().get(getIndex());
                    afficherDetails(avance);
                });
                afficherButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : afficherButton);
            }
        });

        // Charger les données initiales
        rafraichirTable();

        // Ajouter un écouteur pour la recherche
        searchField.textProperty().addListener((obs, oldValue, newValue) -> rechercherAvance());
    }

    @FXML
    private void ajouterAvance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ajouteravance.fxml"));
            Parent root = loader.load();
            ajouteravancecontroller controller = loader.getController();
            controller.setCurrentUserId(currentUserId); // Passer l'ID de l'utilisateur courant
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
                System.out.println("finance.css chargé avec succès");
            } else {
                System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources.");
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de ajouteravance.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void modifierAvance(avance avance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifieravance.fxml"));
            Parent root = loader.load();
            modifieravancecontroller controller = loader.getController();
            controller.setAvanceEnCours(avance);
            controller.setControllerPrincipal(this);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
                System.out.println("finance.css chargé avec succès");
            } else {
                System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources.");
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de modifieravance.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void supprimerAvance(avance avance) {
        try {
            avanceservice avanceService = new avanceservice();
            avanceService.delete(avance);
            rafraichirTable();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Avance supprimée avec succès !");
            alert.showAndWait();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'avance : " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void afficherDetails(avance avance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Detailavancecontroller.fxml"));
            Parent root = loader.load();
            Detailavancecontroller controller = loader.getController();
            controller.setAvance(avance);

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
                System.out.println("finance.css chargé avec succès");
            } else {
                System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources.");
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de Detailavancecontroller.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void rechercherAvance() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            avanceTable.setItems(avanceList);
            return;
        }

        ObservableList<avance> filteredList = FXCollections.observableArrayList();
        for (avance avance : avanceList) {
            if (String.valueOf(avance.getMontant()).toLowerCase().contains(searchText) ||
                    String.valueOf(avance.getDuree()).toLowerCase().contains(searchText) ||
                    avance.getDateAvance().toString().toLowerCase().contains(searchText) ||
                    avance.getNiveauUrgence().toLowerCase().contains(searchText) ||
                    avance.getEtat().toLowerCase().contains(searchText)) {
                filteredList.add(avance);
            }
        }
        avanceTable.setItems(filteredList);
    }

    public void rafraichirTable() {
        try {
            avanceservice avanceService = new avanceservice();
            avanceList = FXCollections.observableArrayList(avanceService.readAll());
            avanceTable.setItems(avanceList);
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des avances : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetour() {
        try {
            System.out.println("Tentative de retour à avance.fxml depuis listeavance.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/avance.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
                System.out.println("finance.css chargé avec succès");
            } else {
                System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources.");
            }
            newStage.setScene(scene);
            newStage.show();
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            if (currentStage != null) {
                currentStage.close();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de avance.fxml dans listeavancecontroller : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        if (isDarkTheme) {
            contentArea.getStyleClass().add("dark-theme");
            themeToggle.setText("☀️");
        } else {
            contentArea.getStyleClass().remove("dark-theme");
            themeToggle.setText("🌙");
        }
    }

    // Méthode pour définir l'ID de l'utilisateur courant
    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }
    private void setupEventHandlers() {
        if (btnSidebarToggle != null) {
            btnSidebarToggle.setOnAction(event -> toggleSidebar());
        } else {
            System.err.println("btnSidebarToggle is null");
        }

        btnConsulter.setOnAction(e -> showReponsesTable());
        btnAjouter.setOnAction(e -> loadAjoutReponse());
        btnReponse.setOnAction(e -> loadReponse());
        btnstatistiques.setOnAction(e -> loadstatistiques());


    }
    private void toggleSidebar() {
        if (sidebar == null || contentArea == null || avanceTable == null) {
            System.err.println("Cannot toggle sidebar: sidebar=" + sidebar + ", contentArea=" + contentArea + ", tableView=" + avanceTable);
            return;
        }
        if (isTransitioning) {
            System.out.println("Toggle ignored: Transition in progress");
            return;
        }
        isTransitioning = true;
        autoCloseTimer.stop();
        TranslateTransition sidebarTransition = new TranslateTransition(Duration.millis(300), sidebar);
        FadeTransition fade = new FadeTransition(Duration.millis(300), sidebar);

        if (isSidebarOpen) {
            // Collapse
            sidebarTransition.setToX(-250);
            fade.setToValue(0.5);
//            sidebar.setPrefWidth(50);
            contentArea.setLayoutX(60.0);
            contentArea.setPrefWidth(1200.0);
            isSidebarOpen = false;
        } else {
            // Open
            sidebarTransition.setToX(0);
            fade.setToValue(1.0);
            sidebar.setPrefWidth(300);
            contentArea.setLayoutX(310.0);
            contentArea.setPrefWidth(1070.0);
            isSidebarOpen = true;
            autoCloseTimer.playFromStart();

        }
        sidebarTransition.setOnFinished(e -> isTransitioning = false);
        sidebarTransition.play();
        fade.play();
    }




    private void showReponsesTable() {

        FadeTransition fade = new FadeTransition(Duration.millis(200), dynamicContent);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> {
            reloadReponsesFromDatabase(); // ici

            dynamicContent.getChildren().setAll(avanceTable);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dynamicContent);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fade.play();
    }
    private void reloadReponsesFromDatabase () {
        try {
            avanceservice reponseService = new avanceservice();
            List<avance> updatedList = reponseService.readAll();
            ObservableList<avance> observableList = FXCollections.observableArrayList(updatedList);
            avanceTable.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de chargement");
            alert.setHeaderText("Impossible de charger les réponses");
            alert.setContentText("Une erreur est survenue lors de la connexion à la base de données.");
            alert.showAndWait();
        }
    }


    private void loadReponse() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/reponsesrh.fxml"));
            searchField.setVisible(false);
            btnSearch.setVisible(false);
//            Recher.setVisible(false);
            FadeTransition fade = new FadeTransition(Duration.millis(300), dynamicContent);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> {
                dynamicContent.getChildren().setAll(root);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dynamicContent);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fade.play();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Échec du chargement du formulaire");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    private void loadstatistiques() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/stats.fxml"));
            searchField.setVisible(false);
            btnSearch.setVisible(false);
//            Recher.setVisible(false);
            FadeTransition fade = new FadeTransition(Duration.millis(300), dynamicContent);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> {
                dynamicContent.getChildren().setAll(root);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dynamicContent);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fade.play();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Échec du chargement du formulaire");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }



    private void loadAjoutReponse() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ajouteravance.fxml"));
            searchField.setVisible(false);
            btnSearch.setVisible(false);
//            Recher.setVisible(false);
            FadeTransition fade = new FadeTransition(Duration.millis(300), dynamicContent);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> {
                dynamicContent.getChildren().setAll(root);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dynamicContent);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fade.play();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Échec du chargement du formulaire");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void toggleChatbot(ActionEvent event) {
        if (!chatbotVisible) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/finance.fxml"));
                Parent chatbotContent = loader.load(); // ✅ Plus générique
                chatbotPane.getChildren().setAll(chatbotContent);
                chatbotPane.setVisible(true);
                chatbotVisible = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            chatbotPane.setVisible(false);
            chatbotPane.getChildren().clear();
            chatbotVisible = false;
        }
    }






}