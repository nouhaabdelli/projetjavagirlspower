package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class pretcontroller {
    @FXML
    private Label titrePrincipal;

    @FXML
    private VBox sideMenu;

    @FXML
    private AnchorPane mainContent;

    @FXML
    private Circle pulseDot;

    @FXML
    private void showAjouterPret() {
        if (titrePrincipal != null) {
            titrePrincipal.setText("Ajout d'un Prêt");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ajouterpret.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showListePret() {
        if (titrePrincipal != null) {
            titrePrincipal.setText("Liste des Prêts");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listepret.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showReponsesRH() {
        try {
            System.out.println("Chargement de reponsespretrh.fxml depuis pret.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reponsespretrh.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Resource 'fxml/reponsespretrh.fxml' not found.");
            }
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            if (getClass().getResource("/style/finance.css") != null) {
                newStage.getScene().getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
                System.out.println("finance.css chargé avec succès");
            }
            newStage.show();
            // Fermer l'interface actuelle
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            if (currentStage != null) {
                currentStage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetour() {
        try {
            System.out.println("Tentative de retour à l'interface précédente depuis pret.fxml");
            Stage stage = (Stage) mainContent.getScene().getWindow();
            stage.close();
            // Charger l'interface finance.fxml (ou une autre interface selon votre logique)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/finance.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            if (getClass().getResource("/style/finance.css") != null) {
                newStage.getScene().getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            }
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}