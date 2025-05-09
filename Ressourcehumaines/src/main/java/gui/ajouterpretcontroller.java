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
    private void convertTNDToEUR() {
        try {
            String tndText = montantTNDField.getText().trim();
            if (tndText.isEmpty()) {
                resultLabel.setText("Résultat : Veuillez entrer un montant en TND !");
                return;
            }

            double amountTND = Double.parseDouble(tndText);
            if (amountTND <= 0) {
                resultLabel.setText("Résultat : Le montant doit être positif !");
                return;
            }

            // Remplacez par votre clé API Xe (obtenue après inscription sur xe.com)
            String apiKey = "VOTRE_CLÉ_API_ICI";
            String url = "https://xecdapi.xe.com/v1/convert_from?from=TND&to=EUR&amount=" + amountTND;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + apiKey)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonResponse = response.body();

            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            double euroAmount = jsonObject.getAsJsonObject("to").get("EUR").getAsDouble();

            resultLabel.setText(String.format("Résultat : %.2f TND = %.2f EUR", amountTND, euroAmount));

        } catch (NumberFormatException e) {
            resultLabel.setText("Résultat : Veuillez entrer un montant valide !");
        } catch (IOException | InterruptedException e) {
            resultLabel.setText("Résultat : Erreur lors de la conversion : " + e.getMessage());
        } catch (Exception e) {
            resultLabel.setText("Résultat : Erreur inattendue : " + e.getMessage());
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