package gui;

import entities.Evenement;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import services.EvenementService;

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
    private ImageView imageView;

    @FXML
    private TextField photoPath;
    private boolean evenementAjoute = false;

    public boolean isEvenementAjoute() {
        return evenementAjoute;
    }


    private final EvenementService service = new EvenementService(); // Service pour interagir avec la BD

    @FXML
    public void initialize() {
        statut.getItems().addAll("prévu", "annulé", "terminé");
        statut.getSelectionModel().selectFirst();
    }

    @FXML
    private void parcourirPhoto (ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Afficher le chemin dans le TextField
            photoPath.setText(selectedFile.getAbsolutePath());

            // Charger et afficher l'image dans l'ImageView
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
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
            String statutEvt = statut.getValue();
            String photo = photoPath.getText().trim();


            if (nom.isEmpty() || dateDebut == null || dateFin == null || lieuEvt.isEmpty() || orga.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs obligatoires.");
                return;
            }


            int maxParticipants;
            try {
                maxParticipants = Integer.parseInt(participantsMax.getText().trim());
                if (maxParticipants <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Participants invalides", "Le nombre de participants doit être un entier strictement positif.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Format invalide", "Veuillez entrer un entier valide pour le nombre de participants.");
                return;
            }

            LocalDate aujourdHui = LocalDate.now();

            if (statutEvt.equals("terminé")) {

                if (dateDebut.isAfter(aujourdHui)) {
                    showAlert(Alert.AlertType.WARNING, "Date début invalide", "Pour un événement terminé, la date de début ne peut pas être dans le futur.");
                    return;
                }


                if (dateFin.isBefore(dateDebut)) {
                    showAlert(Alert.AlertType.WARNING, "Date fin invalide", "La date de fin doit être après la date de début.");
                    return;
                }
                if (dateFin.isAfter(aujourdHui)) {
                    showAlert(Alert.AlertType.WARNING, "Date fin invalide", "Pour un événement terminé, la date de fin ne peut pas être dans le futur.");
                    return;
                }
            } else {

                if (!dateFin.isAfter(dateDebut)) {
                    showAlert(Alert.AlertType.WARNING, "Dates invalides", "La date de fin doit être postérieure à la date de début.");
                    return;
                }
            }


            LocalDateTime dateDebutTime = LocalDateTime.of(dateDebut, LocalTime.of(8, 0));
            LocalDateTime dateFinTime = LocalDateTime.of(dateFin, LocalTime.of(8, 0));
            Evenement e = new Evenement(nom, desc, dateDebutTime, dateFinTime, lieuEvt, orga, maxParticipants, statutEvt, photo);

            service.create(e);
            evenementAjoute = true; // Marque que l'ajout a réussi
            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'événement a été ajouté avec succès.");
            clearFields();

// Ferme la fenêtre après ajout
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();


        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l’ajout.");
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
        imageView.setImage(null);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
