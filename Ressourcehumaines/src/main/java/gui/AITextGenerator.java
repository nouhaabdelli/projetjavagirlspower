/*package gui;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

public class AITextGenerator {

    private static final String API_URL = "https://api-inference.huggingface.co/models/distilgpt2";
    private static final String TOKEN = "Bearer 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"; // Remplacez par votre vrai token

    public static String generateDescription(String title) throws Exception {
        String prompt = "Generate a professional product description for the title: " + title;

        String requestBody = "{ \"inputs\": \"" + prompt.replace("\"", "\\\"") + "\" }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", TOKEN)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        System.out.println(responseBody); // Afficher la réponse brute pour déboguer

        // Si la réponse est simplement un texte, retournez-le directement
        if (responseBody != null && !responseBody.isEmpty()) {
            return responseBody;
        } else {
            return "No description generated.";
        }
    }


}*/
