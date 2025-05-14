package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.UserSession;

import com.google.firebase.database.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;

public class Statistique implements Initializable {

    @FXML
    private TableColumn<StatistiqueModel, String> colActions;

    @FXML
    private TableColumn<StatistiqueModel, LocalDateTime> colDate;

    @FXML
    private TableColumn<StatistiqueModel, LocalTime> colDebut;

    @FXML
    private TableColumn<StatistiqueModel, Integer> colDuree;

    @FXML
    private TableColumn<StatistiqueModel, LocalTime> colFin;

    @FXML
    private Label labelConnexions;

    @FXML
    private Label labelTempsTotal;

    @FXML
    private LineChart<String, Number> lineChartTemps;

    @FXML
    private Button rafraichirButton;

    @FXML
    private Button retourButton;

    @FXML
    private TableView<StatistiqueModel> tableHistorique;

    private ObservableList<StatistiqueModel> statistiquesList = FXCollections.observableArrayList();
    private DatabaseReference dbRef;
    private String userEmail;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize Firebase reference
        userEmail = UserSession.getInstance().getEmail();
        dbRef = FirebaseDatabase.getInstance().getReference("statistiques/" + userEmail.replace(".", "_"));

        // Initialize table columns
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateHeure"));
        colDebut.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
        colDuree.setCellValueFactory(new PropertyValueFactory<>("dureeMinutes"));
        colActions.setCellValueFactory(new PropertyValueFactory<>("actions"));

        // Bind the table to the data
        tableHistorique.setItems(statistiquesList);

        // Initial load of statistics
        chargerStatistiques();
    }

    private void chargerStatistiques() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                statistiquesList.clear();
                
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> data = (Map<String, Object>) snapshot.getValue();
                    
                    StatistiqueModel stat = new StatistiqueModel(
                        LocalDateTime.parse((String) data.get("dateHeure")),
                        LocalTime.parse((String) data.get("heureDebut")),
                        LocalTime.parse((String) data.get("heureFin")),
                        ((Long) data.get("dureeMinutes")).intValue(),
                        (String) data.get("actions")
                    );
                    
                    statistiquesList.add(stat);
                }
                
                updateStatisticsLabels();
                updateChart();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Erreur de chargement des statistiques: " + error.getMessage());
            }
        });
    }

    public static void enregistrerSession(String email, LocalTime debut, LocalTime fin, String action) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
            .getReference("statistiques/" + email.replace(".", "_"))
            .push();

        Map<String, Object> data = new HashMap<>();
        data.put("dateHeure", LocalDateTime.now().toString());
        data.put("heureDebut", debut.toString());
        data.put("heureFin", fin.toString());
        data.put("dureeMinutes", fin.toSecondOfDay() / 60 - debut.toSecondOfDay() / 60);
        data.put("actions", action);

        dbRef.setValueAsync(data);
    }

    private void updateStatisticsLabels() {
        int totalConnexions = statistiquesList.size();
        int tempsTotal = statistiquesList.stream()
            .mapToInt(StatistiqueModel::getDureeMinutes)
            .sum();

        labelConnexions.setText("Nombre de connexions : " + totalConnexions);
        labelTempsTotal.setText("Temps total passé : " + tempsTotal + " minutes");
    }

    private void updateChart() {
        lineChartTemps.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Temps d'utilisation");

        // Group by date and calculate daily usage
        Map<String, Integer> dailyUsage = new HashMap<>();
        statistiquesList.forEach(stat -> {
            String date = stat.getDateHeure().toLocalDate().toString();
            dailyUsage.merge(date, stat.getDureeMinutes(), Integer::sum);
        });

        dailyUsage.forEach((date, minutes) -> {
            series.getData().add(new XYChart.Data<>(date, minutes));
        });

        lineChartTemps.getData().add(series);
    }

    @FXML
    void rafraichirStatistiques(ActionEvent event) {
        chargerStatistiques();
    }

    @FXML
    void retourAccueil(ActionEvent event) {
        // TODO: Implement navigation back to home screen
    }
}
