package gui;
import services.PretService;

import java.sql.Connection;

import utils.MyConnection;

import entities.Pret;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;



import java.util.ArrayList;
import java.util.List;

public class ModifierPret {


        @FXML
        private CheckBox autres;

        @FXML
        private TextField datepret;

        @FXML
        private TextField duree;

        @FXML
        private RadioButton faible;

        @FXML
        private CheckBox fraismédicaux;

        @FXML
        private CheckBox mariage;

        @FXML
        private TextField montant;

        @FXML
        private RadioButton moyen;

        @FXML
        private CheckBox problémesfinanciers;

        @FXML
        private CheckBox urgencefamiliale;

        @FXML
        private RadioButton élevé;
        private Connection connection ;



    @FXML
        void modifier(ActionEvent event) {
            try {
                double montantValue = Double.parseDouble(montant.getText());
                int dureeValue = Integer.parseInt(duree.getText());
                String datePretValue = datepret.getText();
                int idPret = 1;

                String niveauUrgence = "";
                if (faible.isSelected()) {
                    niveauUrgence = "Faible";
                } else if (moyen.isSelected()) {
                    niveauUrgence = "Moyen";
                } else if (élevé.isSelected()) {
                    niveauUrgence = "Élevé";
                }

                List<String> motifs = new ArrayList<>();
                if (mariage.isSelected()) motifs.add("Mariage");
                if (problémesfinanciers.isSelected()) motifs.add("Problèmes financiers");
                if (fraismédicaux.isSelected()) motifs.add("Frais médicaux");
                if (autres.isSelected()) motifs.add("Autres");
                if (urgencefamiliale.isSelected()) motifs.add("Urgence familiale");

                String motifConcat = String.join(", ", motifs);

                Pret pret = new Pret();
                pret.setIdPret(idPret);
                pret.setMontant(montantValue);
                pret.setDuree(dureeValue);
                pret.setDatePret(datePretValue);
                pret.setNiveauUrgence(niveauUrgence);
                pret.setMotif(motifConcat);
                connection = MyConnection.getInstance().getConnection();
                PretService ps = new PretService(connection);
                ps.update(pret);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Le prêt a été modifié avec succès !");
                alert.showAndWait();

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Erreur lors de la modification : " + e.getMessage());
                alert.showAndWait();
            }
        }
    }


