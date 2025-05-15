package gui;

import entities.Certificat;
import entities.Formation;
import entities.User;
import services.Certificatservices;
import services.FormationService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EditCertificatController {

    @FXML private TextField titreField, validiteField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker dateExpirationPicker;
    @FXML private ComboBox<String> niveauComboBox;
    @FXML private ComboBox<User> userComboBox;
    @FXML private ComboBox<Formation> formationComboBox;

    private Certificat certificat;
    private final FormationService formationService = new FormationService();
    private final Certificatservices certificatService = new Certificatservices();

    @FXML
    public void initialize() {
        niveauComboBox.setItems(FXCollections.observableArrayList("Débutant", "Intermédiaire", "Avancé"));

        try {
            List<User> users = certificatService.getAllUsers();
            userComboBox.setItems(FXCollections.observableArrayList(users));

            List<Formation> formations = formationService.afficherFormations();
            formationComboBox.setItems(FXCollections.observableArrayList(formations));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCertificat(Certificat certif) {
        this.certificat = certif;

        titreField.setText(certif.getTitre());
        descriptionField.setText(certif.getDescription());
        validiteField.setText(String.valueOf(certif.getValiditeAnnees()));
        dateExpirationPicker.setValue(LocalDate.parse(certif.getDateExpiration()));
        niveauComboBox.setValue(certif.getNiveau());

        // Match user by ID
        for (User u : userComboBox.getItems()) {
            if (u.getId() == certif.getUserId()) {
                userComboBox.setValue(u);
                break;
            }
        }

        // Match formation by ID
        for (Formation f : formationComboBox.getItems()) {
            if (f.getIdFormation() == certif.getFormationid()) {
                formationComboBox.setValue(f);
                break;
            }
        }
    }



    @FXML
    private void saveChanges() {
        certificat.setTitre(titreField.getText());
        certificat.setDescription(descriptionField.getText());
        certificat.setValiditeAnnees(Integer.parseInt(validiteField.getText()));
        certificat.setDateExpiration(dateExpirationPicker.getValue().toString());
        certificat.setNiveau(niveauComboBox.getValue());

        User selectedUser = userComboBox.getValue();
        Formation selectedFormation = formationComboBox.getValue();

        if (selectedUser != null) {
            certificat.setUserId(selectedUser.getId());
            certificat.setUsername(selectedUser.getUsername());
        }

        if (selectedFormation != null) {
            certificat.setFormationid(selectedFormation.getIdFormation());
        }

        try {

        certificatService.modifierCertificat(certificat);
        ((Stage) titreField.getScene().getWindow()).close();
        } catch (SQLException e) {
            e.printStackTrace();
        }// Close popup
    }
}
