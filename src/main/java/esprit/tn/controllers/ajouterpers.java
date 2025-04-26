package esprit.tn.controllers;
package esprit.tn.models;
package esprit.tn.services;

import esprit.tn.models.User;
import esprit.tn.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ajouterpers {
    UserService userService = new UserService();
    User user =new User() ;
    user.setPrenom(tfPrenom.getText());
    user.setNom(tnom.getText());




    @FXML
    private TextField tfage;

    @FXML
    private TextField tfnom;

    @FXML
    private TextField tfprenom;

    @FXML
    void valider(ActionEvent event) {

    }


    }
}
