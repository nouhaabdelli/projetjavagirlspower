package gui;

import entities.Conge;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModifierCongeFormController {

    private Conge conge;

    @FXML
    private TextField textMotif;

    @FXML
    private TextField textTypeConge;

    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;

    @FXML
    private ComboBox<String> comboStatut;

    @FXML
    private Button btnSaveChanges;

    // Méthode pour injecter les données dans le formulaire de modification
    public void setConge(Conge conge) {
        this.conge = conge;
        // Remplir les champs avec les valeurs de la demande de congé
        textMotif.setText(conge.getMotif());
        textTypeConge.setText(conge.getTypeConge());
        dateDebut.setValue(conge.getDateDebut());
        dateFin.setValue(conge.getDateFin());
        comboStatut.setValue(conge.getStatut());
    }

    // Méthode pour sauvegarder les modifications
    @FXML
    private void saveChanges() {
        // Vérification des champs avant la sauvegarde
        if (textMotif.getText().isEmpty() || textTypeConge.getText().isEmpty() ||
                dateDebut.getValue() == null || dateFin.getValue() == null || comboStatut.getValue() == null) {
            // Afficher un message d'erreur si certains champs sont vides
            System.out.println("Tous les champs doivent être remplis !");
            return;
        }

        // Mettre à jour les informations dans l'objet conge
        conge.setMotif(textMotif.getText());
        conge.setTypeConge(textTypeConge.getText());
        conge.setDateDebut(dateDebut.getValue());
        conge.setDateFin(dateFin.getValue());
        conge.setStatut(comboStatut.getValue());

        // Vous pouvez ajouter ici votre logique pour sauvegarder dans la base de données
        // Par exemple : DemandeService.getInstance().updateConge(conge);

        // Fermer la fenêtre après la mise à jour
        Stage stage = (Stage) textMotif.getScene().getWindow();
        stage.close();
    }

    // Méthode pour annuler la modification et revenir à la page précédente
    @FXML
    private void retourPagePrecedente(ActionEvent event) {
        // Fermer la fenêtre actuelle
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}