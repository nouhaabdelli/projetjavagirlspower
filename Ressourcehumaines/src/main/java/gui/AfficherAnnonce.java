package gui;

import entities.Annonce;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.AnnonceService;

import java.sql.SQLException;
import java.util.List;

    public class AfficherAnnonce {

        private final AnnonceService annonceService = new AnnonceService();

        @FXML
        private TableColumn<Annonce, Integer> idCol;
        @FXML
        private TableColumn<Annonce, String> titreCol;
        @FXML
        private TableColumn<Annonce, String> contenuCol;
        @FXML
        private TableColumn<Annonce, String> datePublicationCol;
        @FXML
        private TableColumn<Annonce, String> pieceJointeCol;
        @FXML
        private TableView<Annonce> tableView;

        @FXML
        void initialize() {
            try {
                List<Annonce> annonces = annonceService.readAll();
                System.out.println(annonces);
                ObservableList<Annonce> observableList = FXCollections.observableList(annonces);
                tableView.setItems(observableList);

                idCol.setCellValueFactory(new PropertyValueFactory<>("idAnnonce"));
                titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
                contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
                datePublicationCol.setCellValueFactory(new PropertyValueFactory<>("datePublication"));
                pieceJointeCol.setCellValueFactory(new PropertyValueFactory<>("pieceJointe"));
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }