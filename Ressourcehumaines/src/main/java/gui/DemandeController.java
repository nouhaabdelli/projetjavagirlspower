package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class DemandeController {

    @FXML
    private VBox demandeSection; // Cette VBox contient les boutons et les informations des demandes

    @FXML
    private Button btnConge;

    @FXML
    private Button btnAttestation;

    @FXML
    private Button btnMesDemandes;

    // Méthode pour gérer la demande de congé
    @FXML
    private void handleCongé() {
        System.out.println("Demande Congé clicked");

        // Charger le formulaire de demande de congé (dans un autre scene ou un autre formulaire)
        // Exemple : Redirection vers un autre formulaire (en fonction de ton application)
        loadForm("/CongéForm.fxml");
    }

    // Méthode pour gérer la demande d'attestation
    @FXML
    private void handleAttestation() {
        System.out.println("Demande Attestation clicked");

        // Charger le formulaire de demande d'attestation
        loadForm("/AttestationForm.fxml");
    }

    // Méthode pour gérer l'affichage des demandes de l'utilisateur


    // Méthode pour charger un formulaire spécifique
    private void loadForm(String fxmlFile) {
        try {
            // Charger le fichier FXML pour le formulaire
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Afficher la nouvelle scène dans la fenêtre actuelle
            Stage stage = (Stage) demandeSection.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement du formulaire.");
        }
    }
    @FXML
    private void handleMesDemandes() {
        System.out.println("Mes Demandes clicked");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MesDemandes.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) demandeSection.getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'interface des demandes.");
        }
    }

    // Méthode pour afficher les demandes de l'utilisateur
    private void loadMesDemandes() {
        String url = "jdbc:mysql://localhost:3306/workbridge";
        String username = "root";
        String password = "";  // Remplace par tes vrais identifiants

        String query = "SELECT * FROM demande WHERE utilisateur_id = ?";  // Adapté à l'utilisateur connecté
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, getUtilisateurId());  // Remplace par l'ID de l'utilisateur actuel

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String description = rs.getString("description");
                String statut = rs.getString("statut");
                String dateSoumission = rs.getString("date_soumission");

                // Affichage des demandes dans l'interface utilisateur
                System.out.println("ID: " + id + ", Type: " + type + ", Statut: " + statut + ", Date: " + dateSoumission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la récupération des demandes.");
        }
    }

    // Méthode pour afficher des alertes
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void initData(String data) {
        System.out.println("Initializing data with: " + data);
        // Tu peux ajouter ici des logiques pour initialiser des composants,
        // comme remplir un champ avec des données passées en paramètre.
    }

    // Méthode pour obtenir l'ID de l'utilisateur connecté (à adapter selon ton système)
    private int getUtilisateurId() {
        // Retourner l'ID de l'utilisateur connecté
        // Remplace cela par la logique pour obtenir l'ID réel de l'utilisateur
        return 1;  // Valeur par défaut, remplace par la valeur dynamique de l'utilisateur connecté
    }
}