package gui;

import entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ReclamationService;
import services.UserService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AjouterReclamations {

    private final ReclamationService reclamationService = new ReclamationService();
    @FXML
    private TextField tftitre;
    @FXML
    private TextArea boxtext;

    @FXML
    private Hyperlink hyperlinkPieceJointe;


    private String cheminPieceJointe ;
    @FXML
    private CheckBox cbEmail;

    @FXML
    private CheckBox cbSMS;

    @FXML
    private RadioButton rbImportant;

    @FXML
    private RadioButton rbNormal;

    @FXML
    private RadioButton rbUrgent;

    private UserService userService = new UserService();



    // Méthode pour récupérer les notifications sélectionnées
    private List<String> getNotifications() {
        List<String> notifications = new ArrayList<>();

        if (cbEmail.isSelected()) {
            notifications.add("Email");
        }
        if (cbSMS.isSelected()) {
            notifications.add("SMS");
        }

        return notifications;
    }

    // Méthode pour récupérer la priorité sélectionnée
    private String getPriorite() {
        if (rbUrgent.isSelected()) {
            return "Urgent";
        } else if (rbImportant.isSelected()) {
            return "Important";
        } else if (rbNormal.isSelected()) {
            return "Normal";
        }
        return "Aucune";  // Si aucune priorité n'est sélectionnée
    }

    @FXML
    void ajouterreclamations(ActionEvent event) {
        String titre = tftitre.getText();
        String description = boxtext.getText();
        LocalDate date = LocalDate.now();
        String statut = "En attente";
        String priorite = getPriorite();
        String RecevoirNotifications = String.join(",", getNotifications());


        if (titre.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champ(s) vide(s)");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs avant de soumettre la réclamation !");
            alert.showAndWait();
            return; // Arrêter l'exécution ici
        }


        // Crée une réclamation avec le chemin de la pièce jointe
        Reclamations reclamation = new Reclamations(0, titre, description, date, statut, cheminPieceJointe , priorite, RecevoirNotifications  );

        try {
            reclamationService.create(reclamation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Réclamation ajoutée avec succès !");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ajout : " + e.getMessage());
            alert.showAndWait();
        }
    }
    @FXML
    void ajoutpiece(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        Stage stage = (Stage) tftitre.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            cheminPieceJointe = file.getAbsolutePath();
            hyperlinkPieceJointe.setText(file.getName()); // Affiche juste le nom du fichier
            hyperlinkPieceJointe.setOnAction(e -> {
                try {
                    java.awt.Desktop.getDesktop().open(file); // Ouvre le fichier avec le programme associé
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            System.out.println("Fichier sélectionné : " + cheminPieceJointe);
        }
    }
    @FXML
    void afficherreclamations(ActionEvent event) {
        try {
        // Récupérer l'utilisateur avec l'ID 4 (ici pour les tests, à remplacer par l'utilisateur authentifié)
        User user = userService.getUserById(4);

        // Créer la réclamation avec les informations déjà présentes
        Reclamations reclamation = new Reclamations(0, tftitre.getText(), boxtext.getText(), LocalDate.now(), "En attente", cheminPieceJointe, getPriorite(), String.join(",", getNotifications())); // ID de l'utilisateur (4)

        // Charger le fichier FXML de la page de détails
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detailreclamation.fxml"));
        Parent root = loader.load();  // Charger le FXML et obtenir le root de la scène

        // Passer la réclamation et l'utilisateur au contrôleur de la page de détails
        Afficherdetail afficherdetailController = loader.getController();
        afficherdetailController.setReclamation(reclamation, user);  // Passer la réclamation et l'utilisateur

        // Changer la scène pour afficher les détails de la réclamation
        boxtext.getScene().setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText("Erreur lors de l'affichage de la réclamation : " + e.getMessage());
        alert.showAndWait();
    }

    } }


