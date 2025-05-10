package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class AttestationFormController {

    @FXML
    private ComboBox<String> comboTypeAttestation;

    @FXML
    private TextField textObjet;

    @FXML
    private TextArea textMotif;

    @FXML
    private Hyperlink hyperlinkPieceJointe;

    private File fichierJoint;

    @FXML
    public void initialize() {
        // Remplir le comboBox avec des types d’attestation courants
        comboTypeAttestation.getItems().addAll(
                "Attestation de travail",
                "Attestation de salaire",
                "Attestation de stage"
        );
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
    @FXML
    void ajoute(ActionEvent event) {
        String type = comboTypeAttestation.getValue();  // Type d'attestation
        String objet = textObjet.getText().trim();
        String motif = textMotif.getText().trim();
        LocalDate dateSoumission = LocalDate.now();  // Date actuelle
        String statut = "en attente";  // Statut par défaut

        // Vérification des champs obligatoires
        if (type == null) {
            showAlert("Le type d'attestation est obligatoire.");
            return;
        }

        if (objet.isEmpty()) {
            showAlert("L'objet est obligatoire.");
            return;
        }

        if (motif.isEmpty()) {
            showAlert("Le motif est obligatoire.");
            return;
        }

        if (objet.length() < 3) {
            showAlert("L'objet doit comporter au moins 3 caractères.");
            return;
        }

        if (fichierJoint == null) {
            showAlert("Veuillez ajouter une pièce jointe.");
            return;
        }

        // Connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/workbridge";
        String username = "root";
        String password = "";

        String query = "INSERT INTO demande (type, objet, description, date_soumission, statut, fichier) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, type); // ici, "type" est le type d'attestation
            stmt.setString(2, objet);
            stmt.setString(3, motif); // motif est enregistré comme description
            stmt.setDate(4, Date.valueOf(dateSoumission));
            stmt.setString(5, statut);
            stmt.setString(6, fichierJoint.getAbsolutePath());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Demande d'attestation envoyée avec succès !");
            } else {
                showAlert("Une erreur est survenue lors de l'ajout de la demande.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur de base de données : " + e.getMessage());
        }

        // Nettoyer le formulaire après ajout
        clearForm();
    }


    // Afficher la liste des demandes (fonctionnalité supplémentaire)
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
        comboTypeAttestation.setValue(null);
        textObjet.clear();
        textMotif.clear();
        fichierJoint = null;
        hyperlinkPieceJointe.setText("Aucun fichier sélectionné");
    }
}
