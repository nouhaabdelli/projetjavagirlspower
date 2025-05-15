package gui;

import entities.Reponses;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.ReponseService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class backoffice {

    @FXML
    private AnchorPane dynamicContent;
    @FXML
    private Button btnConsulter;
    @FXML
    private Button btnRepondre;
    @FXML
    private Button AjouterReclamations;

    @FXML
    private Button MesReclamations;
    @FXML
    private DatePicker datePickerSearch;


        @FXML
        private AnchorPane rootPane;
        @FXML
        private TableView<Reponses> tableViewReponses;
        @FXML
        private TableColumn<Reponses, String> colContenu;
        @FXML
        private TableColumn<Reponses, String> colDateReponse;
        @FXML
        private TableColumn<Reponses, String> colDateModification;
        @FXML
        private TableColumn<Reponses, String> colImportance;
        @FXML
        private TableColumn<Reponses, String> colFichierJoint;
        @FXML
        private TableColumn<Reponses, Void> colModifier;
        @FXML
        private TableColumn<Reponses, Void> colSupprimer;
        @FXML
        private TableColumn<Reponses, Void> colAfficher;
        @FXML
        private AjouterReponse controller;
        @FXML
        private AnchorPane contentArea;
        @FXML
        private Button btnApplyFilters;
        @FXML
        private Button btnClearFilters;
        @FXML
        private Button btnSidebarToggle;
        @FXML
        private Button btnSearch;
        @FXML
        private CheckBox filterHasAttachment;
        @FXML
        private CheckBox filterHighImportance;
    @FXML
    private CheckBox filterHighImportance1;
    @FXML
    private CheckBox filterHighImportance2;
        @FXML
        private VBox sidebar;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;


        @FXML
        private ToggleButton themeToggle;
    @FXML
    private HBox Recher;
    @FXML
    private HBox filtre;
        private final ReponseService serviceReponses = new ReponseService();
        private final ObservableList<Reponses> listeReponses = FXCollections.observableArrayList();
        private boolean isSidebarOpen = false;
    private Parent tableViewParent;

    private PauseTransition autoCloseTimer;
    private boolean isTransitioning = false;

    @FXML
    public void initialize() {


        tableViewParent = tableViewReponses.getParent();
        initCols();
        loadData();
        setupEventHandlers();
        rootPane.getStyleClass().remove("alt-theme");
        themeToggle.setText("🌙");
        // Initialize auto-close timer
        autoCloseTimer = new PauseTransition(Duration.seconds(20));
        autoCloseTimer.setOnFinished(event -> {
            if (isSidebarOpen && !isTransitioning) {
                toggleSidebar();
                System.out.println("Sidebar auto-collapsed after 3 seconds");
            }
        });
    }

    private void initCols() {
        colContenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        colDateReponse.setCellValueFactory(new PropertyValueFactory<>("dateReponse"));
        colDateModification.setCellValueFactory(new PropertyValueFactory<>("dateModification"));
        colImportance.setCellValueFactory(new PropertyValueFactory<>("priorite"));
        colFichierJoint.setCellValueFactory(cellData -> {
            String chemin = cellData.getValue().getFichierJoint();
            return new SimpleStringProperty(chemin != null ? new File(chemin).getName() : "Aucun");
        });

        addActionButtons();
    }

    private void loadData() {
        try {
            listeReponses.setAll(serviceReponses.readAll());
            tableViewReponses.setItems(listeReponses);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de base de données");
            alert.setHeaderText("Erreur lors du chargement des réponses");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    private void chargerToutesLesReponses() {
        try {
            ReponseService reponseService = new ReponseService();
            List<Reponses> list = reponseService.readAll();
            tableViewReponses.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            e.printStackTrace(); // pour le débogage
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger les réponses");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void addActionButtons() {
        colModifier.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("✏ Modifier");

            {
                btn.setOnAction(event -> {
                    Reponses reponse = getTableView().getItems().get(getIndex());

                    try {
                     // Charger le formulaire FXML
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutReponse.fxml"));
                        Parent root = loader.load();
                        AjouterReponse controller = loader.getController();
                        controller.activerModeModification(reponse);
//                        tableViewReponses.refresh(); // Force la mise à jour de la vue

                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setTitle("Modifier la Réponse");
                        stage.setScene(scene);
                        stage.setOnHiding(event1 -> {
                            chargerToutesLesReponses(); // Méthode pour recharger le TableView
                        });
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Échec du chargement du formulaire");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        colSupprimer.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("🗑 Supprimer");
            {
                btn.setOnAction(event -> {
                    Reponses reponse = getTableView().getItems().get(getIndex());
                    if (reponse == null) return;
                    boolean confirm = new Alert(Alert.AlertType.CONFIRMATION, "Confirmer la suppression ?", ButtonType.YES, ButtonType.NO)
                            .showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
                    if (confirm) {
                        try {
                            serviceReponses.delete(reponse);
                            loadData();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Erreur");
                            alert.setHeaderText("Échec de la suppression");
                            alert.setContentText(e.getMessage());
                            alert.showAndWait();
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

        colAfficher.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("🔍 Afficher");
            {
                btn.setOnAction(event -> {
                    Reponses reponse = getTableView().getItems().get(getIndex());
                    try {
                        // Charger le formulaire FXML
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detailreponse.fxml"));
                        Parent root = loader.load();
                        AfficherReponse controller = loader.getController();
                        controller.setReponse(reponse);

                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setTitle("Afficher la Réponse");
                        stage.setScene(scene);
                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur");
                        alert.setHeaderText("Échec du chargement du formulaire");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
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

    private void setupEventHandlers() {
        if (btnSidebarToggle != null) {
            btnSidebarToggle.setOnAction(event -> toggleSidebar());
        } else {
            System.err.println("btnSidebarToggle is null");
        }
        btnApplyFilters.setOnAction(event -> applyFilters());
        btnClearFilters.setOnAction(event -> clearFilters());
        btnSearch.setOnAction(event -> applySearchByDate());
        themeToggle.setOnAction(event -> toggleTheme());
        btnConsulter.setOnAction(e -> showReponsesTable());
        btnRepondre.setOnAction(e -> loadAjoutReponse());
        AjouterReclamations.setOnAction(e -> loadAjoutReclamation());
        MesReclamations.setOnAction(e -> MesReclamations());
    }
    private void toggleSidebar() {
        if (sidebar == null || contentArea == null || tableViewReponses == null) {
            System.err.println("Cannot toggle sidebar: sidebar=" + sidebar + ", contentArea=" + contentArea + ", tableView=" + tableViewReponses);
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
    private void applyFilters() {
        ObservableList<Reponses> filteredList = FXCollections.observableArrayList();
        for (Reponses reponse : listeReponses) {
            boolean matches = true;
            if (filterHighImportance.isSelected() && !reponse.getPriorite().equals("Urgent")) {
                matches = false;
            }
            if (filterHighImportance1.isSelected() && !reponse.getPriorite().equals("Normal")) {
                matches = false;
            }
            if (filterHighImportance2.isSelected() && !reponse.getPriorite().equals("Important")) {
                matches = false;
            }
            if (filterHasAttachment.isSelected() && (reponse.getFichierJoint() == null || reponse.getFichierJoint().isEmpty())) {
                matches = false;
            }
            if (matches) {
                filteredList.add(reponse);
            }
        }
        tableViewReponses.setItems(filteredList);
        toggleSidebar();
    }

    private void clearFilters() {
        filterHighImportance.setSelected(false);
        filterHasAttachment.setSelected(false);
        tableViewReponses.setItems(listeReponses);
        toggleSidebar();
    }


    @FXML
    private void applySearchByDate() {
        LocalDate start = startDatePicker.getValue();

        if (start == null) {
            // Remet toute la liste complète dans le tableau
            tableViewReponses.setItems(FXCollections.observableArrayList(listeReponses));
            return;
        }

        // Sinon, filtre sur la date
        ObservableList<Reponses> filteredReponses = FXCollections.observableArrayList();

        for (Reponses reponse : listeReponses) {
            LocalDate dateReponse = reponse.getDateReponse();
            LocalDate dateModification = reponse.getDateModification();

            if ((dateReponse != null && dateReponse.equals(start)) ||
                    (dateModification != null && dateModification.equals(start))) {
                filteredReponses.add(reponse);
            }
        }

        tableViewReponses.setItems(filteredReponses);
    }


    private void toggleTheme() {
        if (themeToggle.isSelected()) {
            rootPane.getStyleClass().add("alt-theme");
            themeToggle.setText("☀");
        } else {
            rootPane.getStyleClass().remove("alt-theme");
            themeToggle.setText("🌙");
        }
    }


    private void loadAjoutReponse() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/RepondreReclamations.fxml"));
            btnSearch.setVisible(false);
            filtre.setVisible(false);
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

    private void showReponsesTable() {
        btnSearch.setVisible(true);
        filtre.setVisible(true);
        FadeTransition fade = new FadeTransition(Duration.millis(200), dynamicContent);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> {
            reloadReponsesFromDatabase(); // ici

            dynamicContent.getChildren().setAll(tableViewReponses);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dynamicContent);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fade.play();
    }
    private void reloadReponsesFromDatabase () {
        try {
            ReponseService reponseService = new ReponseService();
            List<Reponses> updatedList = reponseService.readAll();
            ObservableList<Reponses> observableList = FXCollections.observableArrayList(updatedList);
            tableViewReponses.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de chargement");
            alert.setHeaderText("Impossible de charger les réponses");
            alert.setContentText("Une erreur est survenue lors de la connexion à la base de données.");
            alert.showAndWait();
        }
    }

    private void loadAjoutReclamation() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AjouterReclamation.fxml"));
            btnSearch.setVisible(false);
//            Recher.setVisible(false);
            filtre.setVisible(false);
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

    private void MesReclamations() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ListeReclamations.fxml"));
            btnSearch.setVisible(false);
            Recher.setVisible(false);
            filtre.setVisible(false);
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



}