package gui;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class pretcontroller {
    @FXML
    private AnchorPane mainContent;

    @FXML
    private Label titrePrincipal;

    // Méthode appelée lorsqu'on clique sur "Ajouter Prêt"
    @FXML
    private void showAjouter() {
        try {
            Pane ajouterPretPane = FXMLLoader.load(getClass().getResource("/gui/ajouterpret.fxml"));
            mainContent.getChildren().setAll(ajouterPretPane);
            titrePrincipal.setText("➕ Ajouter un Prêt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode appelée lorsqu'on clique sur "Liste Prêts"
    @FXML
    private void showListe() {
        try {
            Pane listePretPane = FXMLLoader.load(getClass().getResource("/gui/listepret.fxml"));
            mainContent.getChildren().setAll(listePretPane);
            titrePrincipal.setText("📋 Liste des Prêts");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
