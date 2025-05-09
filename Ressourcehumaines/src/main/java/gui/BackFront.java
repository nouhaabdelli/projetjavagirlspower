package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class BackFront {

    @FXML
    private Button back;

    @FXML
    private Button front;

    @FXML
    void handleButtonClick(ActionEvent event) {
        Button source = (Button) event.getSource();
        String fxmlFile = "";

        if (source == back) {
            fxmlFile = "/FXML/CrudUser.fxml"; // vers le Back Office
        } else if (source == front) {
            fxmlFile = "/FXML/afficherUser.fxml"; // vers le Front Office
        }

        if (!fxmlFile.isEmpty()) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                System.out.println("Erreur de chargement : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
