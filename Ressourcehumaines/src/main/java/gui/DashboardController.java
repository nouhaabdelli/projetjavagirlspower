package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Button backOfficeButton;
    @FXML
    private Button frontOfficeButton;

    @FXML
    private void handleBackOffice() {
        loadDemandeScene("admin");
    }

    @FXML
    private void handleFrontOffice() {
        loadDemandeScene("employe");
    }

    private void loadDemandeScene(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Demande.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur et lui passer le rôle
            DemandeController controller = loader.getController();
            controller.initData(role);

            Stage stage = (Stage) backOfficeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}