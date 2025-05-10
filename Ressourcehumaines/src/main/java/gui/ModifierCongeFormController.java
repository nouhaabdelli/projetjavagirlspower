package gui;
import entities.Demande;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;


public class ModifierCongeFormController {

    private Demande demande;

    @FXML
    private TextField typeField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker dateField;
    @FXML
    private ComboBox<String> statutField;

    // Méthode d'initialisation avec les données de la demande
    public void initData(Demande demande) {
        this.demande = demande;
        typeField.setText(demande.getType());
        descriptionField.setText(demande.getDescription());
        dateField.setValue(demande.getDateSoumission());
        statutField.setValue(demande.getStatut());
    }

    // Méthode pour enregistrer les modifications
    @FXML
    private void saveChanges() {
        demande.setType(typeField.getText());
        demande.setDescription(descriptionField.getText());
        demande.setDateSoumission(dateField.getValue());
        demande.setStatut(statutField.getValue());

        // Mettre à jour la base de données (ajouter la logique ici)
        // Par exemple : updateDemandeInDatabase(demande);

        // Fermer la fenêtre
        Stage stage = (Stage) typeField.getScene().getWindow();
        stage.close();
    }
}

