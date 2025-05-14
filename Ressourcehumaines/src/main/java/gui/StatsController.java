package gui;

import entities.avance;
import services.avanceservice;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatsController {

    @FXML
    private BarChart<String, Number> montantChart;

    @FXML
    private BarChart<String, Number> urgenceChart;

    @FXML
    private Label titleLabel;

    private avanceservice avanceService;

    @FXML
    public void initialize() {
        avanceService = new avanceservice();
        loadChartData();
    }

    private void loadChartData() {
        try {
            List<avance> avances = avanceService.readAll();

            // Montant Statistics (group by ranges)
            Map<String, Integer> montantRanges = new HashMap<>();
            for (avance avance : avances) {
                BigDecimal montant = avance.getMontant();
                String range = getMontantRange(montant);
                montantRanges.put(range, montantRanges.getOrDefault(range, 0) + 1);
            }

            XYChart.Series<String, Number> montantSeries = new XYChart.Series<>();
            montantSeries.setName("Montants");
            montantRanges.forEach((range, count) ->
                    montantSeries.getData().add(new XYChart.Data<>(range, count))
            );
            montantChart.getData().add(montantSeries);

            // NiveauUrgence Statistics
            Map<String, Integer> urgenceCounts = new HashMap<>();
            for (avance avance : avances) {
                String urgence = avance.getNiveauUrgence();
                urgenceCounts.put(urgence, urgenceCounts.getOrDefault(urgence, 0) + 1);
            }

            XYChart.Series<String, Number> urgenceSeries = new XYChart.Series<>();
            urgenceSeries.setName("Urgence");
            urgenceCounts.forEach((urgence, count) ->
                    urgenceSeries.getData().add(new XYChart.Data<>(urgence, count))
            );
            urgenceChart.getData().add(urgenceSeries);

            // Customize chart appearance
            montantChart.setLegendVisible(false);
            montantChart.setBarGap(5);
            montantChart.setCategoryGap(20);
            urgenceChart.setLegendVisible(false);
            urgenceChart.setBarGap(5);
            urgenceChart.setCategoryGap(20);

            titleLabel.setText("Statistiques des Avances (Total: " + avances.size() + ")");

        } catch (SQLException e) {
            e.printStackTrace();
            titleLabel.setText("Erreur lors du chargement des statistiques");
        }
    }

    private String getMontantRange(BigDecimal montant) {
        int value = montant.intValue();
        if (value < 500) return "< 500 TND";
        if (value < 1000) return "500-999 TND";
        if (value < 2000) return "1000-1999 TND";
        if (value < 5000) return "2000-4999 TND";
        return "≥ 5000 TND";
    }

    @FXML
    private void handleRetour() {
        try {
            Parent avancePane = FXMLLoader.load(getClass().getResource("/fxml/avance.fxml"));
            Stage stage = (Stage) montantChart.getScene().getWindow();
            stage.setScene(new Scene(avancePane));
            stage.setTitle("Gestion des Avances");
        } catch (IOException e) {
            e.printStackTrace();
            titleLabel.setText("Erreur lors du retour à l'interface des avances");
        }
    }
}