package gui;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class FinanceController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button pretButton;

    @FXML
    private Button avanceButton;

    @FXML
    private ToggleButton themeToggle;

    @FXML
    private ImageView logo;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;

    private boolean isDarkTheme = false;

    @FXML
    public void initialize() {
        if (logo != null) {
            ScaleTransition logoPulse = new ScaleTransition(Duration.millis(2000), logo);
            logoPulse.setToX(1.1);
            logoPulse.setToY(1.1);
            logoPulse.setCycleCount(ScaleTransition.INDEFINITE);
            logoPulse.setAutoReverse(true);
            logoPulse.play();
        }
        if (chatArea != null) {
            chatArea.setEditable(false);
            chatArea.setWrapText(true);
            displayWelcomeMessage();
        }
    }

    private void displayWelcomeMessage() {
        String welcomeMessage = """
                🎙️ Bonjour et bienvenue dans le système de gestion financière ! 😊
                Je suis votre assistant intelligent, prêt à vous guider sur les **prêts** et **avances** de l’entreprise.
                Posez-moi une question (ex. : "Détails prêt", "Calculer prêt 10000 sur 24 mois", "Conditions prêt", "Combien de temps pour confirmation", "Comment connaître la réponse") ! 💬""";
        appendChatMessage("Chatbot", welcomeMessage);
    }

    @FXML
    private void handlePretButton() throws IOException {
        loadInterface("fxml/pret.fxml");
    }

    @FXML
    private void handleAvanceButton() throws IOException {
        loadInterface("fxml/avance.fxml");
    }

    @FXML
    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        rootPane.getStylesheets().clear();
        if (getClass().getResource("/style/finance.css") != null) {
            rootPane.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            System.out.println("finance.css chargé avec succès dans toggleTheme");
        } else {
            System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources.");
        }
        if (isDarkTheme) {
            rootPane.getStyleClass().add("dark-theme");
            themeToggle.setText("Mode Clair");
        } else {
            rootPane.getStyleClass().remove("dark-theme");
            themeToggle.setText("Mode Sombre");
        }
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

    protected String generateChatResponse(String input) {
        if ((input.contains("confirmation") || input.contains("reçu") || input.contains("recu") || input.contains("reçoive")) &&
                (input.contains("combien de temps") || input.contains("temps pour") || input.contains("délai"))) {
            return "📅 Vous recevrez une confirmation de votre demande de prêt ou d'avance au bout d'une semaine.";
        } else if ((input.contains("savoir") && (input.contains("acceptée") || input.contains("acceptee") || input.contains("refusée") || input.contains("refusee"))) ||
                (input.contains("connais") || input.contains("connaître") || input.contains("connaitre")) && input.contains("réponse") || input.contains("reponse")) {
            return "📧 Vous recevrez un email de confirmation.";
        } else if (input.contains("conditions") && (input.contains("prêt") || input.contains("pret"))) {
            return """
                    💰 **Conditions pour obtenir un prêt** :
                    - **Ancienneté minimale** : Vous devez avoir au moins 12 mois (1 an) d’ancienneté dans l’entreprise.
                    - **Contrat de travail valide** : Être un employé permanent, avec un contrat de travail en cours de validité (CDD ou CDI, selon les cas autorisés).
                    - **Historique financier sain** :
                      - Aucun prêt en cours dépassant les délais de remboursement.
                      - Aucun incident de paiement ou manquement à des obligations financières précédentes dans l'entreprise.
                    - **Capacité de remboursement** : Votre salaire mensuel doit permettre un remboursement sans dépasser un taux d’endettement maximal autorisé (souvent autour de 30 à 40% du salaire net mensuel).
                    - **Montant demandé** : Le montant du prêt demandé ne doit pas dépasser 50,000 TND.
                    - **Durée demandée** : La période de remboursement ne doit pas excéder 60 mois (5 ans).
                    - **Motif justifié** : Le prêt doit être demandé pour un motif sérieux et documenté (achat immobilier, frais médicaux, études, etc.).
                    - **Validation hiérarchique** : La demande doit être approuvée par votre responsable hiérarchique et/ou le service des ressources humaines.""";
        } else if (input.contains("conditions") && input.contains("avance")) {
            return """
                    💸 **Conditions pour obtenir une avance** :
                    - **Statut de l’employé** : Vous devez être employé actif de l’entreprise, avec un contrat de travail en cours (CDD ou CDI selon les règles internes).
                    - **Ancienneté minimale** : Avoir travaillé au moins 3 mois dans l’entreprise.
                    - **Montant demandé** : L’avance demandée ne doit pas dépasser 800 TND, soit le plafond maximal autorisé.
                    - **Durée de remboursement** : L’avance doit être remboursée dans un délai maximal de 12 mois (1 an).
                    - **Solde disponible** : Aucun remboursement en cours d’une avance précédente, ou un solde disponible suffisant pour en demander une nouvelle.
                    - **Motif valable** : La demande doit être faite pour un besoin personnel urgent ou exceptionnel (frais de santé, urgence familiale, etc.).
                    - **Validation hiérarchique** : L’avance doit être approuvée par le responsable direct ou le service des ressources humaines.""";
        } else if (input.contains("prêt") || input.contains("pret")) {
            if (input.contains("détails") || input.contains("info")) {
                return """
                        💰 **Détails sur les Prêts** :
                        - Montant maximum : 50,000 TND
                        - Durée maximale : 60 mois (5 ans)
                        - Taux d’intérêt : 5% par an
                        - Intérêts = (Montant * Taux * Durée) / 12
                        Exemple : Prêt de 20,000 TND sur 36 mois → Intérêts = 3,000 TND, Total = 23,000 TND
                        Posez une question spécifique (ex. : "Calculer prêt 10000 sur 24 mois") !""";
            } else if (input.contains("calculer") || input.contains("interet") || input.contains("intérêt")) {
                try {
                    String[] parts = input.split("\\s+");
                    int amount = 0, months = 0;
                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i].matches("\\d+")) {
                            if (amount == 0) amount = Integer.parseInt(parts[i]);
                            else months = Integer.parseInt(parts[i]);
                        }
                    }
                    if (amount > 50000) {
                        return "⚠️ Erreur : Montant maximum pour un prêt est 50,000 TND.";
                    }
                    if (months == 0) {
                        months = 12;
                        double interest = (amount * 0.05 * months) / 12.0;
                        double total = amount + interest;
                        double monthly = total / months;
                        return String.format("""
                                📊 **Calcul du Prêt (durée par défaut : 12 mois)** :
                                - Montant : %,d TND
                                - Durée : %d mois
                                - Intérêts (5%%) : %,.2f TND
                                - Total à rembourser : %,.2f TND
                                - Mensualité : %,.2f TND/mois
                                Pour une durée spécifique, essayez "calculer prêt %d sur [mois]". """,
                                amount, months, interest, total, monthly, amount);
                    }
                    if (months > 60) {
                        return "⚠️ Erreur : Durée maximale pour un prêt est 60 mois.";
                    }
                    double interest = (amount * 0.05 * months) / 12.0;
                    double total = amount + interest;
                    double monthly = total / months;
                    return String.format("""
                            📊 **Calcul du Prêt** :
                            - Montant : %,d TND
                            - Durée : %d mois
                            - Intérêts (5%%) : %,.2f TND
                            - Total à rembourser : %,.2f TND
                            - Mensualité : %,.2f TND/mois""", amount, months, interest, total, monthly);
                } catch (Exception e) {
                    return "⚠️ Erreur : Veuillez spécifier le montant et éventuellement la durée (ex. : 'Calculer prêt 10000 sur 24 mois').";
                }
            }
            return "💰 Prêt : Max 50,000 TND, 60 mois, 5% intérêt. Demandez 'détails prêt', 'conditions prêt', ou 'calculer prêt [montant] sur [mois]'.";
        } else if (input.contains("avance")) {
            if (input.contains("détails") || input.contains("info")) {
                return """
                        💸 **Détails sur les Avances** :
                        - Montant maximum : 800 TND
                        - Durée maximale : 12 mois
                        - Taux d’intérêt : 0% (sauf conditions spéciales)
                        Exemple : Avance de 600 TND sur 6 mois → 100 TND/mois
                        Posez une question spécifique (ex. : "Calculer avance 500 sur 6 mois") !""";
            } else if (input.contains("calculer") || input.contains("interet") || input.contains("intérêt")) {
                try {
                    String[] parts = input.split("\\s+");
                    int amount = 0, months = 0;
                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i].matches("\\d+")) {
                            if (amount == 0) amount = Integer.parseInt(parts[i]);
                            else months = Integer.parseInt(parts[i]);
                        }
                    }
                    if (amount > 800) {
                        return "⚠️ Erreur : Montant maximum pour une avance est 800 TND.";
                    }
                    if (months == 0) {
                        months = 6;
                        double monthly = amount / (double) months;
                        return String.format("""
                                📊 **Calcul de l'Avance (durée par défaut : 6 mois)** :
                                - Montant : %,d TND
                                - Durée : %d mois
                                - Intérêts : 0 TND (0%%)
                                - Mensualité : %,.2f TND/mois
                                Pour une durée spécifique, essayez "calculer avance %d sur [mois]". """,
                                amount, months, monthly, amount);
                    }
                    if (months > 12) {
                        return "⚠️ Erreur : Durée maximale pour une avance est 12 mois.";
                    }
                    double monthly = amount / (double) months;
                    return String.format("""
                            📊 **Calcul de l'Avance** :
                            - Montant : %,d TND
                            - Durée : %d mois
                            - Intérêts : 0 TND (0%%)
                            - Mensualité : %,.2f TND/mois""", amount, months, monthly);
                } catch (Exception e) {
                    return "⚠️ Erreur : Veuillez spécifier le montant et éventuellement la durée (ex. : 'Calculer avance 500 sur 6 mois').";
                }
            }
            return "💸 Avance : Max 800 TND, 12 mois, 0% intérêt. Demandez 'détails avance', 'conditions avance', ou 'calculer avance [montant] sur [mois]'.";
        } else if (input.contains("récapitulatif") || input.contains("recap")) {
            return """
                    📬 **Récapitulatif Financier** :
                    - **Prêts** : Jusqu’à 50,000 TND, 60 mois max, 5% intérêt/an
                    - **Avances** : Jusqu’à 800 TND, 12 mois max, 0% intérêt
                    - **Fonctionnalités** : Consultez détails, calculez mensualités, vérifiez conditions, faites des demandes
                    - **Rappels** : Justifiez vos demandes, respectez les délais
                    Demandez plus de détails ou un calcul spécifique !""";
        }
        return "🤔 Je ne comprends pas. Essayez 'détails prêt', 'détails avance', 'conditions prêt', 'combien de temps pour confirmation', 'comment connaître la réponse', ou 'récapitulatif'.";
    }

    private void loadInterface(String fxmlFile) throws IOException {
        if (getClass().getResource("/" + fxmlFile) == null) {
            System.err.println("Erreur : Le fichier " + fxmlFile + " n'est pas trouvé dans les ressources.");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) rootPane.getScene().getWindow();
        Scene scene = new Scene(root);
        if (getClass().getResource("/style/finance.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            System.out.println("finance.css chargé avec succès dans " + fxmlFile);
        } else {
            System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources.");
        }
        stage.setScene(scene);
        stage.show();
    }
    public class ChatGPTClient {
        // ⚠️ Ne jamais mettre une clé API en dur dans le code en production !
        private static final String API_KEY = "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";

        public static String getChatResponse(String userMessage) throws IOException, InterruptedException {
            // Construction du corps JSON
            String json = String.format("""
            {
                "model": "gpt-3.5-turbo",
                "messages": [
                    {"role": "user", "content": "%s"}
                ]
            }
            """, userMessage.replace("\"", "\\\"")); // échappe les guillemets si présents

            // Création de la requête HTTP POST
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            // Envoi de la requête
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Retour du corps de la réponse
            return response.body();
        }


    }
}
