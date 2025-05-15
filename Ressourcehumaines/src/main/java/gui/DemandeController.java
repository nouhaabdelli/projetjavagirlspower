package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;

public class DemandeController {
    @FXML private Button btnConge;
    @FXML private Button btnAttestation;
    @FXML private Button btnMesDemandes;
    @FXML private Button btnStatistiques;
    @FXML private VBox sidebar;
    @FXML private VBox contentArea;

    private boolean isSidebarVisible = true;

    @FXML
    public void initialize() {
        ouvrirStatistiques(); // Appelle la version sans paramètre
    }

    private void ouvrirStatistiques() {
        ouvrirStatistiques(null); // Appelle la version FXML avec null
    }
    public void handleToutesLesDemandes(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DemandesAdmin.fxml"));
        try {
            // Charger la vue FXML
            Parent root = loader.load();

            // Effacer le contenu actuel dans contentArea
            contentArea.getChildren().clear();

            // Ajouter le nouveau contenu dans contentArea
            contentArea.getChildren().add(root);

        } catch (IOException e) {
            e.printStackTrace(); // voir où ça coince
            showErrorAlert("Une erreur s'est produite lors du chargement de la page des demandes administratives.");
        }
    }
    @FXML
    private void toggleSidebar(ActionEvent event) {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.3), sidebar);
        if (isSidebarVisible) {
            tt.setToX(-250);
        } else {
            tt.setToX(0);
        }
        tt.play();
        isSidebarVisible = !isSidebarVisible;
    }

    // Méthode pour gérer la demande de congé
    @FXML
    private void handleCongé(ActionEvent event) {
        System.out.println("Demande Congé clicked");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CongéForm.fxml"));
            Parent root = loader.load();
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);  // Ajout du formulaire dans contentArea
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
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);  // Ajout du formulaire dans contentArea
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
            contentArea.getChildren().clear();
            contentArea.getChildren().add(root);  // Affichage dans contentArea
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Statistics.fxml"));
            Parent statisticsContent = loader.load();
            contentArea.getChildren().clear(); // Vide le contenu actuel
            contentArea.getChildren().add(statisticsContent); // Ajoute la vue statistiques
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors du chargement des statistiques : " + e.getMessage());
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




