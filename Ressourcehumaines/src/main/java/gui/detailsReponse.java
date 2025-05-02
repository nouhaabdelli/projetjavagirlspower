package gui;
import entities.Reponses;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class detailsReponse {
    @FXML
    private Label titreLabel;
    @FXML
    private Label contenuLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label importanceLabel;
    @FXML
    private Label fichierLabel;

    // Méthode pour afficher les détails de la réponse
    public void setDetails(Reponses reponse) {
            titreLabel.setText(reponse.getTitre());
            contenuLabel.setText(reponse.getContenu());
            dateLabel.setText(reponse.getDateReponse().toString());
            importanceLabel.setText(reponse.getImportance());
            fichierLabel.setText(reponse.getFichierJoint() != null ? reponse.getFichierJoint() : "Aucun");
        }

        @FXML
        void fermerDetails(ActionEvent event) {
            Stage stage = (Stage) titreLabel.getScene().getWindow();
            stage.close();
        }
}