package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;

import java.io.IOException;

public class avancecontroller {
    @FXML
    private AnchorPane mainContent;

    @FXML
    private Label titrePrincipal;

    // Méthode appelée lorsqu'on clique sur "Ajouter Avance"
    @FXML
    private void showAjouterAvance() {
        try {
            Parent ajouterAvancePane = FXMLLoader.load(getClass().getResource("/fxml/ajouteravance.fxml"));
            mainContent.getChildren().setAll(ajouterAvancePane);
            titrePrincipal.setText("➕ Ajouter une Avance");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée lorsqu'on clique sur "Liste Avances"
    @FXML
    private void showListeAvance() {
        try {
            Parent listeAvancePane = FXMLLoader.load(getClass().getResource("/fxml/listeavance.fxml"));
            mainContent.getChildren().setAll(listeAvancePane);
            titrePrincipal.setText("📋 Liste des Avances");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée lorsqu'on clique sur "Réponses RH"
    @FXML
    private void showReponsesRH() {
        try {
            Parent reponsesPane = FXMLLoader.load(getClass().getResource("/fxml/reponsesrh.fxml"));
            mainContent.getChildren().setAll(reponsesPane);
            titrePrincipal.setText("💬 Réponses RH");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}