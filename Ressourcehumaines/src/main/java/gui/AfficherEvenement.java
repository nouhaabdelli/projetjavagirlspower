package gui;



import entities.Evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.EvenementService;

import java.sql.SQLException;
import java.util.List;

    public class AfficherEvenement {

        private final EvenementService evenementService = new EvenementService();

        @FXML
        private TableColumn<Evenement, Integer> idCol;
        @FXML
        private TableColumn<Evenement, String> nomEvenementCol;
        @FXML
        private TableColumn<Evenement, String> descriptionCol;
        @FXML
        private TableColumn<Evenement, String> dateDebutCol;
        @FXML
        private TableColumn<Evenement, String> dateFinCol;
        @FXML
        private TableColumn<Evenement, String> lieuCol;
        @FXML
        private TableColumn<Evenement, String> organisateurCol;
        @FXML
        private TableColumn<Evenement, Integer> participantsMaxCol;
        @FXML
        private TableColumn<Evenement, String> statutCol;
        @FXML
        private TableColumn<Evenement, String> photoCol;
        @FXML
        private TableView<Evenement> tableView;

        @FXML
        void initialize() {
            try {
                List<Evenement> evenements = evenementService.readAll();
                System.out.println(evenements);
                ObservableList<Evenement> observableList = FXCollections.observableList(evenements);
                tableView.setItems(observableList);

                idCol.setCellValueFactory(new PropertyValueFactory<>("idEvenement"));
                nomEvenementCol.setCellValueFactory(new PropertyValueFactory<>("nomEvenement"));
                descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
                dateDebutCol.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
                dateFinCol.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
                lieuCol.setCellValueFactory(new PropertyValueFactory<>("lieu"));
                organisateurCol.setCellValueFactory(new PropertyValueFactory<>("organisateur"));
                participantsMaxCol.setCellValueFactory(new PropertyValueFactory<>("participantsMax"));
                statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
                photoCol.setCellValueFactory(new PropertyValueFactory<>("photo"));
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }


