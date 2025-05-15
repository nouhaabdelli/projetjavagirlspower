package gui;

import entities.Certificat;
import  entities.Formation;
import  entities.User;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.Certificatservices;
import services.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import utils.MyConnection;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.sql.PreparedStatement;


public class CertificatAddController {

    @FXML private TextField titreField, validiteField, usernameField;
    @FXML private TextArea descriptionField;
    @FXML private ComboBox<String> niveauComboBox;
    @FXML private ComboBox<String> userComboBox;
    @FXML private ComboBox<String> formationComboBox;
    @FXML private DatePicker dateExpirationPicker;
    @FXML private AnchorPane rec;

    private final ObservableList<Certificat> certificatList = FXCollections.observableArrayList();
    private final Certificatservices certificatService = new Certificatservices();
    private final Map<String, Integer> userMap = new HashMap<>();
    private final Map<String, Integer> formationMap = new HashMap<>();
    private final FormationService formationService = new FormationService();
    @FXML
    private AnchorPane rootPane;

    @FXML
    public void initialize() throws SQLException {
        loadNiveauComboBox();
        loadUsers();
        loadFormations();
        loadData();
    }

    private void loadFormations() throws SQLException {
        formationMap.clear();
        formationComboBox.getItems().clear();
        List<Formation> formations = formationService.afficherFormations();
        for (Formation formation : formations) {
            formationMap.put(formation.getTitre(), formation.getIdFormation());
            formationComboBox.getItems().add(formation.getTitre());
        }
    }

    private void loadUsers() throws SQLException {
        userMap.clear();
        userComboBox.getItems().clear();
        List<User> users = certificatService.getAllUsers();
        for (User user : users) {
            userMap.put(user.getNom(), user.getId());
            userComboBox.getItems().add(user.getUsername());
        }
    }

    private void loadNiveauComboBox() {
        niveauComboBox.setItems(FXCollections.observableArrayList("Débutant", "Intermédiaire", "Avancé"));
    }

    private void loadData() throws SQLException {
        certificatList.clear();
        certificatList.addAll(certificatService.afficherCertificats());
    }

    @FXML
    private void onUserSelected() {
        String selectedUsername = userComboBox.getValue();
        usernameField.setText(selectedUsername != null ? selectedUsername : "");
    }

    @FXML
    private void addCertificat() throws SQLException, IOException {
        Certificat cert = getCertificatFromForm();
        cert.setUsername(userComboBox.getValue());

        certificatService.ajouterCertificat(cert);
        showNotification("Succès", "Certificat affecté à " + cert.getUsername(), NotificationType.SUCCESS);

        loadData();
        clearFields();
        senemail(cert);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Certificats.fxml")); // Mets ici le bon chemin
            Parent certificatListView = loader.load();

            // Remplacement de l’interface actuelle par celle de la liste
            rootPane.getChildren().setAll(certificatListView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void senemail(Certificat certificat) {
        try {
            Connection cnx = MyConnection.getInstance().getConnection();
            String req = "SELECT email, nom FROM user WHERE id = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, certificat.getUserId());
            ResultSet rs = ps.executeQuery();

            String recipientEmail = "";
            String recipientName = "";

            if (rs.next()) {
                recipientEmail = rs.getString("email");
                recipientName = rs.getString("nom");
            } else {
                System.out.println("Aucun utilisateur trouvé !");
                return;
            }

            // Utilise un mot de passe d’application sécurisé
            final String from = "hadilfatnassi14@gmail.com";
            final String password = "hutl tlon eytm kmld"; // mot de passe d'application (PAS ton vrai mot de passe)
            final String username = from;

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Votre certificat a été généré !");

            String htmlContent = "<html><body style='font-family:Arial;'>"
                    + "<h2>Bonjour " + recipientName + ",</h2>"
                    + "<p>Voici les détails de votre certificat :</p>"
                    + "<ul>"
                    + "<li><strong>Titre :</strong> " + certificat.getTitre() + "</li>"
                    + "<li><strong>Description :</strong> " + certificat.getDescription() + "</li>"
                    + "<li><strong>Niveau :</strong> " + certificat.getNiveau() + "</li>"
                    + "<li><strong>Validité :</strong> " + certificat.getValiditeAnnees() + " année(s)</li>"
                    + "<li><strong>Date d'expiration :</strong> " + certificat.getDateExpiration() + "</li>"
                    + "</ul><br><p>Cordialement,<br>L'équipe de gestion des certificats</p>"
                    + "</body></html>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Email envoyé à : " + recipientEmail);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("L'email a bien été envoyé à " + recipientEmail);
            alert.show();

        } catch (MessagingException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur d'envoi");
            errorAlert.setHeaderText("Erreur de messagerie");
            errorAlert.setContentText("Échec de l'envoi de l'email. Vérifie tes paramètres SMTP.");
            errorAlert.show();

        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert sqlAlert = new Alert(Alert.AlertType.ERROR);
            sqlAlert.setTitle("Erreur SQL");
            sqlAlert.setHeaderText("Erreur base de données");
            sqlAlert.setContentText("Impossible de récupérer les informations de l'utilisateur.");
            sqlAlert.show();
        }
    }


    private Certificat getCertificatFromForm() {
        String username = userComboBox.getValue();
        String formationTitre = formationComboBox.getValue();

        int userId = userMap.getOrDefault(username, -1);
        int formationId = formationMap.getOrDefault(formationTitre, -1);

        return new Certificat(
                titreField.getText(),
                descriptionField.getText(),
                dateExpirationPicker.getValue().toString(),
                niveauComboBox.getValue(),
                Integer.parseInt(validiteField.getText()),
                username,
                userId,
                formationId
        );
    }

    private void clearFields() {
        titreField.clear();
        descriptionField.clear();
        niveauComboBox.setValue(null);
        dateExpirationPicker.setValue(null);
        validiteField.clear();
        userComboBox.setValue(null);
        formationComboBox.setValue(null);
        usernameField.clear();
    }

    private void showNotification(String title, String message, NotificationType type) {
        Image img = new Image(getClass().getResourceAsStream("/images/okay.png"));
        ImageView icon = new ImageView(img);
        icon.setFitWidth(30);
        icon.setFitHeight(30);

        Notifications notification = Notifications.create()
                .title(title)
                .text(message)
                .graphic(icon)
                .hideAfter(Duration.seconds(4))
                .position(Pos.TOP_RIGHT)
                .onAction(e -> System.out.println("Notification clicked!"));

        notification.show();
    }

    enum NotificationType {
        SUCCESS, ERROR, WARNING
    }

    @FXML
    public void addCertificat(Certificat certificat) {
        titreField.setText(certificat.getTitre());
        descriptionField.setText(certificat.getDescription());
        niveauComboBox.setValue(certificat.getNiveau());
        validiteField.setText(String.valueOf(certificat.getValiditeAnnees()));
        dateExpirationPicker.setValue(LocalDate.parse(certificat.getDateExpiration()));
        userComboBox.setValue(certificat.getUsername());
        formationComboBox.setValue(
                formationMap.entrySet().stream()
                        .filter(entry -> Objects.equals(entry.getValue(), certificat.getFormationid()))
                        .map(Map.Entry::getKey)
                        .findFirst().orElse(null)
        );
        try {
            // Charger la nouvelle interface (certificats.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/certificats.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Liste des certificats");
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle (celle du formulaire)
            // Pour cela, il faut récupérer la fenêtre à partir d’un élément de ta scène
            Stage currentStage = (Stage) titreField.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            // Eventuellement afficher une alerte d’erreur
        }
    }

}
