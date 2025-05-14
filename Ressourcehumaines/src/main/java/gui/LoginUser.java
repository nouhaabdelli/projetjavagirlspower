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
import javafx.application.Platform;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import netscape.javascript.JSObject;
import utils.UserSession;


public class LoginUser {
    @FXML
    private WebView captchaWebView;

    @FXML
    private Hyperlink mdpoub;
    @FXML
    private Button signin;

    @FXML
    private TextField emailField;

    @FXML
    private TextField statutField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink signInLink;
    @FXML
    private Hyperlink googleLoginLink;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/workbridge";
    private static final String DB_USER = "root";  // Should be configured externally
    private static final String DB_PASSWORD = ""; // Should never be empty in production
    private boolean isCaptchaValid = false;


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
    void handleSignIn(ActionEvent event) {
        String statut = statutField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (statut.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
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
       /* if (!isCaptchaValid) {
            showAlert("reCAPTCHA", "Veuillez valider le reCAPTCHA avant de vous connecter.");
            return;
        }
*/



        // Connexion réussie
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Connexion réussie !");
        alert.showAndWait();

        String apiKey = "AIzaSyCF_WYiihngjigbicM_VKNCBolfsjku_II";
        String jsonBody = """
    {
        "email": "%s",
        "password": "%s",
        "returnSecureToken": true
    }
    """.formatted(email, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) { String responseBody = response.body();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String idToken = jsonResponse.getString("idToken");
                        String refreshToken = jsonResponse.getString("refreshToken");

                        Platform.runLater(() -> {
                            UserSession.getInstance().setFirebaseIdToken(idToken);
                            UserSession.getInstance().setFirebaseRefreshToken(refreshToken);


                            alert.setTitle("Succès");
                            alert.setHeaderText(null);
                            alert.setContentText("Connexion réussie !");
                            alert.showAndWait();

                            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                            updatePasswordInDB(email, hashedPassword);
                        });

                    } else {
                        Platform.runLater(() -> {
                            System.out.println("Erreur Firebase : " + response.body());
                        });
                    }
                });
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
    @FXML
    void mdpoub(ActionEvent event) {String email = emailField.getText();
        if (email.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer votre adresse email.");
            return;
        }

        String apiKey = "AIzaSyCF_WYiihngjigbicM_VKNCBolfsjku_II";  // à remplacer

        String jsonBody = """
        {
            "requestType": "PASSWORD_RESET",
            "email": "%s"
        }
        """.formatted(email);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        showAlert("Succès", "Un email de réinitialisation a été envoyé.");
                    } else {
                        showAlert("Erreur", "Échec de la réinitialisation : " + response.body());
                    }
                });
    }

    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


    private void updatePasswordInDB(String email, String newPassword) {
        // Mettre à jour le mot de passe dans la base de données
        String sql = "UPDATE user SET MotDePasse = ? WHERE Email = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Succès Mot de passe mis à jour avec succès dans la base de données !");
            } else {
                System.out.println("Erreur Impossible de mettre à jour le mot de passe en base de données.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur Erreur lors de la mise à jour en base de données : " + e.getMessage());
        }
    }
    public class JavaBridge {
        public void sendToken(String token) {
            System.out.println("reCAPTCHA token reçu : " + token);
            verifyCaptchaToken(token); // Appelle la vérification serveur
        }
    }

    private void verifyCaptchaToken(String token) {
        String secretKey = "6Le3YjcrAAAAAEeJlZbZ1TDpGqp6IhMUiJAaVr2u"; // Remplacez par la vôtre
        String requestBody = "secret=" + secretKey + "&response=" + token ;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.google.com/recaptcha/api/siteverify"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    System.out.println("Réponse reCAPTCHA : " + response.body());
                    if (response.body().contains("\"success\": true")) {
                        isCaptchaValid = true;
                        Platform.runLater(() -> showAlert("Succès", "reCAPTCHA validé avec succès."));
                    } else {
                        isCaptchaValid = false;
                        Platform.runLater(() -> showAlert("Échec", "Échec de la validation reCAPTCHA."));
                    }
                });
    }

    @FXML
    public void initialize() {
        WebEngine webEngine = captchaWebView.getEngine();

        // Permettre à JS d'appeler Java
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("javaApp", new JavaBridge());
            }
        });

        // Charger le HTML dans WebView (depuis une ressource locale)
        webEngine.load(getClass().getResource("/recaptcha.html").toExternalForm());
    }








}
