package gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.io.IOException;

public class maquette {
    @FXML
    private StackPane contentPane;
    @FXML
    private Button handledemande;

    @FXML
    public void initialize() {
        loadUI("slideefeect.fxml");

    }

    @FXML
    private void handleAccueil() {
        loadUI("slideefeect.fxml");
    }
    @FXML
    private void handledemande() {
        loadDemandeScene("admin");
    }
    private void loadDemandeScene(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Demande.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur et lui passer le rôle
            DemandeController controller = loader.getController();
            controller.initData(role);

            Stage stage = (Stage) handledemande.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML private void handleFinance() {
        System.out.println("Finance");
    }

    @FXML private void handleReclamations() {
        loadUI("consulterreponse.fxml");
    }

    @FXML private void handleAnnonce() {

    }

    @FXML private void handleFormations() {
    }

    @FXML private void handleEvenements() {
    }

    @FXML private void handleProfil() {
    }

    @FXML private void toggleNotificationSidebar() {
    }

    private void loadUI(String fxmlFile) {
        try {
            Node node = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlFile)); // adapte le chemin
            contentPane.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
