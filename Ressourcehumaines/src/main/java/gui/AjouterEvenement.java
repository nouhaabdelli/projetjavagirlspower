package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AjouterEvenement {

    @FXML
    private TextField nomEvenement;

    @FXML
    private TextArea description;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextField lieu;

    @FXML
    private TextField organisateur;

    @FXML
    private TextField participantsMax;

    @FXML
    private ComboBox<String> statut;

    @FXML
    private TextField photoPath;

    // Ce bloc est exécuté automatiquement à l'initialisation du contrôleur
    @FXML
    public void initialize() {
        // Initialiser les options du ComboBox
        statut.getItems().addAll("prévu", "annulé", "terminé");
        statut.getSelectionModel().selectFirst();
    }

    @FXML
    private void parcourirPhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            photoPath.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void soumettre(ActionEvent event) {
        try {
            String nom = nomEvenement.getText().trim();
            String desc = description.getText().trim();
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();
            String lieuEvt = lieu.getText().trim();
            String orga = organisateur.getText().trim();
            int maxParticipants = Integer.parseInt(participantsMax.getText().trim());
            String statutEvt = statut.getValue();
            String photo = photoPath.getText().trim();

            // Vérification basique
            if (nom.isEmpty() || dateDebut == null || dateFin == null || lieuEvt.isEmpty() || orga.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            // Pour le test : affichage en console
            System.out.println("=== Événement Ajouté ===");
            System.out.println("Nom : " + nom);
            System.out.println("Description : " + desc);
            System.out.println("Date début : " + dateDebut);
            System.out.println("Date fin : " + dateFin);
            System.out.println("Lieu : " + lieuEvt);
            System.out.println("Organisateur : " + orga);
            System.out.println("Participants max : " + maxParticipants);
            System.out.println("Statut : " + statutEvt);
            System.out.println("Photo : " + photo);

            // Tu peux ici appeler un service ou DAO pour sauvegarder en base

            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'événement a été ajouté avec succès.");
            clearFields();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Le nombre de participants doit être un entier.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue.");
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        nomEvenement.clear();
        description.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        lieu.clear();
        organisateur.clear();
        participantsMax.clear();
        statut.getSelectionModel().selectFirst();
        photoPath.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



