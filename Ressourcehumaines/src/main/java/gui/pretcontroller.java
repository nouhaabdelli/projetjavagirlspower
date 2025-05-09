package gui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class pretcontroller extends FinanceController {
    @FXML
    private Label titrePrincipal;

    @FXML
    private VBox sideMenu;

    @FXML
    private AnchorPane mainContent;

    @FXML
    private Circle pulseDot;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;

    @FXML
    private Button chatButton;

    @FXML
    public void initialize() {
        if (titrePrincipal != null) {
            titrePrincipal.setText("Gestion des Prêts");
        }
        if (pulseDot != null) {
            ScaleTransition pulse = new ScaleTransition(Duration.millis(1000), pulseDot);
            pulse.setToX(1.2);
            pulse.setToY(1.2);
            pulse.setCycleCount(ScaleTransition.INDEFINITE);
            pulse.setAutoReverse(true);
            pulse.play();
        }
        if (chatArea != null) {
            chatArea.setEditable(false);
            chatArea.setWrapText(true);
            displayWelcomeMessage();
        }
    }

    private void displayWelcomeMessage() {
        String welcomeMessage = """
                💰 Bienvenue dans la gestion des prêts ! 😊
                Je peux vous aider à calculer les intérêts, vérifier les conditions, ou suivre votre demande.
                Posez-moi une question (ex. : "Calculer prêt 10000 sur 24 mois", "Conditions prêt", "Confirmation prêt") ! 💬""";
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
    private void showAjouterPret() {
        if (titrePrincipal != null) {
            titrePrincipal.setText("Ajout d'un Prêt");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ajouterpret.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showListePret() {
        if (titrePrincipal != null) {
            titrePrincipal.setText("Liste des Prêts");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listepret.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showReponsesRH() {
        try {
            System.out.println("Chargement de reponsespretrh.fxml depuis pret.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reponsespretrh.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("Resource 'fxml/reponsespretrh.fxml' not found.");
            }
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
                System.out.println("finance.css chargé avec succès");
            }
            newStage.setScene(scene);
            newStage.show();
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            if (currentStage != null) {
                currentStage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetour() {
        try {
            System.out.println("Tentative de retour à l'interface précédente depuis pret.fxml");
            Stage stage = (Stage) mainContent.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/finance.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            }
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}