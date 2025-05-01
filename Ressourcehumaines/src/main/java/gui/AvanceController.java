package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AvanceController {

    @FXML
    void ajouter(ActionEvent event) {
        loadScene("/gui/ajouterAvance.fxml");
    }

    @FXML
    void modifier(ActionEvent event) {
        loadScene("/gui/modifierAvance.fxml");
    }

    @FXML
    void supprimer(ActionEvent event) {
        loadScene("/gui/supprimerAvance.fxml");
    }

    @FXML
    void afficher(ActionEvent event) {
        loadScene("/gui/afficherAvance.fxml");
    }

    private void loadScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
