package gui;

import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DetailsEvenement {

    @FXML
    private Label titreLabel;

    @FXML
    private Label nomEvenementLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label dateDebutLabel;

    @FXML
    private Label dateFinLabel;

    @FXML
    private Label lieuLabel;

    @FXML
    private Label organisateurLabel;

    @FXML
    private Label participantsMaxLabel;

    @FXML
    private Label statutLabel;

    @FXML
    private Label photoLabel;

    // Méthode pour afficher les détails de l'événement
    public void setDetails(Evenement evenement) {
        nomEvenementLabel.setText(evenement.getNomEvenement());
        descriptionLabel.setText(evenement.getDescription());
        dateDebutLabel.setText(String.valueOf(evenement.getDateDebut()));
        dateFinLabel.setText(String.valueOf(evenement.getDateFin()));
        lieuLabel.setText(evenement.getLieu());
        organisateurLabel.setText(evenement.getOrganisateur());
        participantsMaxLabel.setText(String.valueOf(evenement.getParticipantsMax()));
        statutLabel.setText(evenement.getStatut());
        photoLabel.setText(evenement.getPhoto() != null ? evenement.getPhoto() : "Aucune");
    }

    // Méthode pour fermer la fenêtre
    @FXML
    void fermerDetails(ActionEvent event) {
        Stage stage = (Stage) titreLabel.getScene().getWindow();
        stage.close();
    }
}
