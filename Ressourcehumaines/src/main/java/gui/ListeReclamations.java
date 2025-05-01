package gui;
import javafx.scene.control.ButtonType;

import entities.Reclamations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ReclamationService;

import java.sql.SQLException;
import java.util.List;

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
    private TableView<Reclamations> tableViewReclamations;

    private ReclamationService reclamationService;

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
            colNotifications.setCellValueFactory(new PropertyValueFactory<>("notifications"));
            colPieceJointe.setCellValueFactory(new PropertyValueFactory<>("cheminPieceJointe"));
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Impossible de récupérer les réclamations : " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void afficher(ActionEvent event) {
        // Logique pour afficher les détails de la réclamation
        Reclamations selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            // Affiche les détails de la réclamation sélectionnée
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        // Logique pour modifier la réclamation sélectionnée
        Reclamations selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            // Affiche l'interface de modification
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        // Logique pour supprimer la réclamation sélectionnée
        Reclamations selectedReclamation = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (selectedReclamation != null) {
            // Affiche une alerte de confirmation avant suppression
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette réclamation ?");
            alert.setContentText("Cette action est irréversible.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
//                    // Supprime la réclamation
//                    try {
////                        reclamationService.delete(selectedReclamation.getId()); // Appel à la méthode delete du service
//                        tableViewReclamations.getItems().remove(selectedReclamation); // Retire la réclamation de la table
//                    } catch (SQLException e) {
//                        Alert errorAlert = new Alert(AlertType.ERROR);
//                        errorAlert.setTitle("Erreur de suppression");
//                        errorAlert.setContentText("Une erreur est survenue lors de la suppression : " + e.getMessage());
//                        errorAlert.showAndWait();
//                    }
                }
            });
        }
    }
}
