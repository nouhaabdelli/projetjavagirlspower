package gui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.io.IOException;

public class frontoffice {

    @FXML
    private AnchorPane dynamicContent;

    @FXML
    private Button AjouterReclamations;

    @FXML
    private Button MesReclamations;


    @FXML
    private AnchorPane rootPane;


    @FXML
    private AnchorPane contentArea;

    @FXML
    private Button btnSidebarToggle;


    @FXML
    private VBox sidebar;
    @FXML

    private ToggleButton themeToggle;

    private boolean isSidebarOpen = false;

    private PauseTransition autoCloseTimer;
    private boolean isTransitioning = false;

    @FXML
    public void initialize() {

        setupEventHandlers();
        rootPane.getStyleClass().remove("alt-theme");
        themeToggle.setText("🌙");
        // Initialize auto-close timer
        autoCloseTimer = new PauseTransition(Duration.seconds(20));
        autoCloseTimer.setOnFinished(event -> {
            if (isSidebarOpen && !isTransitioning) {
                toggleSidebar();
                System.out.println("Sidebar auto-collapsed after 3 seconds");
            }
        });
    }
    private void setupEventHandlers() {
        if (btnSidebarToggle != null) {
            btnSidebarToggle.setOnAction(event -> toggleSidebar());
        } else {
            System.err.println("btnSidebarToggle is null");
        }

        themeToggle.setOnAction(event -> toggleTheme());
        AjouterReclamations.setOnAction(e -> loadAjoutReclamation());
        MesReclamations.setOnAction(e -> MesReclamations());
    }
    private void toggleSidebar() {
        if (sidebar == null || contentArea == null ) {
            System.err.println("Cannot toggle sidebar: sidebar=" + sidebar + ", contentArea=" + contentArea + ", tableView=" );
            return;
        }
        if (isTransitioning) {
            System.out.println("Toggle ignored: Transition in progress");
            return;
        }
        isTransitioning = true;
        autoCloseTimer.stop();
        TranslateTransition sidebarTransition = new TranslateTransition(Duration.millis(300), sidebar);
        FadeTransition fade = new FadeTransition(Duration.millis(300), sidebar);

        if (isSidebarOpen) {
            // Collapse
            sidebarTransition.setToX(-250);
            fade.setToValue(0.5);
//            sidebar.setPrefWidth(50);
            contentArea.setLayoutX(60.0);
            contentArea.setPrefWidth(1200.0);
            isSidebarOpen = false;
        } else {
            // Open
            sidebarTransition.setToX(0);
            fade.setToValue(1.0);
            sidebar.setPrefWidth(300);
            contentArea.setLayoutX(310.0);
            contentArea.setPrefWidth(1070.0);
            isSidebarOpen = true;
            autoCloseTimer.playFromStart();

        }
        sidebarTransition.setOnFinished(e -> isTransitioning = false);
        sidebarTransition.play();
        fade.play();
    }

    private void toggleTheme() {
        if (themeToggle.isSelected()) {
            rootPane.getStyleClass().add("alt-theme");
            themeToggle.setText("☀");
        } else {
            rootPane.getStyleClass().remove("alt-theme");
            themeToggle.setText("🌙");
        }
    }




    private void loadAjoutReclamation() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AjouterReclamation.fxml"));
            FadeTransition fade = new FadeTransition(Duration.millis(300), dynamicContent);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> {
                dynamicContent.getChildren().setAll(root);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dynamicContent);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fade.play();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Échec du chargement du formulaire");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void MesReclamations() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ListeReclamations.fxml"));

            FadeTransition fade = new FadeTransition(Duration.millis(300), dynamicContent);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> {
                dynamicContent.getChildren().setAll(root);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dynamicContent);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fade.play();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Échec du chargement du formulaire");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }



}
