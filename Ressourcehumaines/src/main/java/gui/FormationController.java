package gui;

import entities.Formation;
import javafx.scene.layout.AnchorPane;
import services.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FormationController implements Initializable {

    @FXML private TableView<Formation> formationTable;
    @FXML private TableColumn<Formation, String> titreCol;
    @FXML private TableColumn<Formation, String> descCol;
    @FXML private TableColumn<Formation, String> domaineCol;
    @FXML private TableColumn<Formation, String> lieuCol;
    @FXML private TableColumn<Formation, String> debutCol;
    @FXML private TableColumn<Formation, String> finCol;

    @FXML private TextField titreField;
    @FXML private TextArea descriptionField;
    @FXML private TextField domaineField;
    @FXML private TextField lieuField;
    @FXML private DatePicker dateDebutField;
    @FXML private DatePicker dateFinField;
    @FXML private TextField idField;

    @FXML private TextField searchField;

    private ObservableList<Formation> formationList = FXCollections.observableArrayList();

    private Stage primaryStage;
    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTable();
        try {
            loadFormations();
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur de chargement des formations.");
        }
    }

    private void initTable() {
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        domaineCol.setCellValueFactory(new PropertyValueFactory<>("domaine"));
        lieuCol.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        debutCol.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        finCol.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
    }

    private void loadFormations() throws SQLException {
        FormationService service = new FormationService();
        formationList.clear();
        formationList.addAll(service.afficherFormations());
        formationTable.setItems(formationList);
    }

    @FXML
    void addFormation(ActionEvent event) throws SQLException {
        try {
            String titre = titreField.getText();
            String desc = descriptionField.getText();
            String domaine = domaineField.getText();
            String lieu = lieuField.getText();
            String debut = dateDebutField.getValue().toString();
            String fin = dateFinField.getValue().toString();


            if (titre.isEmpty() || desc.isEmpty() || domaine.isEmpty() || lieu.isEmpty() || debut.isEmpty() || fin.isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis.");
                return;
            }

            Formation formation = new Formation(titre, desc, domaine, lieu, debut, fin);
            new FormationService().ajouterFormation(formation);

            showAlert("Succès", "Formation ajoutée.");
            clearFields();
            loadFormations();
        } catch (Exception e) {
            showAlert("Erreur", "Échec de l'ajout de la formation.");
        }
        loadFormations();
    }

    @FXML
    private void updateFormation(ActionEvent event) throws SQLException, IOException {
        Formation selected = formationTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditFormationForm.fxml"));
        Parent root = loader.load();

        EditFormationController editController = loader.getController();
        editController.setFormation(selected);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Modifier Formation");
        stage.setScene(new Scene(root));
        stage.showAndWait();

        loadFormations();
    }
    @FXML
    void deleteFormation(ActionEvent event) {
        Formation selected = formationTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                new FormationService().supprimerFormation(selected.getIdFormation());
                showAlert("Succès", "Formation supprimée.");
                clearFields();
                loadFormations();
            } catch (SQLException e) {
                showAlert("Erreur", "Échec de suppression.");
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner une formation.");
        }
    }

    @FXML
    void getSelected(MouseEvent event) {

    }

    @FXML
    void searchFormation(KeyEvent event) {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) {
            formationTable.setItems(formationList);
        } else {
            ObservableList<Formation> filtered = FXCollections.observableArrayList();
            for (Formation f : formationList) {
                if (f.getTitre().toLowerCase().contains(keyword)
                        || f.getDescription().toLowerCase().contains(keyword)
                        || f.getDomaine().toLowerCase().contains(keyword)
                        || f.getLieu().toLowerCase().contains(keyword)) {
                    filtered.add(f);
                }
            }
            formationTable.setItems(filtered);
        }
    }

    @FXML
    void printFormation(ActionEvent event) {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            job.showPrintDialog(primaryStage);
            job.printPage(formationTable);
            job.endJob();
        }
    }

    private void clearFields() {

    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
