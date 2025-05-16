package gui;

import entities.Annonce;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.AnnonceService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class calendrierAnnonce {
    private final AnnonceService annonceService = new AnnonceService();

    @FXML
    private GridPane gridCalendarAnnonce;

    @FXML
    private Label titreCalAnnonce;

    @FXML
    public void initialize() {
        afficherCalendrier(YearMonth.now());
    }

    public void afficherCalendrier(YearMonth month) {
        gridCalendarAnnonce.getChildren().clear();

        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        for (int i = 0; i < jours.length; i++) {
            Label label = new Label(jours[i]);
            label.getStyleClass().add("grid-header");
            gridCalendarAnnonce.add(label, i, 0);
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

                List<Annonce> annonces = getAnnoncesForDate(dateDuJour);
                for (Annonce annonce : annonces) {
                    Label labelAnn = new Label(annonce.getTitre());
                    labelAnn.getStyleClass().add("label-annonce");

                    labelAnn.setOnMouseClicked(event -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsAnnonce.fxml"));
                            Parent root = loader.load();

                            DetailsAnnonce controller = loader.getController();
                            controller.setDetails(annonce);

                            Stage stage = new Stage();
                            stage.setTitle("Détails de l'annonce");
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    cell.getChildren().add(labelAnn);
                }

                gridCalendarAnnonce.add(cell, col, row);
                numJour++;
            }
        }
    }

    public List<Annonce> getAnnoncesForDate(LocalDate date) {
        try {
            return annonceService.readAll().stream()
                    .filter(a -> a.getDatePublication().toLocalDate().isEqual(date))
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @FXML
    public void Retour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/annonces.fxml"));
            Parent root = loader.load();

            Scene currentScene = gridCalendarAnnonce.getScene();
            Stage currentStage = (Stage) currentScene.getWindow();

            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
