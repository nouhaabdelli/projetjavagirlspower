
package gui;

import entities.Annonce;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.AnnonceService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Annonces {

    @FXML
    private Label GrandTitre;

    @FXML
    private TableView<Annonce> tableview;

    @FXML
    private TableColumn<Annonce, String> titre;

    @FXML
    private TableColumn<Annonce, String> contenu;

    @FXML
    private TableColumn<Annonce, String> datepub;

    @FXML
    private TableColumn<Annonce, String> pj;

    private final AnnonceService annonceService = new AnnonceService();

    @FXML
    public void initialize() {
        titre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        contenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        datepub.setCellValueFactory(new PropertyValueFactory<>("datePublication"));
        pj.setCellValueFactory(new PropertyValueFactory<>("pieceJointe"));
        chargerAnnonces();
    }

    private void chargerAnnonces() {
        try {
            AnnonceService annonceService = new AnnonceService();
            List<Annonce> annonces = annonceService.readAll();
            ObservableList<Annonce> observableList = FXCollections.observableList(annonces);
            tableview.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Tu peux afficher une alerte ici si tu veux
        }
    }

    @FXML
    void btnaffich(ActionEvent event) {
        chargerAnnonces();
    }

    @FXML
    void btnmodif(ActionEvent event) {
        Annonce selected = tableview.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Modifier : " + selected.getTitre());
            // Ouvre une interface pour modifier l'annonce sélectionnée
        }
    }


    @FXML
    void btnajout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAnnonce.fxml")); // ou ajuste le chemin
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm()); // ✅ Chemin du CSS ici

            Stage stage = new Stage();
            stage.setTitle("Ajouter une Annonce");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void btnsupp(ActionEvent event) {

    }
}