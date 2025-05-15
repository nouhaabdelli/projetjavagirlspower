package gui;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.UserService;

import java.util.Map;

public class UserStatisticsController {

    @FXML
    private PieChart rolePieChart;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        loadRoleStatistics();
    }

    private void loadRoleStatistics() {
        Map<String, Integer> roleCounts = userService.getUserRoleCounts();
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : roleCounts.entrySet()) {
            data.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        rolePieChart.setData(data);
    }

    @FXML
    private void refresh() {
        loadRoleStatistics();
    }
}
