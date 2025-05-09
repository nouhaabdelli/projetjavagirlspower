package gui;

import entities.Evenement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.EvenementService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class CalendrierEvenement {
    private final EvenementService evenementService = new EvenementService();

    @FXML
    private GridPane gridCalendar;

    @FXML
    private Label calEv;

    @FXML
    public void initialize() {
        afficherCalendrier(YearMonth.now());
    }

    public void afficherCalendrier(YearMonth month) {
        gridCalendar.getChildren().clear();

        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        for (int i = 0; i < jours.length; i++) {
            Label label = new Label(jours[i]);
            label.getStyleClass().add("grid-header");
            gridCalendar.add(label, i, 0);
        }

        LocalDate premierJour = month.atDay(1);
        int jourSemaine = premierJour.getDayOfWeek().getValue();

        int numJour = 1;
        for (int row = 1; numJour <= month.lengthOfMonth(); row++) {
            for (int col = (row == 1 ? jourSemaine - 1 : 0); col < 7; col++) {
                if (numJour > month.lengthOfMonth()) break;

                LocalDate dateDuJour = month.atDay(numJour);
                Label labelDate = new Label(String.valueOf(numJour));
                labelDate.getStyleClass().add("label-date");

                VBox cell = new VBox();
                cell.getStyleClass().add("vbox-cell");
                cell.getChildren().add(labelDate);

                List<Evenement> evenements = getEvenementsForDate(dateDuJour);
                for (Evenement evt : evenements) {
                    Label labelEvt = new Label(evt.getNomEvenement());
                    labelEvt.getStyleClass().add("label-evenement");

                    labelEvt.setOnMouseClicked(event -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsEvent.fxml"));
                            Parent root = loader.load();

                            DetailsEvenement controller = loader.getController();
                            controller.setDetails(evt);

                            Stage stage = new Stage();
                            stage.setTitle("Détails de l'événement");
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    cell.getChildren().add(labelEvt);
                }

                gridCalendar.add(cell, col, row);
                numJour++;
            }
        }
    }

    public List<Evenement> getEvenementsForDate(LocalDate date) {
        try {
            return evenementService.readAll().stream()
                    .filter(e -> e.getDateDebut().toLocalDate().isEqual(date))
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @FXML
    public void Retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenements.fxml"));
            Parent root = loader.load();

            Scene currentScene = gridCalendar.getScene();
            Stage currentStage = (Stage) currentScene.getWindow();

            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

