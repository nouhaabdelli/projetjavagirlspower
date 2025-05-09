package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class acceuilAnn {

    @FXML
    private void showBackoffice(ActionEvent event) {
        try {
            // Charger le fichier FXML de backoffice
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/annonces.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et changer le contenu
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showFrontoffice(ActionEvent event) {
        try {
            // Charger le fichier FXML de frontoffice
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/annoncefo.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et changer le contenu
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

