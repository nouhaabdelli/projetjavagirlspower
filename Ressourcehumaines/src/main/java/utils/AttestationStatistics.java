package utils;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

public class AttestationStatistics {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/workbridge";
    private static final String USER = "root";
    private static final String PASS = "";

    // Statistiques par période
    public static Map<YearMonth, Integer> getAttestationsByMonth() {
        Map<YearMonth, Integer> stats = new TreeMap<>();
        String query = "SELECT DATE_FORMAT(date_soumission, '%Y-%m') as month, COUNT(*) as count " +
                      "FROM demande WHERE type = 'attestation' " +
                      "GROUP BY DATE_FORMAT(date_soumission, '%Y-%m') " +
                      "ORDER BY month";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String monthStr = rs.getString("month");
                YearMonth yearMonth = YearMonth.parse(monthStr);
                stats.put(yearMonth, rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    // Statistiques par type d'attestation
    public static Map<String, Map<String, Integer>> getAttestationsByType() {
        Map<String, Map<String, Integer>> stats = new HashMap<>();
        String query = "SELECT type_attestation, statut, COUNT(*) as count " +
                      "FROM demande WHERE type = 'attestation' " +
                      "GROUP BY type_attestation, statut";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String type = rs.getString("type_attestation");
                String status = rs.getString("statut");
                int count = rs.getInt("count");

                stats.computeIfAbsent(type, k -> new HashMap<>())
                     .put(status, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    // Temps moyen de traitement
    public static Map<String, Double> getAverageProcessingTime() {
        Map<String, Double> processingTimes = new HashMap<>();
        String query = "SELECT type, AVG(TIMESTAMPDIFF(HOUR, date_soumission, date_validation)) as avg_time " +
                      "FROM demande " +
                      "WHERE statut = 'validée' " +
                      "GROUP BY type";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String type = rs.getString("type");
                double avgTime = rs.getDouble("avg_time");
                processingTimes.put(type, avgTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return processingTimes;
    }

    // Statistiques par utilisateur
    public static Map<Integer, Map<String, Integer>> getUserStatistics() {
        Map<Integer, Map<String, Integer>> stats = new HashMap<>();
        String query = "SELECT utilisateur_id, type_attestation, COUNT(*) as count " +
                      "FROM demande WHERE type = 'attestation' " +
                      "GROUP BY utilisateur_id, type_attestation";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int userId = rs.getInt("utilisateur_id");
                String type = rs.getString("type_attestation");
                int count = rs.getInt("count");

                stats.computeIfAbsent(userId, k -> new HashMap<>())
                     .put(type, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    // Taux de conversion par type
    public static Map<String, Double> getConversionRates() {
        Map<String, Double> stats = new HashMap<>();
        String query = "SELECT type_attestation, " +
                      "COUNT(*) as total, " +
                      "SUM(CASE WHEN statut = 'acceptée' THEN 1 ELSE 0 END) as accepted " +
                      "FROM demande WHERE type = 'attestation' " +
                      "GROUP BY type_attestation";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String type = rs.getString("type_attestation");
                int total = rs.getInt("total");
                int accepted = rs.getInt("accepted");
                double rate = total > 0 ? (double) accepted / total * 100 : 0;
                stats.put(type, rate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    // Méthode utilitaire pour formater les statistiques
    public static String formatStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("Statistiques des Attestations\n");
        stats.append("===========================\n\n");

        // Statistiques mensuelles
        stats.append("Attestations par mois :\n");
        getAttestationsByMonth().forEach((month, count) -> 
            stats.append(String.format("- %s : %d attestations\n", month, count)));
        stats.append("\n");

        // Distribution par type
        stats.append("Distribution par type :\n");
        getAttestationsByType().forEach((type, statusMap) -> {
            int total = statusMap.values().stream().mapToInt(Integer::intValue).sum();
            stats.append(String.format("- %s : %d attestations\n", type, total));
        });
        stats.append("\n");

        // Temps de traitement moyen
        stats.append("Temps de traitement moyen (heures) :\n");
        getAverageProcessingTime().forEach((type, time) ->
            stats.append(String.format("- %s : %.2f heures\n", type, time)));

        return stats.toString();
    }
} 