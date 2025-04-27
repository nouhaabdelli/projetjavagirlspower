package gui;

import services.AnnonceService;


import entities.Annonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import services.AnnonceService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

    public class AjouterAnnonce {

        private final AnnonceService annonceService = new AnnonceService();

        @FXML
        private TextField TFTitre;

        @FXML
        private TextField TFContenu;

        @FXML
        private DatePicker DPDatePublication;

        @FXML
        private TextField TFPieceJointe;

        @FXML
        void ajouter(ActionEvent event) {
            String titre = TFTitre.getText();
            String contenu = TFContenu.getText();
            LocalDate datePublication = DPDatePublication.getValue();
            String pieceJointe = TFPieceJointe.getText();

            // Conversion de LocalDate en LocalDateTime (heure par défaut à 00:00)
            LocalDateTime localDateTime = datePublication.atStartOfDay();

            Annonce annonce = new Annonce(titre, contenu, localDateTime, pieceJointe);

            try {
                annonceService.create(annonce);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setContentText("Annonce ajoutée avec succès !");
                alert.showAndWait();

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }

        @FXML
        void afficher(ActionEvent event) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/AfficherAnnonce.fxml"));
                DPDatePublication.getScene().setRoot(root);

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

