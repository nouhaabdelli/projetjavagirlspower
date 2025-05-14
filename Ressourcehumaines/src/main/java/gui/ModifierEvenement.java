package gui;

import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.EvenementService;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ModifierEvenement {
    @FXML
    private TextField nomEvenement;

    @FXML
    private TextArea description;

    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;

    @FXML
    private TextField lieu;

    @FXML
    private TextField organisateur;

    @FXML
    private TextField participantsMax;

    @FXML
    private TextField statut;

    @FXML
    private TextField photo;

    private Evenement selectedEvenement;
    private final EvenementService evenementService = new EvenementService();


    public void initialize(Evenement selectedEvenement) {
        this.selectedEvenement = selectedEvenement;


        nomEvenement.setText(selectedEvenement.getNomEvenement());
        description.setText(selectedEvenement.getDescription());
        dateDebut.setValue(selectedEvenement.getDateDebut().toLocalDate());
        dateFin.setValue(selectedEvenement.getDateFin().toLocalDate());
        lieu.setText(selectedEvenement.getLieu());
        organisateur.setText(selectedEvenement.getOrganisateur());
        participantsMax.setText(String.valueOf(selectedEvenement.getParticipantsMax()));
        statut.setText(selectedEvenement.getStatut());
        photo.setText(selectedEvenement.getPhoto());
    }

    @FXML
    void soumettre(ActionEvent event) {

        String newNomEvenement = nomEvenement.getText();
        String newDescription = description.getText();
        String newLieu = lieu.getText();
        String newOrganisateur = organisateur.getText();
        int newParticipantsMax = Integer.parseInt(participantsMax.getText());
        String newStatut = statut.getText();
        String newPhoto = photo.getText();
        String newDateDebut = dateDebut.getValue().toString();
        String newDateFin = dateFin.getValue().toString();


        selectedEvenement.setNomEvenement(newNomEvenement);
        selectedEvenement.setDescription(newDescription);
        selectedEvenement.setLieu(newLieu);
        selectedEvenement.setOrganisateur(newOrganisateur);
        selectedEvenement.setParticipantsMax(newParticipantsMax);
        selectedEvenement.setStatut(newStatut);
        selectedEvenement.setPhoto(newPhoto);
        selectedEvenement.setDateDebut(Timestamp.valueOf(newDateDebut + " 00:00:00").toLocalDateTime());
        selectedEvenement.setDateFin(Timestamp.valueOf(newDateFin + " 00:00:00").toLocalDateTime());

        try {
            evenementService.update(selectedEvenement);


            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Modification réussie");
            successAlert.setHeaderText(null);
            successAlert.setContentText("L'événement a été modifié avec succès.");
            successAlert.showAndWait();


            Stage stage = (Stage) nomEvenement.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Erreur lors de la mise à jour");
            errorAlert.setContentText("Une erreur est survenue lors de la mise à jour de l'événement.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    void annuler(ActionEvent event) {

        Stage stage = (Stage) nomEvenement.getScene().getWindow();
        stage.close();
    }
}
