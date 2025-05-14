package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class DashboardConrtoller {

    @FXML
    private AnchorPane anch;

    private void loadUI(String fxml) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            anch.getChildren().setAll(pane);
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ajoutFormation() {
        loadUI("FormationAdd.fxml");
    }

    @FXML
    private void AfficherFormation() {
        loadUI("Formations.fxml");
    }

    @FXML
    private void ajouterCertif() {
        loadUI("CertificatAdd.fxml");
    }

    @FXML
    private void AfficherCertif() {
        loadUI("Certificats.fxml");
    }
}
