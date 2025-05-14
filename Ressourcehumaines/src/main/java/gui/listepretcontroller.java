package gui;

import entities.pret;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.pretservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class listepretcontroller {
    @FXML
    private AnchorPane mainContent;

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

    @FXML
    private ToggleButton themeToggle;

    private ObservableList<pret> pretList;

    private boolean isDarkTheme = false;

    private pretservice pretService = new pretservice();

    @FXML
    private void initialize() {
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        colDatePret.setCellValueFactory(new PropertyValueFactory<>("datePret"));
        colNiveauUrgence.setCellValueFactory(new PropertyValueFactory<>("niveauUrgence"));
        colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        colModifier.setCellFactory(col -> new TableCell<>() {
            private final Button modifierButton = new Button("Modifier");
            { modifierButton.setOnAction(event -> { pret pret = getTableView().getItems().get(getIndex()); modifierPret(pret); });
                modifierButton.setStyle("-fx-background-color: #1e3a8a; -fx-text-fill: white;"); }
            @Override protected void updateItem(Void item, boolean empty) { super.updateItem(item, empty); setGraphic(empty ? null : modifierButton); }
        });

        colSupprimer.setCellFactory(col -> new TableCell<>() {
            private final Button supprimerButton = new Button("Supprimer");
            { supprimerButton.setOnAction(event -> { pret pret = getTableView().getItems().get(getIndex()); supprimerPret(pret); });
                supprimerButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;"); }
            @Override protected void updateItem(Void item, boolean empty) { super.updateItem(item, empty); setGraphic(empty ? null : supprimerButton); }
        });

        colAfficher.setCellFactory(col -> new TableCell<>() {
            private final Button afficherButton = new Button("Afficher");
            { afficherButton.setOnAction(event -> { pret pret = getTableView().getItems().get(getIndex()); afficherDetails(pret); });
                afficherButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); }
            @Override protected void updateItem(Void item, boolean empty) { super.updateItem(item, empty); setGraphic(empty ? null : afficherButton); }
        });

        rafraichirTable();
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldValue, newValue) -> rechercherPret());
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
            if (getClass().getResource("/style/finance.css") != null) {
                stage.getScene().getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            }
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
            if (getClass().getResource("/style/finance.css") != null) {
                stage.getScene().getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            }
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors du chargement de detailpret.fxml : " + e.getMessage());
        }
    }

    @FXML
    private void rechercherPret() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            tableViewPret.setItems(pretList);
            return;
        }
        ObservableList<pret> filteredList = FXCollections.observableArrayList();
        for (pret pret : pretList) {
            if (String.valueOf(pret.getMontant()).toLowerCase().contains(searchText) ||
                    String.valueOf(pret.getDuree()).toLowerCase().contains(searchText) ||
                    pret.getDatePret().toString().toLowerCase().contains(searchText) ||
                    pret.getNiveauUrgence().toLowerCase().contains(searchText) ||
                    pret.getEtat().toLowerCase().contains(searchText)) {
                filteredList.add(pret);
            }
        }
        tableViewPret.setItems(filteredList);
    }

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
            System.out.println("Tentative de retour à pret.fxml depuis listepret.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pret.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            if (getClass().getResource("/style/finance.css") != null) {
                newStage.getScene().getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            }
            newStage.show();
            if (mainContent != null) {
                Stage currentStage = (Stage) mainContent.getScene().getWindow();
                if (currentStage != null) {
                    currentStage.close();
                }
            }
        } catch (IOException e) {
            showError("Erreur lors du chargement de pret.fxml dans listepretcontroller : " + e.getMessage());
        }
    }

    @FXML
    private void showReponsesRH() {
        try {
            System.out.println("Chargement de reponsespretrh.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/reponsespretrh.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Resource 'fxml/reponsespretrh.fxml' not found.");
            }
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            if (getClass().getResource("/style/finance.css") != null) {
                newStage.getScene().getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            }
            newStage.show();
            if (mainContent != null) {
                Stage currentStage = (Stage) mainContent.getScene().getWindow();
                if (currentStage != null) {
                    currentStage.close();
                }
            }
        } catch (IOException e) {
            showError("Erreur lors du chargement de reponsespretrh.fxml : " + e.getMessage());
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
}