package gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import entities.User;
import java.io.IOException;

public class ModifierUser {

    @FXML
    private Button adduser;

    @FXML
    private Button back;

    @FXML
    private TextField usadresse;

    @FXML
    private RadioButton uscelibataire;

    @FXML
    private TextField uscnam;

    @FXML
    private RadioButton usdivorce;

    @FXML
    private TextField usemail;

    @FXML
    private DatePicker usembauche;

    @FXML
    private TextField usenfant;

    @FXML
    private RadioButton usfemale;

    @FXML
    private RadioButton usmale;

    @FXML
    private RadioButton usmarie;

    @FXML
    private DatePicker usnaissance;

    @FXML
    private TextField usnom;

    @FXML
    private TextField usprenom;

    @FXML
    private TextField usrib;

    @FXML
    private TextField usrole;

    @FXML
    private TextField usstatut;

    @FXML
    private TextField ustelephone;

    @FXML
    void add(ActionEvent event) {

    }

    @FXML
    void back(ActionEvent event) {try
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CrudUser.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    catch (IOException e) {
        e.printStackTrace();
    }

    }
    public void setUserData(User user) {
        usnom.setText(user.getNom());
        usprenom.setText(user.getPrenom());
        usemail.setText(user.getEmail());
        ustelephone.setText(user.getNumTelephone());
        usrib.setText(user.getRib());
        usenfant.setText(String.valueOf(user.getNombreEnfant()));
        uscnam.setText(user.getCnam());
        usstatut.setText(user.getStatut());
        usadresse.setText(user.getAdresse());
        usrole.setText(user.getRole());

        usembauche.setValue(user.getDateEmbauche());
        usnaissance.setValue(user.getDateNaissance());

        if ("male".equalsIgnoreCase(user.getGenre())) {
            usmale.setSelected(true);
        } else {
            usfemale.setSelected(true);
        }

        switch (user.getSituationFamiliale().toLowerCase()) {
            case "marie":
            case "marie(e)":
                usmarie.setSelected(true);
                break;
            case "divorce":
            case "divorce(e)":
                usdivorce.setSelected(true);
                break;
            case "celibataire":
                uscelibataire.setSelected(true);
                break;
        }
    }


}


