package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class avancecontroller {
    @FXML
    private AnchorPane mainContent;

    @FXML
    private Label titrePrincipal;

    @FXML
    private void initialize() {
        // Au démarrage, on ne charge rien dans mainContent
        // L'interface initiale est définie directement dans avance.fxml
        titrePrincipal.setText("Gestion des  Avances");
    }

    @FXML
    private void showAjouterAvance() {
        try {
            Parent ajouterAvancePane = FXMLLoader.load(getClass().getResource("/fxml/ajouteravance.fxml"));
            mainContent.getChildren().setAll(ajouterAvancePane);
            titrePrincipal.setText("➕ Ajouter une Avance");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de ajouteravance.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showListeAvance() {
        try {
            Parent listeAvancePane = FXMLLoader.load(getClass().getResource("/fxml/listeavance.fxml"));
            mainContent.getChildren().setAll(listeAvancePane);
            titrePrincipal.setText("📋 Liste des Avances");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de listeavance.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showReponsesRH() {
        try {
            Parent reponsesPane = FXMLLoader.load(getClass().getResource("/fxml/reponsesrh.fxml"));
            mainContent.getChildren().setAll(reponsesPane);
            titrePrincipal.setText("💬 Réponses RH");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de reponsesrh.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetour() {
        try {
            System.out.println("Tentative de retour à finance.fxml depuis avance.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/finance.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
                System.out.println("finance.css chargé avec succès");
            } else {
                System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources.");
            }
            newStage.setScene(scene);
            newStage.show();
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            if (currentStage != null) {
                currentStage.close();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de finance.fxml dans avancecontroller : " + e.getMessage());
            e.printStackTrace();
        }
    }
}