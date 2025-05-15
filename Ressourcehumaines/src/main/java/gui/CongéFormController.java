package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class CongéFormController {

    @FXML
    private ComboBox<String> comboType;

    @FXML
    private TextField textObjet;

    @FXML
    private TextArea boxtext;

    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;

    @FXML
    private Hyperlink hyperlinkPieceJointe;

    private File fichierJoint;

    // Initialiser la ComboBox avec les types de congé
    @FXML
    private Label labelJoursOuvres;

    @FXML
    public void initialize() {
        comboType.getItems().addAll("Congé annuel", "Congé maladie", "Congé sans solde");

        dateDebut.valueProperty().addListener((obs, oldV, newV) -> calculerJoursOuvres());
        dateFin.valueProperty().addListener((obs, oldV, newV) -> calculerJoursOuvres());
    }
    private void calculerJoursOuvres() {
        LocalDate debut = dateDebut.getValue();
        LocalDate fin = dateFin.getValue();

        if (debut != null && fin != null && !debut.isAfter(fin)) {
            try {
                int jours = utils.HolidayAPI.getWorkingDays(debut, fin);
                labelJoursOuvres.setText(jours + " jours");
            } catch (IOException e) {
                e.printStackTrace();
                labelJoursOuvres.setText("Erreur calcul");
            }
        } else {
            labelJoursOuvres.setText("0 jours");
        }
    }
    // Ajouter une pièce jointe
    @FXML
    void ajoutpiece(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une pièce jointe");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fichierJoint = selectedFile;
            hyperlinkPieceJointe.setText(selectedFile.getName());
        }
    }

    // Ajouter la demande de congé
    @FXML
    void ajoute(ActionEvent event) {
        String type = "congé";  // Type global fixé
        String typeConge = comboType.getValue();  // Type spécifique
        String objet = textObjet.getText().trim();
        String description = boxtext.getText().trim();
        LocalDate debut = dateDebut.getValue();
        LocalDate fin = dateFin.getValue();
        LocalDate dateSoumission = LocalDate.now();  // Date actuelle
        String statut = "en attente";  // Statut par défaut

        if (typeConge == null) {
            showAlert("Le type de congé est obligatoire.");
            return;
        }

        if (objet.isEmpty()) {
            showAlert("L'objet est obligatoire.");
            return;
        }

        if (description.isEmpty()) {
            showAlert("La description est obligatoire.");
            return;
        }

        if (debut == null) {
            showAlert("La date de début est obligatoire.");
            return;
        }

        if (fin == null) {
            showAlert("La date de fin est obligatoire.");
            return;
        }

        if (debut.isAfter(fin)) {
            showAlert("La date de début ne peut pas être après la date de fin.");
            return;
        }

        if (fichierJoint == null) {
            showAlert("Veuillez ajouter une pièce jointe.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/workbridge";
        String username = "root";
        String password = "";

        String query = "INSERT INTO demande (type, type_conge, objet, description, date_debut, date_fin, date_soumission, statut, fichier) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, type);
            stmt.setString(2, typeConge);
            stmt.setString(3, objet);
            stmt.setString(4, description);
            stmt.setDate(5, Date.valueOf(debut));
            stmt.setDate(6, Date.valueOf(fin));
            stmt.setDate(7, Date.valueOf(dateSoumission));
            stmt.setString(8, statut);
            stmt.setString(9, fichierJoint.getAbsolutePath());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Demande de congé envoyée avec succès !");
            } else {
                showAlert("Une erreur est survenue lors de l'ajout de la demande.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur de base de données : " + e.getMessage());
        }

        clearForm();
    }
    @FXML
    private void retourPagePrecedente(ActionEvent event) {
        try {
            // Change "Dashboard.fxml" par le nom exact de ta page précédente
            Parent root = FXMLLoader.load(getClass().getResource("/Demande.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Afficher la liste des demandes
    @FXML
    void afficherDemandes(ActionEvent event) {
        // Tu peux ici rediriger vers une autre scène ou charger une liste de demandes
        System.out.println("Afficher la liste des demandes");
    }

    // Affichage d'une alerte
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Réinitialiser le formulaire
    private void clearForm() {
        comboType.setValue(null);
        textObjet.clear();
        boxtext.clear();
        dateDebut.setValue(null);
        dateFin.setValue(null);
        fichierJoint = null;
        hyperlinkPieceJointe.setText("Aucun fichier sélectionné");
    }
}