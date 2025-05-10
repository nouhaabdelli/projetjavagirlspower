package gui;

import entities.Demande;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ModifierAttestationFormController {

    private Demande demande;

    @FXML
    private TextField typeField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker dateField;

    @FXML
    private ComboBox<String> statutField;

    @FXML
    public void initialize() {
        // Remplir les options du comboBox de statut
        statutField.getItems().addAll("En attente", "Validée", "Refusée");
    }

    // Méthode pour initialiser les champs avec les données de la demande
    public void initData(Demande demande) {
        this.demande = demande;

        typeField.setText(demande.getType());
        descriptionField.setText(demande.getDescription());
        dateField.setValue(demande.getDateSoumission());
        statutField.setValue(demande.getStatut());
    }

    // Enregistrement des modifications
    @FXML
    private void saveChanges() {
        if (demande != null) {
            demande.setType(typeField.getText());
            demande.setDescription(descriptionField.getText());
            demande.setDateSoumission(dateField.getValue());
            demande.setStatut(statutField.getValue());

            // Ici tu peux appeler une méthode DAO pour mettre à jour dans la base
            // ex : DemandeService.update(demande);

            // Fermer la fenêtre
            Stage stage = (Stage) typeField.getScene().getWindow();
            stage.close();
        }
    }
}