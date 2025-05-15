package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import entities.User;
import services.UserCrud;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

public class ModifierUser {
    @FXML
    private Text usenfantLabel;
    @FXML private Button adduser;
    @FXML private Button back;
    @FXML private TextField usadresse, uscnam, usemail, usenfant, usnom, usprenom, usrib, usrole, usstatut, ustelephone;
    @FXML private RadioButton uscelibataire, usdivorce, usfemale, usmale, usmarie;
    @FXML private DatePicker usembauche, usnaissance;

    private int selectedUserId = -1; // ID du user à modifier

    @FXML
    void add(ActionEvent event) {
        if (usnom.getText().isEmpty() || usprenom.getText().isEmpty() || usadresse.getText().isEmpty()
                || usemail.getText().isEmpty() || ustelephone.getText().isEmpty() || usrib.getText().isEmpty()
                || usstatut.getText().isEmpty() || uscnam.getText().isEmpty()
                || usnaissance.getValue() == null || usembauche.getValue() == null
                || (!usmale.isSelected() && !usfemale.isSelected())
                || (!usmarie.isSelected() && !uscelibataire.isSelected() && !usdivorce.isSelected())) {
            showAlert("Champs vides", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        if (!usemail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            showAlert("Email invalide", "Veuillez saisir une adresse e-mail valide.");
            return;
        }

        if (!ustelephone.getText().matches("\\d{8}")) {
            showAlert("Téléphone invalide", "Veuillez saisir un numéro de téléphone valide (8 chiffres).");
            return;
        }

        int nombreEnfant;
        try {
            nombreEnfant = Integer.parseInt(usenfant.getText());
            if (nombreEnfant < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert("Nombre invalide", "Veuillez saisir un nombre d'enfants valide (entier positif).");
            return;
        }

        if (!usnom.getText().matches("^[A-Za-zÀ-ÿ\\s'-]{2,}$")) {
            showAlert("Nom invalide", "Le nom ne doit contenir que des lettres et faire au moins 2 caractères.");
            return;
        }

        if (!usprenom.getText().matches("^[A-Za-zÀ-ÿ\\s'-]{2,}$")) {
            showAlert("Prénom invalide", "Le prénom ne doit contenir que des lettres et faire au moins 2 caractères.");
            return;
        }

        LocalDate dateNaissance = usnaissance.getValue();
        LocalDate dateEmbauche = usembauche.getValue();
        LocalDate aujourdHui = LocalDate.now();

        if (dateNaissance.isAfter(aujourdHui)) {
            showAlert("Date de naissance invalide", "La date de naissance ne peut pas être dans le futur.");
            return;
        }

        int age = Period.between(dateNaissance, aujourdHui).getYears();
        if (age < 18) {
            showAlert("Âge invalide", "L'utilisateur doit avoir au moins 18 ans.");
            return;
        }

        if (dateEmbauche.isAfter(aujourdHui)) {
            showAlert("Date d'embauche invalide", "La date d'embauche ne peut pas être dans le futur.");
            return;
        }

        if (dateEmbauche.isBefore(dateNaissance)) {
            showAlert("Incohérence des dates", "La date d'embauche ne peut pas être antérieure à la date de naissance.");
            return;
        }

        String genre = usmale.isSelected() ? "Homme" : "Femme";
        String situationFamiliale = usmarie.isSelected() ? "Marié" : uscelibataire.isSelected() ? "Célibataire" : "Divorcé";

        User user = new User();
        user.setId(selectedUserId); // important pour l'update
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
        user.setPhotoProfil("default.png");
        user.setGenre(genre);
        user.setSituationFamiliale(situationFamiliale);
        user.setDateNaissance(dateNaissance);
        user.setDateEmbauche(dateEmbauche);

        UserCrud crud = new UserCrud();
        crud.modifierUser(user);

        showAlert("Modification Réussie", "L'utilisateur a été modifié avec succès !");
        selectedUserId = -1; // reset
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CrudUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserData(User user) {
        selectedUserId = user.getId();
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

        if ("homme".equalsIgnoreCase(user.getGenre())) {
            usmale.setSelected(true);
        } else {
            usfemale.setSelected(true);
        }

        switch (user.getSituationFamiliale().toLowerCase()) {
            case "marié": case "mariée":
                usmarie.setSelected(true); break;
            case "divorcé": case "divorcée":
                usdivorce.setSelected(true); break;
            case "célibataire":
                uscelibataire.setSelected(true); break;
        }
    } @FXML
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
}