package gui;
import entities.Reponses;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import services.ReponseService;
listReclaReponse
import java.io.File;
import java.time.LocalDate;
public class AjouterReponse {
    @FXML private TextField titre;
    @FXML private TextArea contenu;
    @FXML private DatePicker dateReponse;
    @FXML private ComboBox<String> importance;
    @FXML private TextField fichierJoint;

    private int reclamationId; // À set avant soumission
    private final ReponseService service = new ReponseService();

    @FXML
    public void initialize() {
        importance.getItems().addAll("Faible", "Moyenne", "Élevée");
        importance.getSelectionModel().selectFirst();
    }

    public void setReclamationId(int id) {
        this.reclamationId = id;
    }

    @FXML
    private void parcourirFichier(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.docx", "*.txt"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fichierJoint.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void soumettre(ActionEvent event) {
        try {
            String titreTxt = titre.getText().trim();
            String contenuTxt = contenu.getText().trim();
            LocalDate date = dateReponse.getValue();
            String importanceTxt = importance.getValue();
            String fichier = fichierJoint.getText().trim();

            // Vérifications
            if (titreTxt.isEmpty() || contenuTxt.isEmpty() || date == null) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            // Création de la réponse
            Reponses r = new Reponses(0, titreTxt, contenuTxt, date, null, importanceTxt, fichier, reclamationId);
            service.create(r);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "La réponse a été ajoutée avec succès.");
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l’ajout.");
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        titre.clear();
        contenu.clear();
        dateReponse.setValue(null);
        importance.getSelectionModel().selectFirst();
        fichierJoint.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
