package gui;
import entities.Reclamations;
import java.sql.SQLException;
import javafx.fxml.FXML;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import services.ReclamationService;
import javafx.scene.control.Hyperlink;

import java.io.IOException;
import java.time.LocalDate;
public class AjouterReclamations   {
    private final ReclamationService reclamationService = new ReclamationService();
    @FXML
    private TextField tftitre;
    @FXML
    private TextArea boxtext;

    @FXML
    private Hyperlink hyperlinkPieceJointe;

    private String cheminPieceJointe ;
    @FXML
    void ajouterreclamations(ActionEvent event) {
        String titre = tftitre.getText();
        String description = boxtext.getText();
        LocalDate date = LocalDate.now();
        String statut = "En attente";


        if (titre.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champ(s) vide(s)");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs avant de soumettre la réclamation !");
            alert.showAndWait();
            return; // Arrêter l'exécution ici
        }

        // Crée une réclamation avec le chemin de la pièce jointe
        Reclamations reclamation = new Reclamations(0, titre, description, date, statut, cheminPieceJointe);

        try {
            reclamationService.create(reclamation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Réclamation ajoutée avec succès !");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ajout : " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    void ajoutpiece(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        Stage stage = (Stage) tftitre.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            cheminPieceJointe = file.getAbsolutePath();
            hyperlinkPieceJointe.setText(file.getName()); // Affiche juste le nom du fichier
            hyperlinkPieceJointe.setOnAction(e -> {
                try {
                    java.awt.Desktop.getDesktop().open(file); // Ouvre le fichier avec le programme associé
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            System.out.println("Fichier sélectionné : " + cheminPieceJointe);
        }
    }
    @FXML
    void afficher(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherReclamation.fxml"));
            boxtext.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
