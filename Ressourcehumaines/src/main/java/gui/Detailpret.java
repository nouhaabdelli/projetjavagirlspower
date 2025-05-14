package gui;
import entities.pret;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Detailpret {
    @FXML
    private Label labelMontant;

    @FXML
    private Label labelDuree;

    @FXML
    private Label labelDatePret;

    @FXML
    private Label labelNiveauUrgence;

    @FXML
    private Label labelEtat;


    public void setPret(pret p) {
        labelMontant.setText(p.getMontant().toString());
        labelDuree.setText(String.valueOf(p.getDuree()));
        labelDatePret.setText(p.getDatePret().toString());
        labelNiveauUrgence.setText(p.getNiveauUrgence());
        labelEtat.setText(p.getEtat());
    }
}