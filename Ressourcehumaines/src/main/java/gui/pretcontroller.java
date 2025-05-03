package gui;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;

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
            Parent ajouterPretPane = FXMLLoader.load(getClass().getResource("/fxml/ajouterpret.fxml"));
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
                Parent listePretPane = FXMLLoader.load(getClass().getResource("/fxml/listepret.fxml"));
                mainContent.getChildren().setAll(listePretPane);
                titrePrincipal.setText("📋 Liste des Prêts");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
