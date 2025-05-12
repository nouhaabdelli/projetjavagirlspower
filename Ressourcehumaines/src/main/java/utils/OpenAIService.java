package utils;
import com.google.gson.Gson;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class OpenAIService {

    private static final String API_KEY = "votre_clé_API_openai"; // Remplacez par votre propre clé API
    private static final String OPENAI_URL = "https://api.openai.com/v1/completions";

    public static String predictResponseTime(double differenceInDays) {
        try {
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(OPENAI_URL);

            // Définir l'en-tête d'autorisation avec votre clé API OpenAI
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + API_KEY);

            // Créer le corps de la requête JSON
            String jsonBody = "{\n" +
                    "  \"model\": \"text-davinci-003\",\n" +
                    "  \"prompt\": \"Prédire le temps de réponse moyen en jours entre la soumission et la validation basé sur la différence de jours suivante : " + differenceInDays + "\",\n" +
                    "  \"max_tokens\": 50,\n" +
                    "  \"temperature\": 0.7\n" +
                    "}";

            // Attacher le corps JSON à la requête
            StringEntity entity = new StringEntity(jsonBody);
            post.setEntity(entity);

            // Exécuter la requête
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.execute(post).getEntity().getContent()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Utiliser Gson pour analyser la réponse JSON
            Gson gson = new Gson();
            OpenAIResponse openAIResponse = gson.fromJson(response.toString(), OpenAIResponse.class);

            // Récupérer le résultat de la prédiction
            return openAIResponse.choices[0].text.trim();

        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la prédiction.";
        }
    }

    // Classe pour mapper la réponse de l'API OpenAI
    static class OpenAIResponse {
        Choice[] choices;

        static class Choice {
            String text;
        }
    }
}