package gui ;
import entities.Demande;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import services.DemandeDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DemandeAdminController {

    @FXML
    private TableView<Demande> tableViewDemandes;

    @FXML
    private TableColumn<Demande, Integer> idColumn;

    @FXML
    private TableColumn<Demande, String> statutColumn;

    @FXML
    private TableColumn<Demande, String> descriptionColumn;

    @FXML
    private TableColumn<Demande, LocalDate> dateColumn;

    @FXML
    private TableColumn<Demande, String> typeColumn;

    @FXML
    private TableColumn<Demande, Void> actionColumn;


    private DemandeDAO demandeService;

    private ObservableList<Demande> demandesList;

    @FXML
    public void initialize() {
        demandeService = new DemandeDAO();
        demandesList = FXCollections.observableArrayList();

        // Configuration des colonnes
        typeColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getType()));
        idColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        statutColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatut()));
        descriptionColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDescription()));
        dateColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDateSoumission()));

        // Colonne pour les actions
        actionColumn.setCellFactory(createActionButtonCellFactory());

        // Charger les données des demandes
        loadDemandes();

    }

    private void loadDemandes() {
        try {
            demandesList.clear(); // Clear previous list
            List<Demande> demandes = demandeService.getAllDemandesEnAttente(); // Fetch pending demands from database

            if (demandes != null && !demandes.isEmpty()) {
                demandesList.addAll(demandes); // Add all fetched demands to the list
                tableViewDemandes.setItems(demandesList); // Set the items in the table view
            } else {
                showInfoAlert("Aucune demande en attente."); // Optional: Show a message if no pending demands
            }
        } catch (SQLException e) {
            showErrorAlert("Erreur lors du chargement des demandes : " + e.getMessage());
        }
    }

    private Callback<TableColumn<Demande, Void>, TableCell<Demande, Void>> createActionButtonCellFactory() {
        return new Callback<TableColumn<Demande, Void>, TableCell<Demande, Void>>() {
            @Override
            public TableCell<Demande, Void> call(TableColumn<Demande, Void> param) {
                return new TableCell<Demande, Void>() {
                    private final Button validateButton = new Button("Valider");
                    private final Button rejectButton = new Button("Refuser");

                    {
                        // Action du bouton "Valider"
                        validateButton.setOnAction(event -> {
                            Demande demande = getTableRow().getItem();
                            if (demande != null && !demande.getStatut().equals("Validée") && !demande.getStatut().equals("Refusée")) {
                                validerDemande(demande);
                            }
                        });

                        // Action du bouton "Refuser"
                        rejectButton.setOnAction(event -> {
                            Demande demande = getTableRow().getItem();
                            if (demande != null && !demande.getStatut().equals("Validée") && !demande.getStatut().equals("Refusée")) {
                                refuserDemande(demande);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(10, validateButton, rejectButton); // Ajouter les boutons dans un HBox
                            setGraphic(hbox);
                        }
                    }
                };
            }
        };
    }

    private void validerDemande(Demande demande) {
        try {
            demande.setStatut("Validée");
            demande.setDateValidation(LocalDate.now());
            updateStatutAndValidationDateInDB(demande);
            tableViewDemandes.refresh(); // Recharger le tableau après validation
            showSuccessAlert("La demande a été validée avec succès.");
        } catch (SQLException e) {
            showErrorAlert("Erreur lors de la validation de la demande : " + e.getMessage());
        }
    }

    private void refuserDemande(Demande demande) {
        try {
            demande.setStatut("Refusée");
            demande.setDateValidation(LocalDate.now());
            updateStatutAndValidationDateInDB(demande);
            tableViewDemandes.refresh(); // Recharger le tableau après refus
            showSuccessAlert("La demande a été refusée.");
        } catch (SQLException e) {
            showErrorAlert("Erreur lors du refus de la demande : " + e.getMessage());
        }
    }

    private void updateStatutAndValidationDateInDB(Demande demande) throws SQLException {
        // Gestion de l'exception ici, car cette méthode interagit avec la base de données
        demandeService.updateDemandeStatut(demande);
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour traiter toutes les demandes en une fois
    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}