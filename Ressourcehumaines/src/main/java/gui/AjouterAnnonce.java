package gui;

import entities.Annonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import services.AnnonceService;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AjouterAnnonce {

    @FXML
    private Label ajann;

    @FXML
    private DatePicker cal;

    @FXML
    private TextArea contenu;

    @FXML
    private Label contenuann;

    @FXML
    private Label ddp;

    @FXML
    private Label msg;

    @FXML
    private TextField picejoin;

    @FXML
    private Label pj;

    @FXML
    private TextField titre;

    @FXML
    private Label titreann;

    private final AnnonceService annonceService = new AnnonceService();

    @FXML
    void annuler(ActionEvent event) {
        // Si tu veux demander à l'utilisateur s'il est sûr de vouloir annuler
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Annuler la saisie ?");
        alert.setContentText("Voulez-vous vraiment annuler l'ajout de l'annonce ?");

        // Si l'utilisateur confirme l'annulation
        if (alert.showAndWait().get() == ButtonType.OK) {
            // Réinitialiser les champs
            titre.clear();
            contenu.clear();
            picejoin.clear();
            cal.setValue(null); // Réinitialiser la date

            // Ou fermer la fenêtre si nécessaire
            // Stage stage = (Stage) titre.getScene().getWindow();
            // stage.close();
        }
    }

    @FXML
    void parcourir(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une pièce jointe");
        File fichier = fileChooser.showOpenDialog(null);
        if (fichier != null) {
            picejoin.setText(fichier.getAbsolutePath());
        }
    }

    @FXML
    void soumettre(ActionEvent event) {
        String titreAnnonce = titre.getText();
        String contenuAnnonce = contenu.getText();
        LocalDate datePublication = cal.getValue();
        String pieceJointe = picejoin.getText();

        if (titreAnnonce.isEmpty() || contenuAnnonce.isEmpty() || datePublication == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
            return;
        }

        LocalDateTime dateTime = datePublication.atStartOfDay();
        Annonce annonce = new Annonce(titreAnnonce, contenuAnnonce, dateTime, pieceJointe);

        try {
            annonceService.create(annonce);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Annonce ajoutée avec succès !");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Échec lors de l'ajout de l'annonce : " + e.getMessage());
            alert.showAndWait();
        }
    }
}
