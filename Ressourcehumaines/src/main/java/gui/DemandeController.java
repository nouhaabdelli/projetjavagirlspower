package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.*;

public class DemandeController {

    @FXML
    private VBox demandeSection;

    @FXML
    private Button btnConge;

    @FXML
    private Button btnAttestation;

    @FXML
    private Button btnMesDemandes;

    @FXML
    private Button btnStatistiques;

    // Méthode pour gérer la demande de congé
    @FXML
    private void handleCongé(ActionEvent event) {
        System.out.println("Demande Congé clicked");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CongéForm.fxml"));
            Parent root = loader.load();
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Une erreur s'est produite lors du chargement du formulaire.");
        }
    }

    // Méthode pour gérer la demande d'attestation
    @FXML
    private void handleAttestation(ActionEvent event) {
        System.out.println("Demande Attestation clicked");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AttestationForm.fxml"));
            Parent root = loader.load();
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Une erreur s'est produite lors du chargement du formulaire.");
        }
    }

    @FXML
    private void handleMesDemandes(ActionEvent event) {
        System.out.println("Mes Demandes clicked");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MesDemandes.fxml"));
            Parent root = loader.load();
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Impossible de charger l'interface des demandes.");
        }
    }

    // Méthode pour afficher les demandes de l'utilisateur
    private void loadMesDemandes() {
        String url = "jdbc:mysql://localhost:3306/workbridge";
        String username = "root";
        String password = "";

        String query = "SELECT * FROM demande WHERE utilisateur_id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, getUtilisateurId());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String description = rs.getString("description");
                String statut = rs.getString("statut");
                String dateSoumission = rs.getString("date_soumission");

                System.out.println("ID: " + id + ", Type: " + type + ", Statut: " + statut + ", Date: " + dateSoumission);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Une erreur s'est produite lors de la récupération des demandes.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initData(String data) {
        System.out.println("Initializing data with: " + data);
    }

    private int getUtilisateurId() {
        // Retourner l'ID de l'utilisateur connecté
        // Remplace cela par la logique pour obtenir l'ID réel de l'utilisateur
        return 1;  // Valeur par défaut, remplace par la valeur dynamique de l'utilisateur connecté
    }

    @FXML
    void ouvrirStatistiques(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Statistics.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Statistiques des Attestations");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors de l'ouverture des statistiques : " + e.getMessage());
        }
    }

    @FXML
    private void handleDeconnexion(ActionEvent event) {
        try {
            // Charger la scène de connexion
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible de charger la page de connexion");
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}