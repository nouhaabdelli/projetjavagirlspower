package gui;
import java.util.List;
import java.util.ArrayList;

import entities.Pret;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class Ajouterpret {

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
    private CheckBox problèmesfinanciers;

    @FXML
    private CheckBox urgencefamiliale;

    @FXML
    private RadioButton élevé;

    private int idPret;
    private     String motif;


    @FXML
    void ajouter(ActionEvent event) {
        try {
            // Récupération et conversion des valeurs des champs de texte
            double montantValue = Double.parseDouble(montant.getText());
            int dureeValue = Integer.parseInt(duree.getText());
            String datePretValue = datepret.getText();

            // Récupération de la valeur sélectionnée dans les boutons radio
            String niveauUrgence = "";
            if (faible.isSelected()) {
                niveauUrgence = "Faible";
            } else if (moyen.isSelected()) {
                niveauUrgence = "Moyen";
            } else if (élevé.isSelected()) {
                niveauUrgence = "Élevé";
            }

            // Récupération des motifs sélectionnés dans les cases à cocher
            List<String> motifs = new ArrayList<>();
            if (mariage.isSelected()) {
                motifs.add("Mariage");
            }
            if (problèmesfinanciers.isSelected()) {
                motifs.add("Problèmes financiers");
            }
            if (fraismédicaux.isSelected()) {
                motifs.add("Frais médicaux");
            }
            if (autres.isSelected()) {
                motifs.add("Autres");
            }
            if (urgencefamiliale.isSelected()) {
                motifs.add("Urgence familiale");
            }

            // Création de l'objet Pret avec les valeurs saisies
            Pret pret = new Pret(idPret, montantValue, dureeValue, datePretValue, niveauUrgence, motif);

            // Affichage pour vérification
            System.out.println("Prêt ajouté : " + pret);

        } catch (NumberFormatException e) {
            // Gestion des erreurs de conversion
            System.out.println("Erreur : Veuillez entrer des valeurs numériques valides pour le montant et la durée.");
        }
    }
}
