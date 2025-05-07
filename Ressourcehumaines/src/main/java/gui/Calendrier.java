package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import entities.Annonce;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import services.AnnonceService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class Calendrier {
    private AnnonceService annonceService = new AnnonceService();

    @FXML
    private GridPane gridCalendar;

    @FXML
    private Label CalAnn;

    @FXML
    public void initialize() {
        afficherCalendrier(YearMonth.now());
    }

    public void afficherCalendrier(YearMonth month) {
        gridCalendar.getChildren().clear();

        // ✅ Ajouter les en-têtes des jours
        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        for (int i = 0; i < jours.length; i++) {
            Label label = new Label(jours[i]);
            label.getStyleClass().add("grid-header");
            gridCalendar.add(label, i, 0); // Ligne 0 pour les en-têtes
        }

        LocalDate premierJourDuMois = month.atDay(1);
        int jourDeLaSemaine = premierJourDuMois.getDayOfWeek().getValue(); // 1 = Lundi

        int numJour = 1;
        for (int row = 1; numJour <= month.lengthOfMonth(); row++) {
            for (int col = (row == 1 ? jourDeLaSemaine - 1 : 0); col < 7; col++) {
                if (numJour > month.lengthOfMonth()) break;

                // ✅ Date du jour
                LocalDate dateDuJour = month.atDay(numJour);

                // ✅ Titre du jour (ex: 1, 2, 3...)
                Label labelDate = new Label(String.valueOf(numJour));
                labelDate.getStyleClass().add("label-date");

                VBox cell = new VBox();
                cell.getStyleClass().add("vbox-cell");
                cell.getChildren().add(labelDate);

                // ✅ Ajouter les annonces du jour
                List<Annonce> annonces = getAnnoncesForDate(dateDuJour);
                for (Annonce annonce : annonces) {
                    Label labelAnnonce = new Label(annonce.getTitre());
                    labelAnnonce.getStyleClass().add("label-annonce");
                    cell.getChildren().add(labelAnnonce);
                }

                gridCalendar.add(cell, col, row);

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
        // Charger l'interface de gestion des annonces
        try {
            // Exemple de chargement d'une nouvelle scène
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/annonces.fxml"));
            Parent root = loader.load();

            // Récupérer la scène actuelle
            Scene currentScene = gridCalendar.getScene();
            Stage currentStage = (Stage) currentScene.getWindow();

            // Changer la scène
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
