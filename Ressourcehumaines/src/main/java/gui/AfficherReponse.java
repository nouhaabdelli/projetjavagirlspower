package gui;
import entities.Reponses;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ReponseService;

import java.sql.SQLException;
import java.util.List;public class AfficherReponse {
    private final ReponseService reponsesService = new ReponseService();

    @FXML
    private TableColumn<Reponses, Integer> idCol;

    @FXML
    private TableColumn<Reponses, String> titreCol;

    @FXML
    private TableColumn<Reponses, String> contenuCol;

    @FXML
    private TableColumn<Reponses, String> dateCol;

    @FXML
    private TableColumn<Reponses, String> importanceCol;

    @FXML
    private TableColumn<Reponses, String> fichierCol;

    @FXML
    private TableView<Reponses> tableViewReponses;

    @FXML
    void initialize() {
        try {
            List<Reponses> reponsesList = reponsesService.readAll();
            ObservableList<Reponses> observableList = FXCollections.observableList(reponsesList);
            tableViewReponses.setItems(observableList);

            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
            contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
            dateCol.setCellValueFactory(new PropertyValueFactory<>("dateReponse"));
            importanceCol.setCellValueFactory(new PropertyValueFactory<>("importance"));
            fichierCol.setCellValueFactory(new PropertyValueFactory<>("fichierJoint"));

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
