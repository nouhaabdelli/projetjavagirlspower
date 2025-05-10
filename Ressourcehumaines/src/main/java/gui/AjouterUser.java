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
import entities.User;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.UserCrud;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.io.File;




public class AjouterUser {
    @FXML
    private Button back;
    @FXML
    private Text usenfantLabel;
    @FXML
    private TextField uscin;



    @FXML
    private Button adduser;

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
    private Button par;

    @FXML
    private ImageView pdp;
    @FXML
    private TextField photopath;

    @FXML
    void add(ActionEvent event) {
        // Vérification des champs vides
        if (usnom.getText().isEmpty() || usprenom.getText().isEmpty() || usadresse.getText().isEmpty()
                || usemail.getText().isEmpty() || ustelephone.getText().isEmpty() || usrib.getText().isEmpty()
                || usstatut.getText().isEmpty() || uscnam.getText().isEmpty()
                || usnaissance.getValue() == null || usembauche.getValue() == null
                || (!usmale.isSelected() && !usfemale.isSelected())
                || (!usmarie.isSelected() && !uscelibataire.isSelected() && !usdivorce.isSelected())) {

            showAlert("Champs vides", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        // Vérification du format email
        if (!usemail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            showAlert("Email invalide", "Veuillez saisir une adresse e-mail valide.");
            return;
        }


        // Vérification du numéro de téléphone (ex : 8 chiffres)
        if (!ustelephone.getText().matches("\\d{8}")) {
            showAlert("Téléphone invalide", "Veuillez saisir un numéro de téléphone valide (8 chiffres).");
            return;
        }

        // Vérification du nombre d'enfants
        int nombreEnfant;
        try {
            nombreEnfant = Integer.parseInt(usenfant.getText());
            if (nombreEnfant < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert("Nombre invalide", "Veuillez saisir un nombre d'enfants valide (entier positif).");
            return;
        }
// Vérification du nom
        if (!usnom.getText().matches("^[A-Za-zÀ-ÿ\\s'-]{2,}$")) {
            showAlert("Nom invalide", "Le nom ne doit contenir que des lettres et faire au moins 2 caractères.");
            return;
        }

// Vérification du prénom
        if (!usprenom.getText().matches("^[A-Za-zÀ-ÿ\\s'-]{2,}$")) {
            showAlert("Prénom invalide", "Le prénom ne doit contenir que des lettres et faire au moins 2 caractères.");
            return;
        }
        LocalDate dateNaissance = usnaissance.getValue();
        LocalDate dateEmbauche = usembauche.getValue();
        LocalDate aujourdHui = LocalDate.now();

// Vérification date de naissance
        if (dateNaissance.isAfter(aujourdHui)) {
            showAlert("Date de naissance invalide", "La date de naissance ne peut pas être dans le futur.");
            return;
        }

        int age = Period.between(dateNaissance, aujourdHui).getYears();
        if (age < 18) {
            showAlert("Âge invalide", "L'utilisateur doit avoir au moins 18 ans.");
            return;
        }

// Vérification date d'embauche
        if (dateEmbauche.isAfter(aujourdHui)) {
            showAlert("Date d'embauche invalide", "La date d'embauche ne peut pas être dans le futur.");
            return;
        }

        if (dateEmbauche.isBefore(dateNaissance)) {
            showAlert("Incohérence des dates", "La date d'embauche ne peut pas être antérieure à la date de naissance.");
            return;
        }
/// Vérification du champ CIN
        if (uscin.getText().isEmpty()) {
            showAlert("CIN requis", "Le champ CIN est obligatoire.");
            return;
        }

// Vérification que le CIN est un entier valide
        int cinValue;
        try {
            cinValue = Integer.parseInt(uscin.getText());
        } catch (NumberFormatException e) {
            showAlert("CIN invalide", "Le CIN doit être un entier valide.");
            return;
        }





        // Autres données
        String genre = usmale.isSelected() ? "Homme" : "Femme";
        String situationFamiliale = usmarie.isSelected() ? "Marié" :
                uscelibataire.isSelected() ? "Célibataire" :
                        "Divorcé";

        // Création de l'utilisateur
        User user = new User();
        user.setNom(usnom.getText());
        user.setPrenom(usprenom.getText());
        user.setAdresse(usadresse.getText());
        user.setEmail(usemail.getText());
        user.setNumTelephone(ustelephone.getText());
        user.setRib(usrib.getText());
        user.setStatut(usstatut.getText());
        user.setCnam(uscnam.getText());
        user.setMotDePasse("123456");
        user.setRole("utilisateur");
        user.setNombreEnfant(nombreEnfant);
        user.setPhotoProfil(photopath.getText());
        user.setGenre(genre);
        user.setSituationFamiliale(situationFamiliale);
        user.setDateNaissance(usnaissance.getValue());
        user.setDateEmbauche(usembauche.getValue());
        // Si tout est valide, ajouter le CIN à l'utilisateur
        user.setCin(cinValue);

        // Ajout dans la base
        UserCrud crud = new UserCrud();
        crud.ajouterUser(user);

        showAlert("Ajout Réussi", "L'utilisateur a été ajouté avec succès !");
    }

    // Fonction pour afficher des alertes
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void back(ActionEvent event) { try
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

    }  @FXML
    public void initialize() {
        // Masquer au démarrage
        usenfant.setVisible(false);
        usenfantLabel.setVisible(false);

        usmarie.setOnAction(event -> {
            boolean selected = usmarie.isSelected();
            usenfant.setVisible(selected);
            usenfantLabel.setVisible(selected);
        });

        uscelibataire.setOnAction(event -> {
            if (uscelibataire.isSelected()) {
                usenfant.setVisible(false);
                usenfantLabel.setVisible(false);
            }
        });

        usdivorce.setOnAction(event -> {
            if (usdivorce.isSelected()) {
                usenfant.setVisible(false);
                usenfantLabel.setVisible(false);
            }
        });
    }
    @FXML
    void par(ActionEvent event) { FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Afficher le chemin dans le TextField
            photopath.setText(selectedFile.getAbsolutePath());

            // Charger et afficher l'image dans l'ImageView
            Image image = new Image(selectedFile.toURI().toString());
            pdp.setImage(image);
        }

    }



}