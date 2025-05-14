package utils;


import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getGeminiApiKey() {
        return dotenv.get("GEMINI_API_KEY");
    }
}