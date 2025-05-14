package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Statistique {

    @FXML
    private TableColumn<?, ?> colActions;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDebut;

    @FXML
    private TableColumn<?, ?> colDuree;

    @FXML
    private TableColumn<?, ?> colFin;

    @FXML
    private Label labelConnexions;

    @FXML
    private Label labelTempsTotal;

    @FXML
    private LineChart<?, ?> lineChartTemps;

    @FXML
    private Button rafraichirButton;

    @FXML
    private Button retourButton;

    @FXML
    private TableView<?> tableHistorique;

    @FXML
    void rafraichirStatistiques(ActionEvent event) {

    }

    @FXML
    void retourAccueil(ActionEvent event) {

    }

}
