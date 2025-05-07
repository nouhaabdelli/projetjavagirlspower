package gui;

import entities.avance;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.avanceservice;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class ajouteravancecontroller {
    @FXML
    private AnchorPane mainContent;

    @FXML
    private TextField montantField;

    @FXML
    private TextField dureeField;

    @FXML
    private DatePicker dateAvancePicker;

    @FXML
    private TextField niveauUrgenceField;

    @FXML
    private TextField etatField;

    @FXML
    private void handleRetour() {
        try {
            System.out.println("Tentative de retour à avance.fxml depuis ajouteravance.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/avance.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
                System.out.println("finance.css chargé avec succès");
            } else {
                System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources.");
            }
            newStage.setScene(scene);
            newStage.show();
            Stage currentStage = (Stage) mainContent.getScene().getWindow();
            if (currentStage != null) {
                currentStage.close();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de avance.fxml dans ajouteravancecontroller : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void ajouterAvance() {
        try {
            // Valider les champs
            if (montantField.getText().isEmpty() || dureeField.getText().isEmpty() ||
                    dateAvancePicker.getValue() == null || niveauUrgenceField.getText().isEmpty() ||
                    etatField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Tous les champs doivent être remplis !");
                alert.showAndWait();
                return;
            }

            // Créer une nouvelle avance
            avance nouvelleAvance = new avance();
            nouvelleAvance.setMontant(new BigDecimal(montantField.getText()));
            nouvelleAvance.setDuree(Integer.parseInt(dureeField.getText()));
            nouvelleAvance.setDateAvance(dateAvancePicker.getValue());
            nouvelleAvance.setNiveauUrgence(niveauUrgenceField.getText());
            nouvelleAvance.setEtat(etatField.getText());

            // Ajouter à la base de données
            avanceservice avanceService = new avanceservice();
            avanceService.create(nouvelleAvance);

            // Afficher une confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Avance ajoutée avec succès !");
            alert.showAndWait();

            // Réinitialiser les champs
            montantField.clear();
            dureeField.clear();
            dateAvancePicker.setValue(null);
            niveauUrgenceField.clear();
            etatField.clear();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Montant et Durée doivent être des nombres valides !");
            alert.showAndWait();
            e.printStackTrace();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout : " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}