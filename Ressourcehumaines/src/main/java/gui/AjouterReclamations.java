package gui;


import javafx.scene.control.TextArea;

import java.sql.SQLException;

import entities.Reclamations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import services.ReclamationService;

import java.io.IOException;
import java.time.LocalDate;
public class AjouterReclamations   {
    private final ReclamationService reclamationService = new ReclamationService();
    @FXML
    private TextField tftitre;
    @FXML
    private TextArea boxtext;

    @FXML
    void ajouterreclamations(ActionEvent event) {
        String titre = tftitre.getText();
        String description = boxtext.getText();
        LocalDate date = LocalDate.now();
        String statut = "En attente";

        Reclamations reclamation = new Reclamations(0,titre, description, date, statut);

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
    void afficher(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherReclamation.fxml"));
            boxtext.getScene().setRoot(root);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
