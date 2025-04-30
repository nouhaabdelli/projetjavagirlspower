//package gui;
//
//public class afficheretsupprimerpret {
//}
//package gui;
//
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.TableView;
//import javafx.scene.control.TextField;
//
//public class afficheretsupprimerpret {
//
//    @FXML
//    private TextField idasupprimer;
//
//    @FXML
//    private TableView<?> tablepret;
//
//    @FXML
//    void afficher(ActionEvent event) {
//        try {
//            List<Pret> list = pretService.readAll();
//            ObservableList<Pret> data = FXCollections.observableArrayList(list);
//            tablePrets.setItems(data);
//        } catch (Exception e) {
//            showAlert("Erreur", "Impossible de récupérer les prêts : " + e.getMessage(), Alert.AlertType.ERROR);
//
//    }

//    @FXML
//    void supprimer(ActionEvent event) {
//            try {
//                int id = Integer.parseInt(idSuppression.getText());
//                Pret p = new Pret();
//                p.setIdPret(id);
//                pretService.delete(p);
//                showAlert("Succès", "Prêt supprimé avec succès.", Alert.AlertType.INFORMATION);
//                afficherPrets(); // Rafraîchir la table
//            } catch (NumberFormatException e) {
//                showAlert("Erreur", "ID invalide", Alert.AlertType.ERROR);
//            } catch (Exception e) {
//                showAlert("Erreur", "Suppression échouée : " + e.getMessage(), Alert.AlertType.ERROR);
//            }
//        }
//
//    }
//    private void showAlert(String titre, String contenu, Alert.AlertType type) {
//        Alert alert = new Alert(type);
//        alert.setTitle(titre);
//        alert.setContentText(contenu);
//        alert.showAndWait();
//    }
//}
package gui;

import entities.Pret;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.PretService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class afficheretsupprimerpret {

    @FXML
    private TextField idasupprimer;

    @FXML
    private TableView<Pret> tablepret;

    @FXML
    private TableColumn<Pret, Integer> colId;
    @FXML
    private TableColumn<Pret, Double> colMontant;
    @FXML
    private TableColumn<Pret, Integer> colDuree;
    @FXML
    private TableColumn<Pret, String> colDate;
    @FXML
    private TableColumn<Pret, String> colUrgence;
    @FXML
    private TableColumn<Pret, String> colMotif;

    private PretService pretService;

    public afficheretsupprimerpret() {
        try {
            Connection cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourDB", "root", "");
            pretService = new PretService(cnx);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIdPret()).asObject());
        colMontant.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getMontant()).asObject());
        colDuree.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getDuree()).asObject());
        colDate.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDatePret()));
        colUrgence.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNiveauUrgence()));
        colMotif.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMotif()));
    }

    @FXML
    void afficher(ActionEvent event) {
        try {
            List<Pret> prets = pretService.readAll();
            ObservableList<Pret> data = FXCollections.observableArrayList(prets);
            tablepret.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        try {
            int id = Integer.parseInt(idasupprimer.getText());
            Pret p = new Pret();
            p.setIdPret(id);
            pretService.delete(p);
            afficher(null); // rafraîchir la table
        } catch (NumberFormatException e) {
            showAlert("Erreur", "ID invalide", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Échec de suppression", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
