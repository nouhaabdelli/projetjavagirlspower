package gui;

import entities.Reponses;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.ReponseService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class AfficherReponse {
    private final ReponseService reponsesService = new ReponseService();
    private Reponses reponse;

    @FXML
    private Text dateText;

    @FXML
    private Hyperlink fichierHyperlink;

    @FXML
    private Text importanceText;

    @FXML
    private Button quitterButton;

    @FXML
    private Text titre;

    @FXML
    private TextArea titreTextArea;

    @FXML
    void quitter(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Appelée depuis le contrôleur parent
    public void setReponse(Reponses reponse) {
        this.reponse = reponse;
        displayReponseDetails();
    }

    @FXML
    void initialize() {
        // Animation du titre
        FadeTransition titleFade = new FadeTransition(Duration.millis(600), titre);
        titleFade.setFromValue(0);
        titleFade.setToValue(1);
        titleFade.play();

        Timeline titleScale = new Timeline(
                new KeyFrame(Duration.millis(600), new KeyValue(titre.scaleYProperty(), 1))
        );
        titleScale.play();
    }

    private void displayReponseDetails() {
        if (reponse != null) {
            try {
                // Remplir les champs
                titreTextArea.setText(reponse.getContenu());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                dateText.setText(reponse.getDateReponse().format(formatter));

                importanceText.setText(reponse.getPriorite());
// Fichier joint
                String chemin = reponse.getFichierJoint();
                if (chemin != null && !chemin.isEmpty()) {
                    File fichier = new File(chemin);
                    if (fichier.exists()) {
                        fichierHyperlink.setText("Ouvrir pièce jointe");
                        fichierHyperlink.setDisable(false);
                        fichierHyperlink.setOnAction(e -> {
                            try {
                                Desktop.getDesktop().open(fichier);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });
                    } else {
                        fichierHyperlink.setText("Fichier introuvable");
                        fichierHyperlink.setDisable(true);
                    }
                } else {
                    fichierHyperlink.setText("Aucune pièce jointe");
                    fichierHyperlink.setDisable(true);
                }


            } catch (Exception e) {
                showErrorAlert("Erreur d'affichage", e.getMessage());
            }


        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
