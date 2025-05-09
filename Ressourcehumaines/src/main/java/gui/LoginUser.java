package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;


public class LoginUser {

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink signInLink;

    @FXML
    private CheckBox termsCheckBox;




    @FXML
    void handleSignUp(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }

        if (!termsCheckBox.isSelected()) {
            System.out.println("Vous devez accepter les Conditions Générales.");
            return;
        }


    }


    @FXML
    void termsCheckBox(ActionEvent event) {
        if (termsCheckBox.isSelected()) {
            System.out.println("Conditions Générales acceptées.");
        } else {
            System.out.println("Conditions Générales non acceptées.");
        }
    }


}
