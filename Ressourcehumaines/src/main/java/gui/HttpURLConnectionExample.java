package gui;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import org.json.JSONObject;

public class HttpURLConnectionExample {

    private static final String API_KEY = "AIzaSyDjShgbvfPz8Ome5qL0MAyuvunZTYY7ink"; // Remplace avec ta clé API

    public static String generateDescription(String nomEvenement) throws Exception {
        try {
            // URL de l'API Gemini
            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY;

            // Création du JSON avec le prompt pour Gemini
            JSONObject jsonInput = new JSONObject();
            JSONObject content = new JSONObject();
            content.put("text", "Crée une description professionnelle et engageante pour un événement nommé : " + nomEvenement);
            jsonInput.put("contents", new JSONObject[] { content });

            // Connexion HTTP
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Envoi du JSON
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Réponse de l'API
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // Extraction de la description générée de la réponse JSON
            JSONObject responseJson = new JSONObject(response.toString());
            return responseJson.getJSONArray("candidates").getJSONObject(0).getString("content");

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Erreur lors de la génération de la description : " + e.getMessage());
        }
    }

}

