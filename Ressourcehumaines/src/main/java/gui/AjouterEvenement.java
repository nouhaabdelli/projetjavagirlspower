package gui;

import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import services.EvenementService;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AjouterEvenement {

    @FXML private TextField titre;
    @FXML private TextArea description;
    @FXML private DatePicker dateDebut;
    @FXML private DatePicker dateFin;
    @FXML private TextField lieu;
    @FXML private TextField organisateur;
    @FXML private Spinner<Integer> participantsMax;
    @FXML private ComboBox<String> statut;
    @FXML private TextField affiche;

    private final EvenementService evenementService = new EvenementService();

    @FXML
    public void initialize() {
        // Initialiser le spinner et le comboBox
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, 50);
        participantsMax.setValueFactory(valueFactory);

        statut.getItems().addAll("prévu", "annulé", "terminé");
        statut.getSelectionModel().selectFirst();
    }

    @FXML
    void parcourir(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image pour l'événement");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            affiche.setText(file.getAbsolutePath());
        }
    }

    @FXML
    void annuler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Annuler la saisie ?");
        alert.setContentText("Voulez-vous vraiment effacer le formulaire ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            titre.clear();
            description.clear();
            dateDebut.setValue(null);
            dateFin.setValue(null);
            lieu.clear();
            organisateur.clear();
            participantsMax.getValueFactory().setValue(50);
            statut.getSelectionModel().selectFirst();
            affiche.clear();
        }
    }

    @FXML
    void soumettre(ActionEvent event) {
        String nom = titre.getText();
        String desc = description.getText();
        LocalDateTime debut = dateDebut.getValue() != null ? dateDebut.getValue().atStartOfDay() : null;
        LocalDateTime fin = dateFin.getValue() != null ? dateFin.getValue().atStartOfDay() : null;
        String lieuEvt = lieu.getText();
        String orga = organisateur.getText();
        Integer participants = participantsMax.getValue();
        String etat = statut.getValue();
        String img = affiche.getText();

        if (nom.isEmpty() || desc.isEmpty() || debut == null || fin == null || lieuEvt.isEmpty() || orga.isEmpty() || etat == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
            return;
        }

        Evenement evt = new Evenement(nom, desc, debut, fin, lieuEvt, orga, participants, etat, img);

        try {
            evenementService.create(evt);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Événement ajouté avec succès !");
            alert.showAndWait();
            annuler(null); // Réinitialise le formulaire après ajout
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Échec de l'ajout : " + e.getMessage());
            alert.showAndWait();
        }
    }
}

