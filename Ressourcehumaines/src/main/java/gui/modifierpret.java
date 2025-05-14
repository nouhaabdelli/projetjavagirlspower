package gui;

import javafx.stage.Stage;
import services.pretservice;
import entities.pret;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class modifierpret {
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
    public void modifierPret(ActionEvent event) {
        try {
            BigDecimal montant = new BigDecimal(montantField.getText());
            int duree = Integer.parseInt(dureeField.getText());
            LocalDate date = datePretPicker.getValue();
            String urgence = niveauUrgenceField.getText();
            String etat = etatField.getText();

            pretEnCours.setMontant(montant);
            pretEnCours.setDuree(duree);
            pretEnCours.setDatePret(date);
            pretEnCours.setNiveauUrgence(urgence);
            pretEnCours.setEtat(etat);

            pretservice pretservice = new pretservice();
            pretservice.update(pretEnCours);

            if (controllerPrincipal != null) {
                controllerPrincipal.rafraichirTable();
            }

            ((Stage) montantField.getScene().getWindow()).close();

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void chargerPourModification(pret p) {
        this.pretEnCours = p;
        System.out.println("Chargement du prêt pour modification, ID = " + p.getIdavance());

        montantField.setText(String.valueOf(p.getMontant()));
        dureeField.setText(String.valueOf(p.getDuree()));
        datePretPicker.setValue(p.getDatePret());
        niveauUrgenceField.setText(p.getNiveauUrgence());
        etatField.setText(p.getEtat());
    }

    public void setControllerPrincipal(listepretcontroller controller) {
        this.controllerPrincipal = controller;
    }
}