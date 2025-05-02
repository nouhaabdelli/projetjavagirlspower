package gui;

import entities.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.ReclamationService;
import services.UserService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.ToggleGroup;

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
    @FXML
    private Button btnAjouter;
    @FXML
    private ToggleGroup prioriteGroup;

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
    @FXML
    public void initialize() {
        // Créer un ToggleGroup
        prioriteGroup = new ToggleGroup();

        // Ajouter les RadioButton au ToggleGroup
        rbUrgent.setToggleGroup(prioriteGroup);
        rbImportant.setToggleGroup(prioriteGroup);
        rbNormal.setToggleGroup(prioriteGroup);
    }

    private String getPriorite() {
        RadioButton selectedRadioButton = (RadioButton) prioriteGroup.getSelectedToggle();

        if (selectedRadioButton != null) {
            return selectedRadioButton.getText();
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
        int userId = 4 ;

        if (titre.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champ(s) vide(s)");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs avant de soumettre la réclamation !");
            alert.showAndWait();
            return; // Arrêter l'exécution ici
        }
        Reclamations reclamation = new Reclamations(0, titre, description, date, statut, cheminPieceJointe , priorite, RecevoirNotifications , userId  );
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
            User user = userService.getUserById(4);  // À adapter selon utilisateur connecté
            Reclamations reclamation = new Reclamations(
                    0,
                    tftitre.getText(),
                    boxtext.getText(),
                    LocalDate.now(),
                    "En attente",
                    cheminPieceJointe,
                    getPriorite(),
                    String.join(",", getNotifications()),
                    user.getId()
            );

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detailreclamation.fxml"));
            Parent root = loader.load();

            Afficherdetail afficherdetailController = loader.getController();
            afficherdetailController.setReclamation(reclamation, user);

            // Ouvre une nouvelle fenêtre proprement
            Stage stage = new Stage();
            stage.setTitle("Détails de la Réclamation");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'affichage de la réclamation : " + e.getMessage());
            alert.showAndWait();
        }
    }

    public void chargerDonneesPourModification(Reclamations reclamation) {
        tftitre.setText(reclamation.getTitre());
        boxtext.setText(reclamation.getDescription());


        switch (reclamation.getPriorite()) {
            case "Important":
                rbImportant.setSelected(true);
                break;
            case "Normal":
                rbNormal.setSelected(true);
                break;
            case "Urgent":
                rbUrgent.setSelected(true);
                break;
        }

        List<String> notifs = Arrays.asList(reclamation.getRecevoirNotifications().split(","));
        if (notifs.contains("Email")) cbEmail.setSelected(true);
        if (notifs.contains("SMS")) cbSMS.setSelected(true);


        if (reclamation.getCheminPieceJointe() != null && !reclamation.getCheminPieceJointe().isEmpty()) {
            cheminPieceJointe = reclamation.getCheminPieceJointe();
            Path path = Paths.get(cheminPieceJointe);
            hyperlinkPieceJointe.setText(path.getFileName().toString());

            // Ajout de l'action pour ouvrir le fichier
            hyperlinkPieceJointe.setOnAction(event -> {
                File file = new File(cheminPieceJointe);
                if (file.exists()) {
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Le fichier n'existe pas.");
                }
            });

        } else {
            hyperlinkPieceJointe.setText("Aucune pièce jointe");
            hyperlinkPieceJointe.setOnAction(null); // Pas d'action si pas de fichier
        }

        btnAjouter.setText("Modifier");
        btnAjouter.setOnAction(event -> {
            try {
                // Appel à une méthode update au lieu de create
                reclamationService.update(new Reclamations(
                        reclamation.getId(),
                        tftitre.getText(),
                        boxtext.getText(),
                        LocalDate.now(),
                        "En attente",
                        cheminPieceJointe,
                        getPriorite(),
                        String.join(",", getNotifications()) ,
                        4
                ));



            } catch (Exception e) {
                e.printStackTrace();
            }
            if (controllerPrincipal != null) {
                controllerPrincipal.rafraichirTable();
            }
            // Fermer la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
    }
    // hathaa lel auctualiser liste//
    private ListeReclamations controllerPrincipal;

    public void setControllerPrincipal(ListeReclamations controllerPrincipal) {
        this.controllerPrincipal = controllerPrincipal;
    }
}


