
package gui;

import entities.Annonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

    public class DetailsAnnonce {

        @FXML
        private Label detail;

        @FXML
        private Label detcont;

        @FXML
        private Label detdat;

        @FXML
        private Label detpiece;

        @FXML
        private Label dettit;


        public void setDetails(Annonce annonce) {
            dettit.setText("Titre : " + annonce.getTitre());
            detcont.setText("Contenu : " + annonce.getContenu());
            detdat.setText("Date de publication : " + annonce.getDatePublication());
            detpiece.setText("Pièce jointe : " + (annonce.getPieceJointe() != null ? annonce.getPieceJointe() : "Aucune"));
        }


        @FXML
        void detferm(ActionEvent event) {

            Stage stage = (Stage) detdat.getScene().getWindow();
            stage.close();
        }
    }


