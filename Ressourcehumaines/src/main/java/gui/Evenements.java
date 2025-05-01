package gui;

import entities.Evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.EvenementService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Evenements {

    @FXML
    private TableView<Evenement> tableview;

    @FXML
    private TableColumn<Evenement, String> nomEvenement;
    @FXML
    private TableColumn<Evenement, String> description;
    @FXML
    private TableColumn<Evenement, String> dateDebut;
    @FXML
    private TableColumn<Evenement, String> dateFin;
    @FXML
    private TableColumn<Evenement, String> lieu;
    @FXML
    private TableColumn<Evenement, String> organisateur;
    @FXML
    private TableColumn<Evenement, Integer> participantsMax;
    @FXML
    private TableColumn<Evenement, String> statut;
    @FXML
    private TableColumn<Evenement, String> photo;

    @FXML
    public void initialize() {
        // Ne pas lier de colonne à l'ID
        nomEvenement.setCellValueFactory(new PropertyValueFactory<>("nomEvenement"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateDebut.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        dateFin.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        lieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        organisateur.setCellValueFactory(new PropertyValueFactory<>("organisateur"));
        participantsMax.setCellValueFactory(new PropertyValueFactory<>("participantsMax"));
        statut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        photo.setCellValueFactory(new PropertyValueFactory<>("photo"));

        chargerEvenements();
    }

    private void chargerEvenements() {
        try {
            EvenementService evenementService = new EvenementService();
            List<Evenement> evenements = evenementService.readAll();
            ObservableList<Evenement> observableList = FXCollections.observableList(evenements);
            tableview.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   @FXML
    void btnAfficherEvenement(ActionEvent event) {
        // Récupérer l'événement sélectionné dans la TableView
       Evenement selectedEvenement = tableview.getSelectionModel().getSelectedItem();

       if (selectedEvenement != null) {
           try {
               // Charger le fichier FXML pour afficher les détails de l'événement
               FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsEvent.fxml")); // Chemin à adapter si nécessaire
               Parent root = loader.load();

               // Obtenir le contrôleur de la vue de détails
               DetailsEvenement detailsEvenementController = loader.getController();

               // Passer les détails de l'événement au contrôleur
               detailsEvenementController.setDetails(selectedEvenement);

               // Créer une nouvelle scène pour afficher les détails de l'événement
               Scene scene = new Scene(root);
               scene.getStylesheets().add(getClass().getResource("/css/ajouter.css").toExternalForm());

               Stage stage = new Stage();
               stage.setTitle("Détails de l'événement");
               stage.setScene(scene);
               stage.show();

           } catch (IOException e) {
               e.printStackTrace();
               // Afficher une alerte si le chargement échoue
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Erreur");
               alert.setHeaderText("Erreur lors du chargement des détails");
               alert.setContentText("Une erreur est survenue lors de l'affichage des détails de l'événement.");
               alert.showAndWait();
           }
       } else {
           // Si aucun événement n'est sélectionné, afficher une alerte
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Aucune sélection");
           alert.setHeaderText("Aucun événement sélectionné");
           alert.setContentText("Veuillez sélectionner un événement pour afficher ses détails.");
           alert.showAndWait();
       }

    }

    @FXML
    void btnModifierEvenement(ActionEvent event) {
        Evenement selected = tableview.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                // Charger l'interface de modification de l'événement
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEvenement.fxml"));
                Parent root = loader.load();

                // Passer l'événement sélectionné au contrôleur
                ModifierEvenement modifierEvenementController = loader.getController();
                modifierEvenementController.initialize(selected);

                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/css/ajouter.css").toExternalForm());

                Stage stage = new Stage();
                stage.setTitle("Modifier l'Événement");
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Si aucun événement n'est sélectionné, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucun événement sélectionné");
            alert.setContentText("Veuillez sélectionner un événement à modifier.");
            alert.showAndWait();
        }
    }


    @FXML
    void btnAjouterEvenement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvenement.fxml")); // ajuste si besoin
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/ajouter.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Événement");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSupprimerEvenement(ActionEvent event) {
        // Vérifie si un événement est sélectionné
        Evenement selectedEvenement = tableview.getSelectionModel().getSelectedItem();

        if (selectedEvenement == null) {
            // Si aucun événement n'est sélectionné, afficher un message d'avertissement
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucun événement sélectionné");
            alert.setContentText("Veuillez sélectionner un événement à supprimer.");
            alert.showAndWait();
            return;
        }

        // Crée une boîte de dialogue de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cet événement ?");
        alert.setContentText("L'action est irréversible.");

        // Si l'utilisateur clique sur "OK"
        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                // Effectuer la suppression de l'événement sélectionné
                EvenementService.delete(selectedEvenement);

                // Mettre à jour la table après la suppression
                chargerEvenements();

                // Afficher un message de succès
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Suppression réussie");
                successAlert.setHeaderText(null);
                successAlert.setContentText("L'événement a été supprimé avec succès.");
                successAlert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                // En cas d'erreur lors de la suppression
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Erreur");
                errorAlert.setHeaderText("Erreur lors de la suppression");
                errorAlert.setContentText("Une erreur est survenue lors de la suppression de l'événement.");
                errorAlert.showAndWait();
            }
        }
    }
}
