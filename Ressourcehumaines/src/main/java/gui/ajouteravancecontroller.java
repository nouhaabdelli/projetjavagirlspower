package gui;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.math.BigDecimal;
import java.time.LocalDate;
import services.avanceservice;
import entities.avance;

public class ajouteravancecontroller {
    @FXML
    private TextField montantField;
    @FXML
    private TextField dureeField;

    @FXML
    private DatePicker dateAvancePicker;

    @FXML
    private TextField niveauUrgenceField;

    @FXML
    private TextField etatField;

    private avance avanceEnCours; // utilisé si modification
    private listeavancecontroller controllerPrincipal; // référence au contrôleur principal

    @FXML
    private void ajouterAvance() {
        try {
            BigDecimal montant = new BigDecimal(montantField.getText());
            int duree = Integer.parseInt(dureeField.getText());

            LocalDate dateAvance = dateAvancePicker.getValue();
            String urgence = niveauUrgenceField.getText();
            String etat = etatField.getText();
            avance avance = new avance(0, montant,duree, dateAvance, urgence, etat);

            avanceservice avanceService = new avanceservice();
            avanceService.create(avance);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("L'avance a été enregistrée avec succès !");
            alert.showAndWait();

            if (controllerPrincipal != null) {
                controllerPrincipal.rafraichirTable();
            }

            // Nettoyer les champs
            montantField.clear();
            dateAvancePicker.setValue(null);
            niveauUrgenceField.clear();
            etatField.clear();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Champs numériques incorrects");
            alert.setContentText("Veuillez saisir un montant valide.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void setControllerPrincipal(listeavancecontroller controller) {
        this.controllerPrincipal = controller;
    }
}