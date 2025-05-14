package gui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import javafx.scene.layout.*;


import java.io.IOException;

public class maquette {
    @FXML
    private StackPane contentPane;

    @FXML
    public void initialize() {
        loadUI("slideefeect.fxml");

    }

    @FXML
    private void handleAccueil() {
        loadUI("slideefeect.fxml");
    }

    @FXML private void handleDemandes() {
        System.out.println("Demandes");
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
