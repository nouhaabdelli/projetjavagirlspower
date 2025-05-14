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
import entities.Attestation;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class ModifierAttestationFormController {

    @FXML
    private ComboBox<String> comboTypeAttestation;

    @FXML
    private TextField textObjet;

    @FXML
    private TextArea textMotif;

    @FXML
    private Hyperlink hyperlinkPieceJointe;

    @FXML
    private Label labelStatut;

    private File fichierJoint;
    private int demandeId; // Pour stocker l'ID de la demande à modifier
    private Attestation attestation;

    @FXML
    public void initialize() {
        // Remplir le comboBox avec des types d'attestation courants
        comboTypeAttestation.getItems().addAll(
                "Attestation de travail",
                "Attestation de salaire",
                "Attestation de stage"
        );
    }

    public void setAttestation(Attestation attestation) {
        this.attestation = attestation;
        if (attestation != null) {
            // Remplir les champs avec les données de l'attestation
            comboTypeAttestation.setValue(attestation.getTypeAttestation());
            textObjet.setText(attestation.getObjet());
            textMotif.setText(attestation.getMotif());
            labelStatut.setText(attestation.getStatut());
            
            // Gérer le fichier joint
            String cheminFichier = attestation.getFichier();
            if (cheminFichier != null && !cheminFichier.isEmpty()) {
                fichierJoint = new File(cheminFichier);
                hyperlinkPieceJointe.setText(fichierJoint.getName());
            }
            
            // Stocker l'ID de la demande
            this.demandeId = attestation.getId();
        }
    }

    // Méthode pour charger les données de l'attestation
    public void chargerAttestation(int id) {
        this.demandeId = id;
        String url = "jdbc:mysql://localhost:3306/workbridge";
        String username = "root";
        String password = "";

        String query = "SELECT * FROM demande WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Remplir les champs avec les données de la base
                comboTypeAttestation.setValue(rs.getString("type_attestation"));
                textObjet.setText(rs.getString("objet"));
                textMotif.setText(rs.getString("motif"));
                labelStatut.setText(rs.getString("statut"));

                // Gérer le fichier joint
                String cheminFichier = rs.getString("fichier");
                if (cheminFichier != null && !cheminFichier.isEmpty()) {
                    fichierJoint = new File(cheminFichier);
                    hyperlinkPieceJointe.setText(fichierJoint.getName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors du chargement des données : " + e.getMessage());
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

    @FXML
    private void retourPagePrecedente(ActionEvent event) {
        try {
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
    void modifier(ActionEvent event) {
        String typeAttestation = comboTypeAttestation.getValue();
        String objet = textObjet.getText().trim();
        String motif = textMotif.getText().trim();

        // Vérification des champs obligatoires
        if (typeAttestation == null) {
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

        // Connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/workbridge";
        String username = "root";
        String password = "";

        String query = "UPDATE demande SET type_attestation = ?, objet = ?, description = ?, " +
                      "motif = ?, fichier = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, typeAttestation);
            stmt.setString(2, objet);
            stmt.setString(3, objet); // Description = objet
            stmt.setString(4, motif);
            stmt.setString(5, fichierJoint != null ? fichierJoint.getAbsolutePath() : null);
            stmt.setInt(6, demandeId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Demande d'attestation modifiée avec succès !");
                retourPagePrecedente(event);
            } else {
                showAlert("Aucune modification n'a été effectuée.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur lors de la modification : " + e.getMessage());
        }
    }

    // Affichage d'une alerte
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
