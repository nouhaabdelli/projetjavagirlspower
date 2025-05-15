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

public class CrudUser {

    @FXML
    private Button afficherUser;

    @FXML
    private Button ajouterUser;

    @FXML
    private Button modifierUser;

    @FXML
    private Button supprimerUser;

    @FXML
    void handleButtonClick(ActionEvent event) {
        Button source = (Button) event.getSource();
        String fxmlFile = "";

        if (source == ajouterUser) {
            fxmlFile = "/FXML/ajouterUser.fxml";
        } else if (source == modifierUser) {
            fxmlFile = "/FXML/modifierUser.fxml";
        } //else if (source == supprimerUser) {
           // fxmlFile = "/FXML/supprimerUser.fxml";
         else if (source == afficherUser) {
            fxmlFile = "/FXML/afficherUser.fxml";
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur de chargement : " + e.getMessage());
        }
    }
}


