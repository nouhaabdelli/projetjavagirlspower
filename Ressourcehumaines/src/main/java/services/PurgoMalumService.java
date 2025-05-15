package services;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PurgoMalumService {
    private static final String API_URL = "https://www.purgomalum.com/service/json?text=";

    public String checkAndCensorProfanity(String input) throws IOException {
        String encodedInput = URLEncoder.encode(input, StandardCharsets.UTF_8);
        String requestUrl = API_URL + encodedInput;

        HttpURLConnection connection = (HttpURLConnection) new URL(requestUrl).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("API request failed with response code: " + responseCode);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Parse JSON response
            JSONObject jsonObject = new JSONObject(response.toString());
            return jsonObject.getString("result");
        } finally {
            connection.disconnect();
        }
    }
}
