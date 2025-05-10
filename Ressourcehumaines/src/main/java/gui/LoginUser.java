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

import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;
import java.util.regex.Matcher;



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




    @FXML
    void handleSignUp(ActionEvent event) {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }

        if (!termsCheckBox.isSelected()) {
            System.out.println("Vous devez accepter les Conditions Générales.");
            return;
        }


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
        String apiKey = "AIzaSyCF_WYiihngjigbicM_VKNCBolfsjku_II"; // Ton apiKey Firebase

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

        System.out.println("Firebase Response: " + response.body());
    }

}
