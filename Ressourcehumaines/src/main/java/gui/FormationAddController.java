package gui;

import entities.Formation;
import javafx.scene.Parent;
import services.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FormationAddController implements Initializable {



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

    }

    @FXML
    void addFormation(ActionEvent event) throws SQLException, IOException {
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
        } catch (Exception e) {
            showAlert("Erreur", "Échec de l'ajout de la formation.");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Formations.fxml"));
            Parent listView = loader.load();

            // Remplacer le contenu de rootPane par la nouvelle interface
            rootPane.getChildren().setAll(listView);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void updateFormation(ActionEvent event) {
        try {
            int id = Integer.parseInt(idField.getText());
            String titre = titreField.getText();
            String desc = descriptionField.getText();
            String domaine = domaineField.getText();
            String lieu = lieuField.getText();

            String debut = dateDebutField.getValue().toString();
            String fin = dateFinField.getValue().toString();

            Formation formation = new Formation(titre, desc, domaine, lieu, debut, fin);
            new FormationService().modifierFormation( formation);

            showAlert("Succès", "Formation modifiée.");
            clearFields();
        } catch (Exception e) {
            showAlert("Erreur", "Veuillez sélectionner une formation.");
        }
    }




    private void clearFields() {
        titreField.clear();
        descriptionField.clear();
        domaineField.clear();
        lieuField.clear();
        dateDebutField.setValue(null);
        dateFinField.setValue(null);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
