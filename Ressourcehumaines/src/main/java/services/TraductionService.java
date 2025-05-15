package services;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;

public class TraductionService {
    private static final String API_URL = "https://libretranslate.de/translate";

    public String traduire(String texte, String langueSource, String langueCible) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String requestBodyContent = "q=" + texte +
                "&source=" + langueSource +
                "&target=" + langueCible +
                "&format=text";

        RequestBody body = RequestBody.create(requestBodyContent, mediaType);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erreur de traduction : " + response.code());
            }

            JSONObject jsonResponse = new JSONObject(response.body().string());
            return jsonResponse.getString("translatedText");
        }
    }
}
