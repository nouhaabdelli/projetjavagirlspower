package gui;

import entities.Annonce;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.AnnonceService;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;


public class ModifierAnnonce {
    @FXML
    private TextField titre;

    @FXML
    private TextArea contenu;

    @FXML
    private DatePicker cal;

    @FXML
    private TextField picejoin;

    private Annonce selectedAnnonce;
    private final AnnonceService annonceService = new AnnonceService();


    public void initialize(Annonce selectedAnnonce) {
        this.selectedAnnonce = selectedAnnonce;


        titre.setText(selectedAnnonce.getTitre());
        contenu.setText(selectedAnnonce.getContenu());
        cal.setValue(LocalDate.now());
        picejoin.setText(selectedAnnonce.getPieceJointe());
    }

    @FXML
    void soumettre(ActionEvent event) {

        String newTitre = titre.getText();
        String newContenu = contenu.getText();
        String newPieceJointe = picejoin.getText();
        String newDate = cal.getValue().toString();


        selectedAnnonce.setTitre(newTitre);
        selectedAnnonce.setContenu(newContenu);
        selectedAnnonce.setPieceJointe(newPieceJointe);
        selectedAnnonce.setDatePublication(Timestamp.valueOf(newDate + " 00:00:00").toLocalDateTime());

        try {
            annonceService.update(selectedAnnonce);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Modification réussie");
            successAlert.setHeaderText(null);
            successAlert.setContentText("L'annonce a été modifiée avec succès.");
            successAlert.showAndWait();


            Stage stage = (Stage) titre.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            e.printStackTrace();

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Erreur lors de la mise à jour");
            errorAlert.setContentText("Une erreur est survenue lors de la mise à jour de l'annonce.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    void annuler(ActionEvent event) {

        Stage stage = (Stage) titre.getScene().getWindow();
        stage.close();
    }

    @FXML
    void parcourir(ActionEvent event) {

    }
}
