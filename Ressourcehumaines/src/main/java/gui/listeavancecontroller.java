package gui;

import entities.avance;
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
import services.avanceservice;
import java.io.IOException;
import java.sql.SQLException;

public class listeavancecontroller {
    @FXML
    private AnchorPane mainContent;

    @FXML
    private AnchorPane contentArea;

    @FXML
    private AnchorPane dynamicContent;

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

    @FXML
    private void initialize() {
        // Initialiser les colonnes du TableView
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
}