package gui;

import entities.avance;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Detailavancecontroller {

    @FXML
    private Label labelIdAvance;

    @FXML
    private Label labelMontant;
    @FXML
    private Label labelDuree;

    @FXML
    private Label labelDateAvance;

    @FXML
    private Label labelNiveauUrgence;

    @FXML
    private Label labelEtat;

    public void setAvance(avance a) {
        labelIdAvance.setText(String.valueOf(a.getIdAvance()));
        labelMontant.setText(a.getMontant().toString());
        labelDuree.setText(String.valueOf(a.getDuree()));
        labelDateAvance.setText(a.getDateAvance().toString());
        labelNiveauUrgence.setText(a.getNiveauUrgence());
        labelEtat.setText(a.getEtat());
    }
}