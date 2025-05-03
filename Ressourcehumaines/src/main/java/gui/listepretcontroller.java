package gui;
import entities.pret;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.pretservice;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class listepretcontroller {

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

    private final pretservice pretService = new pretservice();

    @FXML
    public void initialize() {
        try {
            rafraichirTable();
            colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
            colDuree.setCellValueFactory(new PropertyValueFactory<>("duree"));
            colDatePret.setCellValueFactory(new PropertyValueFactory<>("datePret"));
            colNiveauUrgence.setCellValueFactory(new PropertyValueFactory<>("niveauUrgence"));
            colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        } catch (Exception e) {
            showError("Erreur d'initialisation : " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void rafraichirTable() throws SQLException {
        List<pret> liste = pretService.readAll();
        ObservableList<pret> data = FXCollections.observableArrayList(liste);
        tableViewPret.setItems(data);
        tableViewPret.refresh();
    }

    @FXML
    void afficher(ActionEvent event) {
        pret selected = tableViewPret.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Veuillez sélectionner un prêt à afficher.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detailpret.fxml"));
            Parent root = loader.load();

            Detailpret controller = loader.getController();
            controller.setPret(selected);

            Stage stage = new Stage();
            stage.setTitle("Détails du prêt");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            showError("Erreur d'affichage : " + e.getMessage());
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        pret selected = tableViewPret.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Veuillez sélectionner un prêt à modifier.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ajouterpret.fxml"));
            Parent root = loader.load();

            ajouterpretcontroller controller = loader.getController();
            controller.chargerPourModification(selected);
            controller.setControllerPrincipal(this);

            Stage stage = new Stage();
            stage.setTitle("Modifier un prêt");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors du chargement de l'interface : " + e.getMessage());
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        pret selected = tableViewPret.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Veuillez sélectionner un prêt à supprimer.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce prêt ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    pretService.delete(selected);
                    tableViewPret.getItems().remove(selected);
                } catch (SQLException e) {
                    showError("Erreur lors de la suppression : " + e.getMessage());
                }
            }
        });
    }

    @FXML
    void ajouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ajouterpret.fxml"));
            Parent root = loader.load();

            ajouterpretcontroller controller = loader.getController();
            controller.setControllerPrincipal(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un prêt");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setContentText(message);
        alert.showAndWait();
    }
}