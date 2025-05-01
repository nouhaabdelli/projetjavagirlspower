package gui;

import entities.Pret;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.PretService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class AfficherEtSupprimerPretController {

    @FXML private TableView<Pret> tablepret;
    @FXML private TableColumn<Pret, Integer> colId;
    @FXML private TableColumn<Pret, Double> colMontant;
    @FXML private TableColumn<Pret, Integer> colDuree;
    @FXML private TableColumn<Pret, String> colDate;
    @FXML private TableColumn<Pret, String> colUrgence;
    @FXML private TableColumn<Pret, String> colMotif;
    @FXML private TextField idasupprimer;

    private PretService pretService;

    @FXML
    public void initialize() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nom_de_ta_base", "utilisateur", "motdepasse");
            pretService = new PretService(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getIdPret()).asObject());
        colMontant.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getMontant()).asObject());
        colDuree.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getDuree()).asObject());
        colDate.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDatePret()));
        colUrgence.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNiveauUrgence()));
        colMotif.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMotif()));
    }

    @FXML
    void afficher() {
        try {
            List<Pret> prets = pretService.readAll();
            ObservableList<Pret> list = FXCollections.observableArrayList(prets);
            tablepret.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void supprimer() {
        try {
            int id = Integer.parseInt(idasupprimer.getText());
            Pret p = new Pret();
            p.setIdPret(id);
            pretService.delete(p);
            afficher(); // rafraîchir
        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID invalide");
        } catch (SQLException e) {
            showAlert("Erreur SQL", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
