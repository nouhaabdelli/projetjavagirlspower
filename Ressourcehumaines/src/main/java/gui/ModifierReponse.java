package gui;


import entities.Reponses;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ReponseService;

import java.sql.SQLException;
import java.time.LocalDate;

public class ModifierReponse {
    @FXML private TextField titre;
    @FXML private TextArea contenu;
    @FXML private DatePicker dateReponse;
    @FXML private TextField importance; // Tu peux le changer en ComboBox si nécessaire
    @FXML private TextField fichierJoint;

    private Reponses selectedReponse;
    private final ReponseService reponseService = new ReponseService();

    // Initialiser les champs avec la réponse sélectionnée
    public void initialize(Reponses selectedReponse) {
        this.selectedReponse = selectedReponse;

        titre.setText(selectedReponse.getTitre());
        contenu.setText(selectedReponse.getContenu());
        dateReponse.setValue(selectedReponse.getDateReponse());
        importance.setText(selectedReponse.getImportance());
        fichierJoint.setText(selectedReponse.getFichierJoint());
    }

    @FXML
    void soumettre(ActionEvent event) {
        try {
            // Mettre à jour les valeurs
            selectedReponse.setTitre(titre.getText().trim());
            selectedReponse.setContenu(contenu.getText().trim());
            selectedReponse.setDateReponse(dateReponse.getValue());
            selectedReponse.setImportance(importance.getText().trim());
            selectedReponse.setFichierJoint(fichierJoint.getText().trim());

            reponseService.update(selectedReponse);

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Modification réussie");
            success.setHeaderText(null);
            success.setContentText("La réponse a été modifiée avec succès.");
            success.showAndWait();

            // Fermer la fenêtre
            Stage stage = (Stage) titre.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Erreur");
            error.setHeaderText("Erreur lors de la mise à jour");
            error.setContentText("Une erreur est survenue lors de la mise à jour de la réponse.");
            error.showAndWait();
        }
    }

    @FXML
    void annuler(ActionEvent event) {
        Stage stage = (Stage) titre.getScene().getWindow();
        stage.close();
    }
}
