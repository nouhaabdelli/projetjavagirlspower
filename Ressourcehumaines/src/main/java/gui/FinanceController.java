package gui;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class FinanceController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button pretButton;

    @FXML
    private Button avanceButton;

    @FXML
    private ToggleButton themeToggle;

    @FXML
    private ImageView logo;

    private boolean isDarkTheme = false;

    @FXML
    public void initialize() {
        // Animation pulsation logo
        ScaleTransition logoPulse = new ScaleTransition(Duration.millis(2000), logo);
        logoPulse.setToX(1.1);
        logoPulse.setToY(1.1);
        logoPulse.setCycleCount(ScaleTransition.INDEFINITE);
        logoPulse.setAutoReverse(true);
        logoPulse.play();
    }

    @FXML
    private void handlePretButton() throws IOException {
        loadInterface("fxml/pret.fxml");
    }

    @FXML
    private void handleAvanceButton() throws IOException {
        loadInterface("fxml/avance.fxml");
    }

    @FXML
    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        rootPane.getStylesheets().clear();
        if (getClass().getResource("/style/finance.css") != null) {
            rootPane.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            System.out.println("finance.css chargé avec succès dans toggleTheme");
        } else {
            System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources. Vérifiez l'emplacement.");
        }
        if (isDarkTheme) {
            rootPane.getStyleClass().add("dark-theme");
            themeToggle.setText("Mode Clair");
        } else {
            rootPane.getStyleClass().remove("dark-theme");
            themeToggle.setText("Mode Sombre");
        }
    }

    private void loadInterface(String fxmlFile) throws IOException {
        if (getClass().getResource("/" + fxmlFile) == null) {
            System.err.println("Erreur : Le fichier " + fxmlFile + " n'est pas trouvé dans les ressources.");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage) rootPane.getScene().getWindow();
        Scene scene = new Scene(root);
        if (getClass().getResource("/style/finance.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            System.out.println("finance.css chargé avec succès dans " + fxmlFile);
        } else {
            System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources. Vérifiez l'emplacement.");
        }
        stage.setScene(scene);
        stage.show();
    }
}