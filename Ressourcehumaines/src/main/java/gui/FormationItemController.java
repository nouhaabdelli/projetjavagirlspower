package gui;

import entities.Formation;
import main.MyListener1;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public class FormationItemController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label dateDebutLabel;

    @FXML
    private Label dateFinLabel;

    @FXML
    private Label descriptionLabel;

    private Formation formation;
    private MyListener1 myListener;
    // Called from the front controller to pass data to this item
    public void setFormation(Formation formation, MyListener1 myListener) {
        this.myListener = myListener;

        this.formation = formation;
        titleLabel.setText(formation.getTitre());
        dateDebutLabel.setText("Début : " + formation.getDateDebut().toString());
        dateFinLabel.setText("Fin : " + formation.getDateFin().toString());
        descriptionLabel.setText(formation.getDescription());
    }
    @FXML
    public void click(MouseEvent mouseEvent) throws SQLException {
        myListener.onClickListener(formation);
    }
    // Handle item click
    @FXML
    private void handleClick(MouseEvent event) {
        // Example: print or perform action on click
        System.out.println("Formation sélectionnée : " + formation.getTitre());
        // Optional: open details view or emit event

    }
}
