package gui;

import entities.avance;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.avanceservice;
import java.math.BigDecimal;
import java.sql.SQLException;

public class modifieravancecontroller {

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
    @FXML
    private Button saveButton;

    private avance avanceEnCours;
    private listeavancecontroller controllerPrincipal;

    public void setAvanceEnCours(avance avance) {
        this.avanceEnCours = avance;
        if (avance != null) {
            montantField.setText(String.valueOf(avance.getMontant()));
            dureeField.setText(String.valueOf(avance.getDuree()));
            dateAvancePicker.setValue(avance.getDateAvance());
            niveauUrgenceField.setText(avance.getNiveauUrgence());
            etatField.setText(avance.getEtat());
        }
    }

    public void setControllerPrincipal(listeavancecontroller controller) {
        this.controllerPrincipal = controller;
    }

    @FXML
    private void initialize() {
        saveButton.setOnAction(event -> {
            if (avanceEnCours != null) {
                try {
                    avanceEnCours.setMontant(new BigDecimal(montantField.getText()));
                    avanceEnCours.setDuree(Integer.parseInt(dureeField.getText()));
                    avanceEnCours.setDateAvance(dateAvancePicker.getValue());
                    avanceEnCours.setNiveauUrgence(niveauUrgenceField.getText());
                    avanceEnCours.setEtat(etatField.getText());

                    avanceservice avanceService = new avanceservice();
                    avanceService.update(avanceEnCours);
                    if (controllerPrincipal != null) {
                        controllerPrincipal.rafraichirTable();
                    }
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();
                } catch (SQLException | NumberFormatException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la modification : " + e.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }
}