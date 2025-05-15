package gui;

import entities.*;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
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
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import services.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
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
    @FXML
    private ComboBox<String> comboTitre;
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
    private Map<String, Integer> titresPersonnalises = new HashMap<>();
    private UserService userService = new UserService();
    private final PauseTransition pause = new PauseTransition(Duration.seconds(5));
    String titre;
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
    @FXML
    public void initialize() {
        prioriteGroup = new ToggleGroup();
        rbUrgent.setToggleGroup(prioriteGroup);
        rbImportant.setToggleGroup(prioriteGroup);
        rbNormal.setToggleGroup(prioriteGroup);
        comboTitre.getItems().addAll(
                "Ouvrier", "Technicien", "Agent de production", "Agent de nettoyage",
                "Agent de sécurité", "PDG", "Directeur général", "Directeur RH",
                "Directeur administratif", "Superviseur", "Chef service", "Chef équipe", "Autre"
        );

        tftitre.setVisible(false);
        comboTitre.setOnAction(event -> {
            String selected = comboTitre.getValue();
            tftitre.setVisible("Autre".equals(selected));
        });

    }
    private String getPriorite() {
        RadioButton selectedRadioButton = (RadioButton) prioriteGroup.getSelectedToggle();

        if (selectedRadioButton != null) {
            return selectedRadioButton.getText();
        }

        return "Aucune";
    }
    String texteCensuré ;
    @FXML
    void ajouterreclamations(ActionEvent event) {
        if ("Autre".equals(comboTitre.getValue())) {
            titre = tftitre.getText();
        } else {
            titre = comboTitre.getValue();
        }
        PurgoMalumService purgoMalumService = new PurgoMalumService();

        String texteOriginal = boxtext.getText();
        String texteCensuré = "";

        try {
            texteCensuré = purgoMalumService.checkAndCensorProfanity(texteOriginal);
            System.out.println("Texte censuré : " + texteCensuré);
            boxtext.setText(texteCensuré);

            if (!texteOriginal.equals(texteCensuré)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Contenu inapproprié");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez éviter d’utiliser des mots vulgaires ou insultants dans votre texte.");
                alert.showAndWait();
                return;
            }

            boxtext.setText(texteCensuré);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (titre.isEmpty() || boxtext.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champ(s) vide(s)");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs avant de soumettre la réclamation !");
            alert.showAndWait();
            return;
        }

        LocalDate date = LocalDate.now();
        String statut = "En attente";
        String priorite = getPriorite();
        String RecevoirNotifications = String.join(",", getNotifications());
        int userId = 4 ;



        if (titre.isEmpty() || boxtext.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champ(s) vide(s)");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs avant de soumettre la réclamation !");
            alert.showAndWait();
            return; // Arrêter l'exécution ici
        }

        Reclamations reclamation = new Reclamations(0, titre, texteOriginal, date, statut, cheminPieceJointe , priorite, RecevoirNotifications , userId  );
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
        if ("Autre".equals(comboTitre.getValue())) {
            titre = tftitre.getText().trim();

            if (!titre.isEmpty()) {
                // Incrémenter le compteur du titre personnalisé
                titresPersonnalises.put(titre, titresPersonnalises.getOrDefault(titre, 0) + 1);

                // Si ce titre a été utilisé 3 fois ou plus, on l'ajoute à la ComboBox
                if (titresPersonnalises.get(titre) == 3 && !comboTitre.getItems().contains(titre)) {
                    comboTitre.getItems().add(comboTitre.getItems().size() - 1, titre); // juste avant "Autre"
                    System.out.println("Titre personnalisé ajouté à la liste : " + titre);
                }
            }
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
    private ListeReclamations controllerPrincipal;

    public void setControllerPrincipal(ListeReclamations controllerPrincipal) {
        this.controllerPrincipal = controllerPrincipal;
    }

    private final CorrectionService correctionService = new CorrectionService();



//    private final CorrectionService correctionService = new CorrectionService();

    @FXML
    private void corrigerTexte() {
        String texteOriginal = boxtext.getText();

        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                CorrectionServiceLocal correctionService = new CorrectionServiceLocal();
                return correctionService.corrigerTexte(texteOriginal);
            }
        };

        task.setOnSucceeded(e -> boxtext.setText(task.getValue()));
        task.setOnFailed(e -> task.getException().printStackTrace());

        new Thread(task).start();
    }


}


