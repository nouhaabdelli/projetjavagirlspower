package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ReponseService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Reponses {

        @FXML
        private Label GrandTitre;

        @FXML
        private TableColumn<?, ?> colContenu;

        @FXML
        private TableColumn<?, ?> colDate;

        @FXML
        private TableColumn<?, ?> colFichierJoint;

        @FXML
        private TableColumn<?, ?> colImportance;

        @FXML
        private TableColumn<?, ?> colReclamationAssociee;

        @FXML
        private TableColumn<?, ?> colTitre;

        @FXML
        private VBox sideMenu;
    private final ReponseService reponseService = new ReponseService();
        @FXML
        private TableView<entities.Reponses> tableviewReponses;
    @FXML
    public void initialize() {
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colContenu.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateReponse"));
        colImportance.setCellValueFactory(new PropertyValueFactory<>("importance"));
        colFichierJoint.setCellValueFactory(new PropertyValueFactory<>("fichierJoint"));
        colReclamationAssociee.setCellValueFactory(new PropertyValueFactory<>("reclamationId")); // ou autre champ lié

        chargerReponses();
    }

    private void chargerReponses() {
        try {

            List<entities.Reponses> reponses = reponseService.readAll();
            ObservableList<entities.Reponses> observableList = FXCollections.observableList(reponses);
            tableviewReponses.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Chargement échoué", e.getMessage());
        }
    }

    @FXML
    void btnAjouterReponse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterReponse.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Ajouter une réponse");
            stage.setScene(scene);
            stage.show();
            stage.setOnHidden(e -> chargerReponses());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnModifierReponse(ActionEvent event) {
        entities.Reponses selected = (entities.Reponses) tableviewReponses.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierReponse.fxml"));
                Parent root = loader.load();

                ModifierReponse controller = loader.getController();
                controller.initialize(selected);

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Modifier la réponse");
                stage.setScene(scene);
                stage.show();
                stage.setOnHidden(e -> chargerReponses());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Aucune réponse sélectionnée", "Veuillez sélectionner une réponse à modifier.");
        }
    }

    @FXML
    void btnSupprimerReponse(ActionEvent event) {
        entities.Reponses selected = (entities.Reponses) tableviewReponses.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Êtes-vous sûr de vouloir supprimer cette réponse ?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        reponseService.delete(selected);
                        chargerReponses();
                        showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", null, "La réponse a été supprimée avec succès.");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Suppression échouée", e.getMessage());
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Aucune réponse sélectionnée", "Veuillez sélectionner une réponse à supprimer.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
