package gui;

import entities.Annonce;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
        // Récupérer l'annonce sélectionnée dans la TableView
        Annonce selectedAnnonce = tableview.getSelectionModel().getSelectedItem();

        if (selectedAnnonce != null) {
            try {
                // Charger le fichier FXML pour afficher les détails de l'annonce
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsAnnonce.fxml")); // Assure-toi que le chemin est correct
                Parent root = loader.load();

                // Obtenir le contrôleur de la vue de détails
                DetailsAnnonce detailsAnnonceController = loader.getController();

                // Passer les détails de l'annonce au contrôleur de la vue
                detailsAnnonceController.setDetails(selectedAnnonce);

                // Créer une nouvelle scène pour afficher les détails de l'annonce
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

                Stage stage = new Stage();
                stage.setTitle("Détails de l'annonce");
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                // Afficher un message d'erreur si le chargement échoue
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur lors du chargement des détails");
                alert.setContentText("Une erreur est survenue lors de l'affichage des détails.");
                alert.showAndWait();
            }
        } else {
            // Si aucune annonce n'est sélectionnée, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucune annonce sélectionnée");
            alert.setContentText("Veuillez sélectionner une annonce pour afficher ses détails.");
            alert.showAndWait();
        }
    }

    @FXML
    void btnmodif(ActionEvent event) {
        Annonce selected = tableview.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Charger l'interface de modification
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAnnonce.fxml"));
                Parent root = loader.load();

                // Passer l'annonce sélectionnée au contrôleur
                ModifierAnnonce modifierAnnonceController = loader.getController();
                modifierAnnonceController.initialize(selected);

                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

                Stage stage = new Stage();
                stage.setTitle("Modifier l'Annonce");
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Si aucune annonce n'est sélectionnée, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucune annonce sélectionnée");
            alert.setContentText("Veuillez sélectionner une annonce à modifier.");
            alert.showAndWait();
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
        // Vérifie si une annonce est sélectionnée
        Annonce selectedAnnonce = tableview.getSelectionModel().getSelectedItem();

        if (selectedAnnonce == null) {
            // Si aucune annonce n'est sélectionnée, afficher un message d'avertissement
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucune annonce sélectionnée");
            alert.setContentText("Veuillez sélectionner une annonce à supprimer.");
            alert.showAndWait();
            return;
        }

        // Crée une boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette annonce ?");
        alert.setContentText("L'action est irréversible.");

        // Si l'utilisateur clique sur "OK"
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                // Effectuer la suppression de l'annonce sélectionnée
                annonceService.delete(selectedAnnonce);

                // Mettre à jour la table après la suppression
                chargerAnnonces();

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Suppression réussie");
                successAlert.setHeaderText(null);
                successAlert.setContentText("L'annonce a été supprimée avec succès.");
                successAlert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                // En cas d'erreur lors de la suppression
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText("Erreur lors de la suppression");
                errorAlert.setContentText("Une erreur est survenue lors de la suppression de l'annonce.");
                errorAlert.showAndWait();
            }
        }
    }
}