package gui;

import entities.Annonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import services.AnnonceService;
import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

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


    @FXML private TextField picejoin; // Champ non modifiable par l'utilisateur
    private File fichierSelectionne;

    @FXML
    private Label pj;

    @FXML
    private TextField titre;

    @FXML
    private Label titreann;

    private final AnnonceService annonceService = new AnnonceService();
    private boolean annonceAjoutee = false;  // Pour vérifier si l'annonce a été ajoutée


    public boolean isAnnonceAjoutee() {
        return annonceAjoutee;
    }


    @FXML
    public void initialize() {
        cal.setValue(LocalDate.now());
    }

    @FXML
    void annuler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Annuler la saisie ?");
        alert.setContentText("Voulez-vous vraiment annuler l'ajout de l'annonce ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            titre.clear();
            contenu.clear();
            picejoin.clear();
            cal.setValue(null);
        }
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void parcourir() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
        Window stage = titre.getScene().getWindow(); // Pour obtenir la fenêtre

        File fichier = fileChooser.showOpenDialog(stage);
        if (fichier != null) {
            fichierSelectionne = fichier;
            picejoin.setText(fichier.getName());
        }
    }
    @FXML
    void soumettre(ActionEvent event) {
        String titreAnnonce = titre.getText();
        String contenuAnnonce = contenu.getText();
        LocalDate datePublication = cal.getValue();
        String pieceJointe = picejoin.getText();

        if (titreAnnonce.isEmpty() || contenuAnnonce.isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
            return;
        }

        LocalDateTime dateTime = datePublication.atStartOfDay();

        if (fichierSelectionne != null) {
            try {
                File dossier = new File("ressources/annonces");
                if (!dossier.exists()) dossier.mkdirs();

                Path destination = Paths.get(dossier.getAbsolutePath(), fichierSelectionne.getName());
                Files.copy(fichierSelectionne.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                pieceJointe = fichierSelectionne.getName(); // On met à jour le nom réel copié
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur fichier");
                alert.setContentText("Erreur lors de la copie de la pièce jointe.");
                alert.showAndWait();
                return;
            }
        }
        Annonce annonce = new Annonce(titreAnnonce, contenuAnnonce, dateTime, pieceJointe);

        try {
            annonceService.create(annonce);
            annonceAjoutee = true;  // Annonce ajoutée avec succès
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



            // Fermer la fenêtre actuelle (formulaire d'ajout)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

    }


}
