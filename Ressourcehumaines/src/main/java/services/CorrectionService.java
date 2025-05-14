package services;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class CorrectionService {
    private static final String API_URL = "https://api.languagetool.org/v2/check";

    public String corrigerTexte(String texte) {
        try {
            URL url = new URL("https://api.languagetoolplus.com/v2/check");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String data = "text=" + URLEncoder.encode(texte, "UTF-8")
                    + "&language=fr"
                    + "&enabledOnly=false";

            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes(StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject json = new JSONObject(response.toString());
            JSONArray matches = json.getJSONArray("matches");

            // Application des corrections en partant de la fin du texte
            StringBuilder correctedText = new StringBuilder(texte);
            for (int i = matches.length() - 1; i >= 0; i--) {
                JSONObject match = matches.getJSONObject(i);
                JSONArray replacements = match.getJSONArray("replacements");
                if (replacements.length() > 0) {
                    String replacement = replacements.getJSONObject(0).getString("value");
                    int offset = match.getInt("offset");
                    int length = match.getInt("length");
                    correctedText.replace(offset, offset + length, replacement);
                }
            }

            return correctedText.toString();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return texte; // En cas d'erreur, on retourne le texte original
        }
}}