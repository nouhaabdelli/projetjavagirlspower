package gui;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class avancecontroller extends FinanceController {
    @FXML
    private AnchorPane mainContent;

    @FXML
    private Label titrePrincipal;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;

    @FXML
    private Button chatButton;

    private int currentUserId = 1; // Assurez-vous que cet ID existe dans la table user

    @FXML
    public void initialize() {
        if (titrePrincipal != null) {
            titrePrincipal.setText("Gestion des Avances");
        } else {
            System.err.println("titrePrincipal est null dans initialize()");
        }
        if (chatArea != null) {
            chatArea.setEditable(false);
            chatArea.setWrapText(true);
            displayWelcomeMessage();
        } else {
            System.err.println("chatArea est null dans initialize()");
        }
    }

    private void displayWelcomeMessage() {
        String welcomeMessage = """
                💸 Bienvenue dans la gestion des avances ! 😊
                Je peux vous aider à calculer les mensualités, vérifier les conditions, ou suivre votre demande.
                Posez-moi une question (ex. : "Calculer avance 500 sur 6 mois", "Conditions avance", "Confirmation avance") ! 💬""";
        appendChatMessage("Chatbot", welcomeMessage);
    }

    @FXML
    private void handleChatInput() {
        String userInput = chatInput.getText().trim();
        if (!userInput.isEmpty()) {
            appendChatMessage("Vous", userInput);
            String response = generateChatResponse(userInput.toLowerCase());
            appendChatMessage("Chatbot", response);
            chatInput.clear();
        }
    }

    private void appendChatMessage(String sender, String message) {
        String styledMessage = String.format("[%s]: %s\n", sender, message);
        chatArea.appendText(styledMessage);
        FadeTransition fade = new FadeTransition(Duration.millis(500), chatArea);
        fade.setFromValue(0.7);
        fade.setToValue(1.0);
        fade.play();
        chatArea.setScrollTop(Double.MAX_VALUE);
    }

    @FXML
    private void showAjouterAvance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ajouteravance.fxml"));
            Parent ajouterAvancePane = loader.load();
            ajouteravancecontroller controller = loader.getController();
            controller.setCurrentUserId(currentUserId);
            mainContent.getChildren().setAll(ajouterAvancePane);
            titrePrincipal.setText("➕ Ajouter une Avance");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de ajouteravance.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showListeAvance() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listeavance.fxml"));
            Parent listeAvancePane = loader.load();
            listeavancecontroller controller = loader.getController();
            controller.setCurrentUserId(currentUserId);
            mainContent.getChildren().setAll(listeAvancePane);
            titrePrincipal.setText("📋 Liste des Avances");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de listeavance.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showReponsesRH() {
        try {
            Parent reponsesPane = FXMLLoader.load(getClass().getResource("/fxml/reponsesrh.fxml"));
            mainContent.getChildren().setAll(reponsesPane);
            titrePrincipal.setText("💬 Réponses RH");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de reponsesrh.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void showStatistiques() {
        try {
            Parent statsPane = FXMLLoader.load(getClass().getResource("/fxml/stats.fxml"));
            mainContent.getChildren().setAll(statsPane);
            titrePrincipal.setText("📊 Statistiques des Avances");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de stats.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetour() {
        try {
            System.out.println("Tentative de retour à finance.fxml depuis avance.fxml");
            String fxmlPath = "/fxml/finance.fxml";
            if (getClass().getResource(fxmlPath) == null) {
                System.err.println("Erreur : Le fichier " + fxmlPath + " n'a pas été trouvé dans les ressources.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
                System.out.println("finance.css chargé avec succès");
            } else {
                System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources.");
            }

            Stage currentStage = null;
            if (mainContent != null && mainContent.getScene() != null && mainContent.getScene().getWindow() != null) {
                currentStage = (Stage) mainContent.getScene().getWindow();
                System.out.println("currentStage récupéré via mainContent");
            } else if (chatArea != null && chatArea.getScene() != null && chatArea.getScene().getWindow() != null) {
                currentStage = (Stage) chatArea.getScene().getWindow();
                System.out.println("currentStage récupéré via chatArea");
            } else if (titrePrincipal != null && titrePrincipal.getScene() != null && titrePrincipal.getScene().getWindow() != null) {
                currentStage = (Stage) titrePrincipal.getScene().getWindow();
                System.out.println("currentStage récupéré via titrePrincipal");
            }

            if (currentStage != null) {
                currentStage.setScene(scene);
                currentStage.show();
                System.out.println("Scène remplacée avec succès");
            } else {
                System.err.println("Erreur : Impossible de remplacer la scène, currentStage est null. Ouverture d'une nouvelle fenêtre en fallback.");
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.show();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de finance.fxml dans AvanceController : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur inattendue dans handleRetour : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }
}