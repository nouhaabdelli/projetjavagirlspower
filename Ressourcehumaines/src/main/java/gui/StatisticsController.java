package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.AttestationStatistics;
import java.time.YearMonth;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import java.io.IOException;

public class StatisticsController {
    @FXML
    private TabPane tabPane;
    
    @FXML
    private LineChart<String, Number> monthlyChart;
    
    @FXML
    private PieChart typeChart;
    
    @FXML
    private BarChart<String, Number> processingTimeChart;
    
    @FXML
    private TextArea textStats;

    @FXML
    public void initialize() {
        initializeCharts();
        displayStatistics();
    }

    private void initializeCharts() {
        // Initialiser les graphiques
        initializeMonthlyChart();
        initializeTypeChart();
        initializeProcessingTimeChart();
    }

    private void initializeMonthlyChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Attestations par mois");

        Map<YearMonth, Integer> monthlyStats = AttestationStatistics.getAttestationsByMonth();
        for (Map.Entry<YearMonth, Integer> entry : monthlyStats.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }

        monthlyChart.getData().add(series);
    }

    private void initializeTypeChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        Map<String, Map<String, Integer>> typeStats = AttestationStatistics.getAttestationsByType();
        for (Map.Entry<String, Map<String, Integer>> entry : typeStats.entrySet()) {
            int total = entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
            pieChartData.add(new PieChart.Data(entry.getKey(), total));
        }

        typeChart.setData(pieChartData);
        typeChart.setTitle("Distribution par type d'attestation");
    }

    private void initializeProcessingTimeChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Temps moyen de traitement (heures)");

        Map<String, Double> processingTimes = AttestationStatistics.getAverageProcessingTime();
        for (Map.Entry<String, Double> entry : processingTimes.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        processingTimeChart.getData().add(series);
    }

    private void displayStatistics() {
        // Mettre à jour les statistiques textuelles
        textStats.setText(AttestationStatistics.formatStatistics());
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleRefresh() {
        displayStatistics();
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) tabPane.getScene().getWindow();
        stage.close();
    }
} 