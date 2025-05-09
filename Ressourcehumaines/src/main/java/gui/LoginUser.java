package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginUser {

    @FXML
    private TextField logemail;

    @FXML
    private Button loginuser;

    @FXML
    private TextField logmdp;

    @FXML
    private Button signup;

    @FXML
    void loginus(ActionEvent event) {

    }

    @FXML
    void signup(ActionEvent event) {
        Button source = (Button) event.getSource();
        String fxmlFile = "";

        if (source == signup) {
            fxmlFile = "/FXML/signup.fxml";

        } try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Erreur de chargement : " + e.getMessage());
        }

    }
}
