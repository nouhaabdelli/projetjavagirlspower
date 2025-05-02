package gui;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import javafx.scene.Parent;

import entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.*;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.Scene;

public class ListeReclamations {

    @FXML
    private TableColumn<Reclamations, String> colDescription;

    @FXML
    private TableColumn<Reclamations, String> colNotifications;

    @FXML
    private TableColumn<Reclamations, String> colPieceJointe;

    @FXML
    private TableColumn<Reclamations, String> colPriorite;

    @FXML
    private TableColumn<Reclamations, String> colTitre;

    @FXML
    private TableColumn<?, ?> date_creation;


    @FXML
    private TableView<Reclamations> tableViewReclamations;

    private ReclamationService reclamationService;
    private UserService userService;


    public ListeReclamations() {
        this.reclamationService = new ReclamationService();
    }

    @FXML
    public void initialize() {
        try {
            List<Reclamations> reclamationsList = reclamationService.readAll();  // Appel du service pour récupérer les réclamations
            ObservableList<Reclamations> observableList = FXCollections.observableList(reclamationsList);
            tableViewReclamations.setItems(observableList);

            colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colPriorite.setCellValueFactory(new PropertyValueFactory<>("priorite"));
            colNotifications.setCellValueFactory(new PropertyValueFactory<>("recevoirNotifications"));
            colPieceJointe.setCellValueFactory(new PropertyValueFactory<>("cheminPieceJointe"));
            date_creation.setCellValueFactory(new PropertyValueFactory<>("dateDemande")) ;
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Impossible de récupérer les réclamations : " + e.getMessage());
            alert.showAndWait();
        }
    }
    public void rafraichirTable() {
        try {
            List<Reclamations> reclamationsList = reclamationService.readAll();
            ObservableList<Reclamations> observableList = FXCollections.observableList(reclamationsList);
            tableViewReclamations.setItems(observableList);
            tableViewReclamations.refresh(); // Force la mise à jour
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors du rafraîchissement des données : " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    void afficher(ActionEvent event) {
        Reclamations selected = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une réclamation à afficher.");
            alert.showAndWait();
            return;
        }

        try {
            // Récupérer l'utilisateur lié à cette réclamation
            UserService userService = new UserService(); // Ajoute cette ligne avant de l’utiliser
            User user = userService.getUserById(selected.getUserId());

            // Charger l'interface de détails
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detailreclamation.fxml"));
            Parent root = loader.load();

            // Passer les données au contrôleur
            Afficherdetail controller = loader.getController();
            controller.setReclamation(selected, user);

            // Crée une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Détails de la Réclamation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Optionnel : bloque la fenêtre précédente jusqu'à fermeture
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'affichage : " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        Reclamations selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();

        if (selectedReclamation == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setContentText("Veuillez sélectionner une réclamation à modifier.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjouterReclamation.fxml"));
            Parent root = loader.load();

            // Récupère le contrôleur et passe les données
            AjouterReclamations controller = loader.getController();
            controller.chargerDonneesPourModification(selectedReclamation);

            // Passe la référence du contrôleur principal à la fenêtre de modification
            controller.setControllerPrincipal(this);

            Stage stage = new Stage();
            stage.setTitle("Modification de la Réclamation");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void supprimer(ActionEvent event) {

        Reclamations selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();

        if (selectedReclamation != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette réclamation ?");
            alert.setContentText("Cette action est irréversible.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        ReclamationService service = new ReclamationService(); // ou statique selon ton code
                        service.delete(selectedReclamation);
                        tableViewReclamations.getItems().remove(selectedReclamation);
                    } catch (SQLException e) {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Erreur de suppression");
                        errorAlert.setHeaderText("Échec de la suppression");
                        errorAlert.setContentText("Une erreur est survenue : " + e.getMessage());
                        errorAlert.showAndWait();
                    }
                }
            });
        } else {
            Alert noSelection = new Alert(Alert.AlertType.WARNING);
            noSelection.setTitle("Aucune sélection");
            noSelection.setHeaderText("Aucune réclamation sélectionnée");
            noSelection.setContentText("Veuillez sélectionner une réclamation à supprimer.");
            noSelection.showAndWait();
        }
    }
}

