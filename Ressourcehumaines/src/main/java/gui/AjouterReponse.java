package gui;

import entities.Reponses;
import entities.Reclamations;
import entities.user;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.*;
import utils.MyConnection;

import java.security.cert.PolicyNode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class AjouterReponse {
    @FXML

    private Button btnAjouter;

    @FXML
    private ComboBox<String> comboImportance;

    @FXML
    private Label lblContenuReclamation;

    @FXML
    private Label lblDateReclamation;

    @FXML
    private Label lblFichier;

    @FXML
    private Label lblPrioriteReclamation;
    @FXML
    private VBox formulaireAjoutPane;

    @FXML
    private Label lblTitreReclamation;
    @FXML
    private Hyperlink linkFichierJoint;
    private String cheminFichier;
    @FXML
    private TableView<Reponses> tableViewReponses;
    @FXML
    private TextArea txtContenu;
    @FXML
    private Button quitter;

    private Reclamations reclamation;
    private File fichierJoint;


    private Reponses reponseAModifier;


    private int reclamationId;
    public void setReclamationId(int id) {
        this.reclamationId = id;
    }


    @FXML
    private void ouvrirFichierJoint() {
        if (cheminFichier != null && !cheminFichier.isEmpty()) {
            try {
                File file = new File(cheminFichier);
                if (file.exists()) {
                    Desktop.getDesktop().open(file);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Fichier introuvable", "Le fichier n'existe pas à ce chemin : " + cheminFichier);
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir le fichier.");
            }
        }
    }

    public void setReclamation(Reclamations r) {
        this.reclamation = r;

        lblTitreReclamation.setText("Titre   :  " + r.getTitre());
        lblContenuReclamation.setText("Contenu  : " + r.getDescription());
        if (reclamation.getDateDemande() != null) {
            lblDateReclamation.setText(reclamation.getDateDemande().toString());
        } else {
            lblDateReclamation.setText("Date non disponible");
        }        lblPrioriteReclamation.setText("Priorité  : " + r.getPriorite());
        cheminFichier = r.getCheminPieceJointe();

        if (cheminFichier != null && !cheminFichier.isEmpty()) {
            linkFichierJoint.setText("📎FichierJoint : " + new File(cheminFichier).getName());
            linkFichierJoint.setVisible(true);
            linkFichierJoint.setDisable(false); // au cas où
        } else {
            linkFichierJoint.setText("Aucun fichier joint");
            linkFichierJoint.setDisable(true);
        }

    }

    @FXML
    void parcourirFichier(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fichierJoint = fileChooser.showOpenDialog(null);
        if (fichierJoint != null) {
            lblFichier.setText(fichierJoint.getAbsolutePath());
        }
    }

    @FXML
    void RepondreReclamation(ActionEvent event) {
        String contenu = txtContenu.getText();
        String priorite = (comboImportance.getValue() != null) ? comboImportance.getValue().toString() : "";

        if (contenu.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Erreur", "Le contenu de la réponse ne peut pas être vide!");
            return;
        }

        if (priorite.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Erreur", "Veuillez sélectionner une priorité !");
            return;
        }

        String cheminFichier = (fichierJoint != null) ? fichierJoint.getAbsolutePath() : (reponseAModifier != null ? reponseAModifier.getFichierJoint() : null);


        ReponseService service = new ReponseService();

        try {
            if (reponseAModifier != null) {

                Reponses r = new Reponses(1, contenu, LocalDate.now(), null, priorite, cheminFichier);
                r.setReclamationId(reclamationId);  // Assure-toi que reclamationId est bien défini ici


                // Mode modification
                reponseAModifier.setContenu(contenu);
                reponseAModifier.setPriorite(priorite);
                reponseAModifier.setFichierJoint(cheminFichier);
                service.update(reponseAModifier);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Réponse modifiée avec succès !");
                quitter.setVisible(false); // (peut être facultatif ici si déjà visible)


            } else {

                Reponses r = new Reponses(1, contenu, LocalDate.now(), null, priorite, cheminFichier , reclamationId);

                // Mode ajout
                r.setReclamationId(reclamationId);
                service.create(r);
                ReclamationService rs = new ReclamationService();
                rs.setStatutTraite(reclamationId);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Réponse ajoutée avec succès !");
                reclamation.setStatut("traitée");

                UserService userService = new UserService();
                user user = userService.getUserById(reclamation.getUserId());
                if (user != null) {
                    String typeNotif = reclamation.getRecevoirNotifications(); // Exemple : "email", "sms", ou "both"

                    if ("Email".equalsIgnoreCase(typeNotif) ) {
                        sendReclamationStatusEmail(user, reclamation);
                    }

                    if ("SMS".equalsIgnoreCase(typeNotif) ) {
                        sendReclamationStatusSMS(user, reclamation);
                    }
                    if ("Email,SMS".equalsIgnoreCase(typeNotif) ) {
                        sendReclamationStatusSMS(user, reclamation);
                        sendReclamationStatusEmail(user, reclamation);

                    }

                }


            }
            txtContenu.clear();
            comboImportance.setValue(null);
            fichierJoint = null;
            reponseAModifier = null;
            btnAjouter.setText("ajouter");
            formulaireAjoutPane.setVisible(false); // ⚠️ Assure-toi que c’est bien le nom de ton Pane
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue : " + e.getMessage());
        }

    }


    private void sendReclamationStatusSMS(user user, Reclamations reclamation) {
        String phone = user.getNumTelephone();
        if (phone != null && !phone.trim().isEmpty()) {
            String smsBody = "Votre réclamation du " + reclamation.getDateDemande() + " a été traitée. Merci. - RH";
            System.out.println("Tentative d'envoi SMS à : " + phone);  // <--- pour vérifier
//            SmsService.sendSms(phone, smsBody);
        } else {
            System.out.println("Numéro de téléphone invalide ou vide.");
        }
    }



    public user getUserById(int userId) {
        user user = null;
        try {
            Connection conn = MyConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE id = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new user();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                // ajoute d'autres champs si nécessaire
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private void sendReclamationStatusEmail(user user, Reclamations reclamation) {
        System.out.println("sendReclamationStatusEmail called");

        if ("traitée".equalsIgnoreCase(reclamation.getStatut())) {
            EmailService emailService = new EmailService();
            String subject = "Votre réclamation a été traitée";
            String body = "Bonjour " + user.getNom() + " " + user.getPrenom() + ",\n\n"
                    + "Nous vous informons que votre réclamation n°" + reclamation.getId()
                    + " intitulée '" + reclamation.getTitre() + "' a été traitée.\n\n"
                    + "Cordialement,\nL'équipe RH";

            emailService.sendEmail(user.getEmail(), subject, body);
        } else {
            System.out.println("Statut non traité, aucun e-mail envoyé.");
        }
    }


    @FXML
    void annulerModification(ActionEvent event) {
        txtContenu.clear();
        comboImportance.setValue(null);
        fichierJoint = null;
        reponseAModifier = null;

        btnAjouter.setText("Modifier");
        formulaireAjoutPane.setVisible(false);
        Stage stage = (Stage) btnAjouter.getScene().getWindow();  // Récupérer le Stage actuel
        stage.close();  // Fermer la fenêtre actuelle

    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void activerModeModification(Reponses reponse) {

        this.reponseAModifier = reponse;
        setReclamationId(reponse.getReclamationId());

        System.out.println("Reclamation ID récupéré depuis la table : " + reponse.getReclamationId());

        txtContenu.setText(reponse.getContenu());
        reponse.setDateModification(LocalDate.now());
        comboImportance.setValue(reponse.getPriorite());
        String cheminFichier = reponse.getFichierJoint();
        if (cheminFichier != null && !cheminFichier.isEmpty()) {
        File fichier = new File(cheminFichier);
        lblFichier.setText(fichier.getName());
          } else {
        lblFichier.setText("Aucun fichier");
             }
        formulaireAjoutPane.setVisible(true);
        btnAjouter.setText("Modifier");
        ReclamationService rs = new ReclamationService();
        try {
            List<Reclamations> toutesReclamations = rs.readAll();
            Reclamations reclamationTrouvee = null;
            for (Reclamations r : toutesReclamations) {
                if (r.getId() == reponse.getReclamationId()) {
                    reclamationTrouvee = r;
                    System.out.println(reclamationTrouvee);
                    break;
                }
            }
            if (reclamationTrouvee != null) {
                setReclamation(reclamationTrouvee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
//    public void rafraichirTableReponses() {
//        try {
//            ReponseService reponseService = new ReponseService();
//            List<Reponses> reponsesList = reponseService.readAll();
//            ObservableList<Reponses> observableList = FXCollections.observableArrayList(reponsesList);
//            tableViewReponses.setItems(observableList);
//            tableViewReponses.refresh(); // Force la mise à jour de la vue
//        } catch (SQLException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Erreur");
//            alert.setContentText("Erreur lors du rafraîchissement des réponses : " + e.getMessage());
//            alert.showAndWait();
//        }
//    }







}