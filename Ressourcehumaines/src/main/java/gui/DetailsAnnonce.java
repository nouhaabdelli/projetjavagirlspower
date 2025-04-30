
package gui;

import entities.Annonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

    public class DetailsAnnonce {

        @FXML
        private Label detail;  // Label pour afficher le détail de l'annonce

        @FXML
        private Label detcont;  // Label pour afficher le contenu de l'annonce

        @FXML
        private Label detdat;  // Label pour afficher la date de publication

        @FXML
        private Label detpiece;  // Label pour afficher la pièce jointe

        @FXML
        private Label dettit;  // Label pour afficher le titre de l'annonce

        // Méthode pour afficher les détails de l'annonce
        public void setDetails(Annonce annonce) {
            dettit.setText("Titre : " + annonce.getTitre());  // Affiche le titre
            detcont.setText("Contenu : " + annonce.getContenu());  // Affiche le contenu
            detdat.setText("Date de publication : " + annonce.getDatePublication());  // Affiche la date de publication
            detpiece.setText("Pièce jointe : " + (annonce.getPieceJointe() != null ? annonce.getPieceJointe() : "Aucune"));  // Affiche la pièce jointe, ou "Aucune" si elle est vide
        }

        // Méthode pour fermer la fenêtre
        @FXML
        void detferm(ActionEvent event) {
            // Ferme la fenêtre actuelle
            Stage stage = (Stage) detdat.getScene().getWindow();  // Récupère la fenêtre actuelle
            stage.close();  // Ferme la fenêtre
        }
    }


