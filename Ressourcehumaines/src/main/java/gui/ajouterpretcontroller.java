package gui;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import services.pretservice;
import entities.pret;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ajouterpretcontroller {
    @FXML
    private TextField montantField;

    @FXML
    private TextField dureeField;

    @FXML
    private DatePicker datePretPicker;

    @FXML
    private TextField niveauUrgenceField;

    @FXML
    private TextField etatField;

    @FXML
    private TextField montantTNDField; // Champ pour entrer le montant en TND

    @FXML
    private Label resultLabel; // Label pour afficher le résultat de la conversion

    private pret pretEnCours; // utilisé si modification
    private listepretcontroller controllerPrincipal; // référence au contrôleur principal

    // Simulate current user ID (replace with actual session logic)
    private int currentUserId = 1; // Example: hardcoded for now; replace with session/user context

    @FXML
    private void ajouterPret() {
        try {
            BigDecimal montant = new BigDecimal(montantField.getText());
            int duree = Integer.parseInt(dureeField.getText());
            LocalDate datePret = datePretPicker.getValue();
            String urgence = niveauUrgenceField.getText();
            String etat = etatField.getText();

            // Create pret without id_user initially
            pret pret = new pret(0, montant, duree, datePret, urgence, etat);
            pret.setId_user(currentUserId); // Set id_user from session/context

            pretservice pretservice = new pretservice();
            pretservice.create(pret);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Le prêt a été enregistré avec succès !");
            alert.showAndWait();

            if (controllerPrincipal != null) {
                controllerPrincipal.rafraichirTable();
            }

            // Nettoyer les champs
            montantField.clear();
            dureeField.clear();
            datePretPicker.setValue(null);
            niveauUrgenceField.clear();
            etatField.clear();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Champs numériques incorrects");
            alert.setContentText("Veuillez saisir un montant et une durée valides.");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Une erreur est survenue");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void convertUSDToEUR() {
        try {
            String tndText = montantTNDField.getText().trim();

            // Test si le champ est vide
            if (tndText.isEmpty()) {
                // Appel API pour afficher la réponse brute
                String apiKey = "fca_live_CcOjFkIyL5TjK7McBqyHG0e06esmIME9UEczeaem";
                String url = "https://api.freecurrencyapi.com/v1/latest?apikey=" + apiKey + "&base_currency=EUR&currencies=USD";

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String jsonResponse = response.body();

                // Affiche la réponse brute JSON dans le label
                resultLabel.setText(jsonResponse);
                return;
            }

            double amountTND;
            try {
                amountTND = Double.parseDouble(tndText);
            } catch (NumberFormatException e) {
                resultLabel.setText("Résultat : Veuillez entrer un montant numérique valide !");
                return;
            }

            if (amountTND <= 0) {
                resultLabel.setText("Résultat : Le montant doit être positif !");
                return;
            }

            String apiKey = "fca_live_CcOjFkIyL5TjK7McBqyHG0e06esmIME9UEczeaem";
            String url = "https://api.freecurrencyapi.com/v1/latest?apikey=" + apiKey + "&base_currency=EUR&currencies=USD";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();

            System.out.println(jsonResponse);

            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

            if (!jsonObject.has("data") || !jsonObject.getAsJsonObject("data").has("USD")) {
                resultLabel.setText("Résultat : Taux de conversion non trouvé !");
                return;
            }

            double tndPerEuro = jsonObject.getAsJsonObject("data").get("USD").getAsDouble();
            double eurAmount = amountTND / tndPerEuro;

            resultLabel.setText(String.format("Résultat : %.2f USD = %.2f EUR", amountTND, eurAmount));

        } catch (IOException | InterruptedException e) {
            resultLabel.setText("Résultat : Erreur réseau : " + e.getMessage());
        } catch (Exception e) {
            resultLabel.setText("Résultat : Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
    }



    public void setControllerPrincipal(listepretcontroller controller) {
        this.controllerPrincipal = controller;
    }

    // Method to set current user ID (e.g., from login session)
    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }
}