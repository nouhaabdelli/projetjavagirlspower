package utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HolidayAPI {
    private static final String API_URL = "https://date.nager.at/api/v3/PublicHolidays";

    public static class Holiday {
        private final LocalDate date;
        private final String name;
        private final String localName;
        private final boolean isGlobalHoliday;

        public Holiday(LocalDate date, String name, String localName, boolean isGlobalHoliday) {
            this.date = date;
            this.name = name;
            this.localName = localName;
            this.isGlobalHoliday = isGlobalHoliday;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getName() {
            return name;
        }

        public String getLocalName() {
            return localName;
        }

        public boolean isGlobalHoliday() {
            return isGlobalHoliday;
        }

        @Override
        public String toString() {
            return String.format("%s : %s (%s)",
                    date.toString(),
                    localName, name);
        }
    }

    public static List<Holiday> getHolidays(int year) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String url = String.format("%s/%d/FR", API_URL, year); // Pays : France (FR)

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Erreur API Holiday : " + response);

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Le corps de la réponse est nul");
            }

            String jsonString = responseBody.string();

            // Optionnel: debug - afficher la réponse brute
            System.out.println("Réponse API : " + jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);
            List<Holiday> holidays = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject holiday = jsonArray.getJSONObject(i);
                holidays.add(new Holiday(
                        LocalDate.parse(holiday.getString("date")),
                        holiday.getString("name"),
                        holiday.getString("localName"),
                        holiday.getBoolean("global")
                ));
            }

            return holidays;
        }
    }

    public static boolean isHoliday(LocalDate date) throws IOException {
        List<Holiday> holidays = getHolidays(date.getYear());
        return holidays.stream().anyMatch(h -> h.getDate().equals(date));
    }

    public static int getWorkingDays(LocalDate startDate, LocalDate endDate) throws IOException {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }

        // On crée une nouvelle liste mutable pour cumuler les jours fériés
        List<Holiday> holidays = new ArrayList<>(getHolidays(startDate.getYear()));
        if (startDate.getYear() != endDate.getYear()) {
            holidays.addAll(getHolidays(endDate.getYear()));
        }

        int workingDays = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            final LocalDate current = currentDate;

            boolean isWeekend = current.getDayOfWeek().getValue() > 5; // 6=Samedi, 7=Dimanche
            boolean isHoliday = holidays.stream().anyMatch(h -> h.getDate().equals(current));

            // Debug: afficher la date et son statut
            System.out.printf("Date : %s | Weekend : %b | Férié : %b%n", current, isWeekend, isHoliday);

            if (!isWeekend && !isHoliday) {
                workingDays++;
            }

            currentDate = currentDate.plusDays(1);
        }

        return workingDays;
    }

    // Pour tester rapidement
    public static void main(String[] args) {
        try {
            int year = 2025;
            List<Holiday> holidays = getHolidays(year);
            System.out.println("Jours fériés en " + year + " : " + holidays.size());
            for (Holiday h : holidays) {
                System.out.println(h);
            }

            // Test du calcul jours ouvrés
            LocalDate start = LocalDate.of(2025, 5, 1);
            LocalDate end = LocalDate.of(2025, 5, 15);
            int workingDays = getWorkingDays(start, end);
            System.out.println("Jours ouvrés entre " + start + " et " + end + " : " + workingDays);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
