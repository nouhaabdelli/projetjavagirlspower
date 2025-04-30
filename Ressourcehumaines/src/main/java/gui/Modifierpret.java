package gui;

import services.PretService;
import utils.MyConnection;
import entities.Pret;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public  class Modifierpret {

    @FXML
    private CheckBox autres;

    @FXML
    private TextField datepret;

    @FXML
    private TextField duree;

    @FXML
    private RadioButton faible;

    @FXML
    private CheckBox fraismedicaux;

    @FXML
    private CheckBox mariage;

    @FXML
    private TextField montant;

    @FXML
    private RadioButton moyen;

    @FXML
    private CheckBox problemesfinanciers;

    @FXML
    private CheckBox urgencefamiliale;

    @FXML
    private RadioButton eleve;

    private Connection connection;

    // ID du prêt à modifier (existant dans la base)
    private final int idPret = 1;

    @FXML
    void modifier(ActionEvent event) {
        try {
            Pret pret = construirePretDepuisFormulaire();
            pret.setIdPret(idPret);  // affectation de l'ID à modifier

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

    // 🔄 Méthode privée réutilisable
    private Pret construirePretDepuisFormulaire() {
        Pret pret = new Pret();

        // Champs principaux
        pret.setMontant(Double.parseDouble(montant.getText()));
        pret.setDuree(Integer.parseInt(duree.getText()));
        pret.setDatePret(datepret.getText());

        // Niveau d'urgence
        String niveauUrgence = "";
        if (faible.isSelected()) niveauUrgence = "Faible";
        else if (moyen.isSelected()) niveauUrgence = "Moyen";
        else if (eleve.isSelected()) niveauUrgence = "Élevé";

        pret.setNiveauUrgence(niveauUrgence);

        // Motifs
        List<String> motifs = new ArrayList<>();
        if (mariage.isSelected()) motifs.add("Mariage");
        if (problemesfinanciers.isSelected()) motifs.add("Problèmes financiers");
        if (fraismedicaux.isSelected()) motifs.add("Frais médicaux");
        if (autres.isSelected()) motifs.add("Autres");
        if (urgencefamiliale.isSelected()) motifs.add("Urgence familiale");

        pret.setMotif(String.join(", ", motifs));

        return pret;
    }
}


