package gui;

import entities.pret;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import services.pretservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class Frontlistepret implements ListePretParentController {

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
    private Button btnSearch;

    @FXML
    private Button btnSidebarToggle;

    @FXML
    private VBox sidebar;

    @FXML
    private AnchorPane contentArea;

    @FXML
    private AnchorPane dynamicContent;

    @FXML
    private TableView<pret> tableViewPret;

    @FXML
    private TableColumn<pret, BigDecimal> colMontant;

    @FXML
    private TableColumn<pret, Integer> colDuree;

    @FXML
    private TableColumn<pret, LocalDate> colDatePret;

    @FXML
    private TableColumn<pret, String> colNiveauUrgence;

    @FXML
    private TableColumn<pret, String> colEtat;

    @FXML
    private TableColumn<pret, Void> colModifier;

    @FXML
    private TableColumn<pret, Void> colSupprimer;

    @FXML
    private TableColumn<pret, Void> colAfficher;

    @FXML
    private TextField searchField;

    private ObservableList<pret> pretList;

    private final pretservice pretService = new pretservice();

    private PauseTransition autoCloseTimer;

    private boolean isSidebarOpen = false;
    private boolean isTransitioning = false;
    private boolean chatbotVisible = false;

    @FXML
    private void initialize() {
        setupTableColumns();
        setupActionButtons();
        setupSearchField();
        setupSidebarAutoClose();

        rafraichirTable(); // méthode de l'interface
    }

    private void setupTableColumns() {
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        colDatePret.setCellValueFactory(new PropertyValueFactory<>("datePret"));
        colNiveauUrgence.setCellValueFactory(new PropertyValueFactory<>("niveauUrgence"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
    }

    private void setupActionButtons() {
        colModifier.setCellFactory(col -> new TableCell<pret, Void>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setStyle("-fx-background-color: #1e3a8a; -fx-text-fill: white;");
                btn.setOnAction(e -> {
                    pret currentPret = getTableView().getItems().get(getIndex());
                    modifierPret(currentPret);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        colSupprimer.setCellFactory(col -> new TableCell<pret, Void>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
                btn.setOnAction(e -> {
                    pret currentPret = getTableView().getItems().get(getIndex());
                    supprimerPret(currentPret);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        colAfficher.setCellFactory(col -> new TableCell<pret, Void>() {
            private final Button btn = new Button("Afficher");
            {
                btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btn.setOnAction(e -> {
                    pret currentPret = getTableView().getItems().get(getIndex());
                    afficherDetails(currentPret);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void setupSearchField() {
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> rechercherPret(newVal));
        }
    }

    private void setupSidebarAutoClose() {
        autoCloseTimer = new PauseTransition(Duration.seconds(20));
        autoCloseTimer.setOnFinished(e -> {
            if (isSidebarOpen && !isTransitioning) {
                toggleSidebar();
                System.out.println("Sidebar auto-collapsed after 20 seconds");
            }
        });

        if (btnSidebarToggle != null) {
            btnSidebarToggle.setOnAction(e -> toggleSidebar());
        } else {
            System.err.println("btnSidebarToggle is null");
        }
    }

    private void modifierPret(pret pret) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifier.fxml"));
            Parent root = loader.load();
            modifierpret controller = loader.getController();
            controller.chargerPourModification(pret);
            controller.setControllerPrincipal(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            addStylesheet(stage);
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors du chargement de modifier.fxml : " + e.getMessage());
        }
    }

    private void supprimerPret(pret pret) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce prêt ?");
        alert.setContentText("Cette action est irréversible.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    pretService.delete(pret);
                    rafraichirTable();
                    showInfo("Prêt supprimé avec succès !");
                } catch (SQLException e) {
                    showError("Erreur lors de la suppression : " + e.getMessage());
                }
            }
        });
    }

    private void afficherDetails(pret pret) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detailpret.fxml"));
            Parent root = loader.load();
            Detailpret controller = loader.getController();
            controller.setPret(pret);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            addStylesheet(stage);
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors du chargement de detailpret.fxml : " + e.getMessage());
        }
    }

    @FXML
    private void rechercherPret(String searchText) {
        if (searchText == null) searchText = "";
        searchText = searchText.toLowerCase().trim();

        if (searchText.isEmpty()) {
            tableViewPret.setItems(pretList);
            return;
        }

        ObservableList<pret> filtered = FXCollections.observableArrayList();
        for (pret p : pretList) {
            if (matchesSearch(p, searchText)) {
                filtered.add(p);
            }
        }
        tableViewPret.setItems(filtered);
    }

    private boolean matchesSearch(pret p, String searchText) {
        return String.valueOf(p.getMontant()).toLowerCase().contains(searchText) ||
                String.valueOf(p.getDuree()).toLowerCase().contains(searchText) ||
                p.getDatePret().toString().toLowerCase().contains(searchText) ||
                p.getNiveauUrgence().toLowerCase().contains(searchText) ||
                p.getEtat().toLowerCase().contains(searchText);
    }

    @Override
    public void rafraichirTable() {
        try {
            pretList = FXCollections.observableArrayList(pretService.readAll());
            tableViewPret.setItems(pretList);
            tableViewPret.refresh();
        } catch (SQLException e) {
            showError("Erreur lors du chargement des prêts : " + e.getMessage());
        }
    }

    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pret.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            addStylesheet(newStage);
            newStage.show();

            if (mainContent != null) {
                Stage currentStage = (Stage) mainContent.getScene().getWindow();
                if (currentStage != null) currentStage.close();
            }
        } catch (IOException e) {
            showError("Erreur lors du chargement de pret.fxml : " + e.getMessage());
        }
    }

    private void addStylesheet(Stage stage) {
        if (getClass().getResource("/style/finance.css") != null) {
            stage.getScene().getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void toggleSidebar() {
        if (sidebar == null || contentArea == null || tableViewPret == null) {
            System.err.println("Impossible de basculer la sidebar: composants manquants");
            return;
        }
        if (isTransitioning) {
            System.out.println("Transition en cours, toggle ignoré");
            return;
        }
        isTransitioning = true;
        autoCloseTimer.stop();

        TranslateTransition sidebarTransition = new TranslateTransition(Duration.millis(300), sidebar);
        FadeTransition fade = new FadeTransition(Duration.millis(300), sidebar);

        if (isSidebarOpen) {
            sidebarTransition.setToX(-250);
            fade.setToValue(0.5);
            contentArea.setLayoutX(60.);
        }
    }
}