package gui;

import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    @FXML
    private ImageView imagevieww;


    public void setDetails(Evenement evenement) {
        nomEvenementLabel.setText(evenement.getNomEvenement());
        descriptionLabel.setText(evenement.getDescription());
        dateDebutLabel.setText(String.valueOf(evenement.getDateDebut()));
        dateFinLabel.setText(String.valueOf(evenement.getDateFin()));
        lieuLabel.setText(evenement.getLieu());
        organisateurLabel.setText(evenement.getOrganisateur());
        participantsMaxLabel.setText(String.valueOf(evenement.getParticipantsMax()));
        statutLabel.setText(evenement.getStatut());
        // Afficher la photo si elle existe
        if (evenement.getPhoto() != null && !evenement.getPhoto().isEmpty()) {
            // Créer l'image à partir du chemin de la photo
            // Utiliser le chemin fourni par evenement.getPhoto()
            Image image = new Image("file:" + evenement.getPhoto()); // Cela chargera l'image à partir du chemin réel
            imagevieww.setImage(image);  // Afficher l'image dans l'ImageView
            photoLabel.setText("Photo disponible");
        } else {
            // Si aucune photo n'est disponible, afficher un message ou laisser vide
            imagevieww.setImage(null);  // Aucune image affichée
            photoLabel.setText("Aucune photo");
        }

    }


    @FXML
    void fermerDetails(ActionEvent event) {
        Stage stage = (Stage) titreLabel.getScene().getWindow();
        stage.close();
    }
}
