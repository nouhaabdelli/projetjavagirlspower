package gui;

import entities.avance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.avanceservice;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class listeavancecontroller implements Initializable {

    @FXML
    private TableView<avance> avanceTable;

    @FXML
    private TableColumn<avance, Integer> idAvanceColumn;
    @FXML
    private TableColumn<avance, BigDecimal> montantColumn;
    @FXML
    private TableColumn<avance, Integer> dureeColumn;
    @FXML
    private TableColumn<avance, LocalDate> dateAvanceColumn;
    @FXML
    private TableColumn<avance, String> niveauUrgenceColumn;
    @FXML
    private TableColumn<avance, String> etatColumn;
    @FXML
    private TableColumn<avance, Void> modifierColumn;
    @FXML
    private TableColumn<avance, Void> supprimerColumn;
    @FXML
    private TableColumn<avance, Void> afficherColumn;

    private ObservableList<avance> avanceList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idAvanceColumn.setCellValueFactory(new PropertyValueFactory<>("idAvance"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dureeColumn.setCellValueFactory(new PropertyValueFactory<>("duree"));
        dateAvanceColumn.setCellValueFactory(new PropertyValueFactory<>("dateAvance"));
        niveauUrgenceColumn.setCellValueFactory(new PropertyValueFactory<>("niveauUrgence"));
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));

        modifierColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifierButton = new Button("Modifier");
            {
                modifierButton.getStyleClass().add("modifier");
                modifierButton.setOnAction(event -> {
                    avance avance = getTableView().getItems().get(getIndex());
                    handleModifier(avance);
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                setGraphic(empty ? null : modifierButton);
            }
        });

        supprimerColumn.setCellFactory(param -> new TableCell<>() {
            private final Button supprimerButton = new Button("Supprimer");
            {
                supprimerButton.getStyleClass().add("supprimer");
                supprimerButton.setOnAction(event -> {
                    avance avance = getTableView().getItems().get(getIndex());
                    handleSupprimer(avance);
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                setGraphic(empty ? null : supprimerButton);
            }
        });

        afficherColumn.setCellFactory(param -> new TableCell<>() {
            private final Button afficherButton = new Button("Afficher");
            {
                afficherButton.getStyleClass().add("afficher");
                afficherButton.setOnAction(event -> {
                    avance avance = getTableView().getItems().get(getIndex());
                    handleAfficher(avance);
                });
            }
            @Override protected void updateItem(Void item, boolean empty) {
                setGraphic(empty ? null : afficherButton);
            }
        });

        avanceList = FXCollections.observableArrayList();
        avanceTable.setItems(avanceList);
        rafraichirTable();
    }

    public void rafraichirTable() {
        avanceservice avanceService = new avanceservice();
        try {
            avanceList.setAll(avanceService.readAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleModifier(avance avance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/modifieravance.fxml"));
            Parent root = loader.load();
            modifieravancecontroller controller = loader.getController();
            controller.setAvanceEnCours(avance);
            controller.setControllerPrincipal(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSupprimer(avance avance) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Êtes-vous sûr de supprimer cette avance ?");
        confirmation.setContentText("ID: " + avance.getIdAvance());
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                avanceservice avanceService = new avanceservice();
                avanceService.delete(avance);
                rafraichirTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleAfficher(avance avance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detailavance.fxml"));
            Parent root = loader.load();
            Detailavancecontroller controller = loader.getController();
            controller.setAvance(avance);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showReponsesRH() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reponsesrh.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}