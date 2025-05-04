package gui;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.math.BigDecimal;
import java.time.LocalDate;
import services.pretservice;
import entities.pret;


public class ajouterpretcontroller {
    @FXML
    private TextField montantField;


    @FXML
    private TextField dureeField;

    @FXML
    private DatePicker datePretPicker;

    @FXML
    private TextField niveauUrgenceField;

    @FXML
    private TextField etatField;

    private pret pretEnCours; // utilisé si modification
    private listepretcontroller controllerPrincipal; // référence au contrôleur principal

    @FXML
    private void ajouterPret() {


        BigDecimal montant = new BigDecimal(montantField.getText());
            int duree = Integer.parseInt(dureeField.getText());
            LocalDate datePret = datePretPicker.getValue();
            String urgence = niveauUrgenceField.getText();
            String etat = etatField.getText();
            pret pret = new pret(0,montant, duree, datePret, urgence , etat) ;

        try {
            pretservice pretservice = new pretservice();
            pretservice.create(pret);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Le prêt a été enregistré avec succès !");
            alert.showAndWait();

            if (controllerPrincipal != null) {
                controllerPrincipal.rafraichirTable();
            }

            // Nettoyer les champs
            montantField.clear();
            dureeField.clear();
            datePretPicker.setValue(null);
            niveauUrgenceField.clear();
            etatField.clear();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Champs numériques incorrects");
            alert.setContentText("Veuillez saisir un montant et une durée valides.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


//    public void chargerPourModification(pret p) {
//        this.pretEnCours = p;
//        montantField.setText(String.valueOf(p.getMontant()));
//        dureeField.setText(String.valueOf(p.getDuree()));
//        datePretPicker.setValue(p.getDatePret());
//        niveauUrgenceField.setText(p.getNiveauUrgence());
//        etatField.setText(p.getEtat());
//    }


    public void setControllerPrincipal(listepretcontroller controller) {
        this.controllerPrincipal = controller;
    }
}
