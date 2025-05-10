package gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.*;




public class LoginUser {

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink signInLink;
    @FXML
    private Hyperlink googleLoginLink;


    @FXML
    private CheckBox termsCheckBox;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/workbridge";
    private static final String DB_USER = "root";  // Should be configured externally
    private static final String DB_PASSWORD = ""; // Should never be empty in production

    private boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE Email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            // Log error properly instead of printStackTrace
            System.err.println("Error checking email existence: " + e.getMessage());
            return false; // Fail gracefully
        }
    }
    private String extractEmailFromResponse(String json) {
        Pattern pattern = Pattern.compile("\"email\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }






    @FXML
    void handleSignUp(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        if (!termsCheckBox.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Conditions non acceptées");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez accepter les Conditions Générales.");
            alert.showAndWait();
            return;
        }

        if (!emailExists(email)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Cet email n'existe pas dans notre base de données.");
            alert.showAndWait();
            return;
        }

        // Connexion réussie
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Connexion réussie !");
        alert.showAndWait();
    }



    @FXML
    void termsCheckBox(ActionEvent event) {
        if (termsCheckBox.isSelected()) {
            System.out.println("Conditions Générales acceptées.");
        } else {
            System.out.println("Conditions Générales non acceptées.");
        }
    }
    @FXML
    private void handleGoogleLogin(ActionEvent event) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Paramètres Firebase
        String clientId = "1055142804084-flji6os6du1omng2bd7td7up9gv93tvr.apps.googleusercontent.com";
        String redirectUri = "https://applicaton-rh.firebaseapp.com/__/auth/handler";

        // URL OAuth2 Google
        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=token"
                + "&scope=email%20profile%20openid";

        // Charger la WebView avec l'URL
        webEngine.load(authUrl);

        // Ouvrir la WebView dans une nouvelle fenêtre
        Stage stage = new Stage();
        stage.setScene(new Scene(webView, 600, 600));
        stage.show();

        // Quand l'utilisateur est redirigé après login
        webEngine.locationProperty().addListener((obs, oldLoc, newLoc) -> {
            if (newLoc.contains("access_token=")) {
                stage.close();
                String idToken = extractTokenFromUrl(newLoc);
                if (idToken != null) {
                    System.out.println("ID Token récupéré : " + idToken);
                    try {
                        sendTokenToFirebase(idToken);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Token d'accès non trouvé dans l'URL.");
                }
            }
        });
    }

    private String extractTokenFromUrl(String url) {
        Pattern pattern = Pattern.compile("access_token=([^&]+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) return matcher.group(1);
        return null;
    }

    private void sendTokenToFirebase(String idToken) throws Exception {
        String apiKey = "AIzaSyCF_WYiihngjigbicM_VKNCBolfsjku_II";

        String jsonBody = """
    {
      "postBody": "access_token=%s&providerId=google.com",
      "requestUri": "http://localhost",
      "returnIdpCredential": true,
      "returnSecureToken": true
    }
    """.formatted(idToken);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signInWithIdp?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();

        System.out.println("Firebase Response: " + responseBody);

        // 🟡 EXTRAIRE L'EMAIL
        String email = extractEmailFromResponse(responseBody);
        if (email != null) {
            System.out.println("Email récupéré : " + email);

            if (emailExists(email)) {
                // Alerte pour la connexion réussie
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Connexion Google");
                alert.setHeaderText(null);
                alert.setContentText("Connexion Google réussie, utilisateur reconnu.");
                alert.showAndWait();
                // TODO: Rediriger vers l’accueil ou charger la prochaine scène
            } else {
                // Alerte pour l'email non trouvé
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Accès refusé");
                alert.setHeaderText(null);
                alert.setContentText("Cet email n'existe pas dans la base. Accès refusé.");
                alert.showAndWait();
                // TODO: Afficher une alerte à l’utilisateur (facultatif)
            }
        } else {
            System.out.println("Impossible de récupérer l'email depuis Firebase.");
        }
    }

}
