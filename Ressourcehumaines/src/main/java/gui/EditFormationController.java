package gui;

import entities.Formation;
import services.FormationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;

public class EditFormationController {

    @FXML private TextField titreField, domaineField, lieuField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker dateDebutField, dateFinField;
    @FXML private TextField idField;

    private Formation formation;
    private FormationService formationService = new FormationService();

    @FXML
    public void initialize() {
        // Initialize any necessary components here
    }

    public void setFormation(Formation formation) {
        this.formation = formation;

        titreField.setText(formation.getTitre());
        descriptionField.setText(formation.getDescription());
        domaineField.setText(formation.getDomaine());
        lieuField.setText(formation.getLieu());
        dateDebutField.setValue(LocalDate.parse(formation.getDateDebut()));
        dateFinField.setValue(LocalDate.parse(formation.getDateFin()));
        idField.setText(String.valueOf(formation.getIdFormation()));
    }

    @FXML
    private void saveChanges() {
        formation.setTitre(titreField.getText());
        formation.setDescription(descriptionField.getText());
        formation.setDomaine(domaineField.getText());
        formation.setLieu(lieuField.getText());
        formation.setDateDebut(dateDebutField.getValue().toString());
        formation.setDateFin(dateFinField.getValue().toString());

        try {
            formationService.modifierFormation(formation);
            ((Stage) titreField.getScene().getWindow()).close(); // Close popup
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
