
package gui;

import entities.Annonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;

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

        private Annonce annonce;


    public void setDetails(Annonce annonce) {
        this.annonce = annonce; // <-- C'est ce qui manquait

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

        @FXML
        void handleOuvrirPdf() {
            try {
                File fichier = new File("ressources/annonces/" + annonce.getPieceJointe());
                if (fichier.exists()) {
                    Desktop.getDesktop().open(fichier);
                } else {
                    showAlert("Fichier introuvable.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur lors de l'ouverture du PDF.");
            }
        }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    }


