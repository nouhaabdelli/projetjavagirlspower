package gui;

import entities.avance;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class Detailavancecontroller {

    @FXML
    private Label montantLabel;

    @FXML
    private Label dureeLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label niveauUrgenceLabel;

    @FXML
    private Label etatLabel;

    private avance avance;

    public void setAvance(avance avance) {
        this.avance = avance;
        if (avance != null) {
            montantLabel.setText("Montant : " + avance.getMontant());
            dureeLabel.setText("Durée : " + avance.getDuree());
            dateLabel.setText("Date Avance : " + avance.getDateAvance());
            niveauUrgenceLabel.setText("Niveau Urgence : " + avance.getNiveauUrgence());
            etatLabel.setText("État : " + avance.getEtat());
        }
    }

    @FXML
    private void handleRetour() {
        try {
            System.out.println("Tentative de retour à listeavance.fxml depuis detailsavance.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listeavance.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
                System.out.println("finance.css chargé avec succès");
            } else {
                System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources.");
            }
            newStage.setScene(scene);
            newStage.show();
            Stage currentStage = (Stage) montantLabel.getScene().getWindow();
            if (currentStage != null) {
                currentStage.close();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de listeavance.fxml dans detailsavancecontroller : " + e.getMessage());
            e.printStackTrace();
        }
    }
}