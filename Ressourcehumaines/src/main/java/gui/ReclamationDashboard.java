package gui;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
public class ReclamationDashboard {
    @FXML
    private VBox sidebar;
//    @FXML
//    private VBox sideMenu;

    @FXML
    private Label titrePrincipal;
    private SplitPane splitPane;
    private boolean isSidebarVisible = false;
    @FXML
    private AnchorPane mainContent; // Contenu principal à droite du SplitPane

    @FXML
    public void initialize() {

        toggleSidebar();

    }


    public void showAjouter() throws IOException {
        Parent ajout = FXMLLoader.load(getClass().getResource("/fxml/AjouterReclamation.fxml"));
        mainContent.getChildren().setAll(ajout);
    }

    public void showListe() throws IOException {
        Parent liste = FXMLLoader.load(getClass().getResource("/fxml/ListeReclamations.fxml"));

        // Ajouter la vue dans le mainContent (la partie droite du SplitPane)
        mainContent.getChildren().setAll(liste);
        AnchorPane.setLeftAnchor(liste, 45.0);
        AnchorPane.setTopAnchor(liste, 0.0);
        AnchorPane.setRightAnchor(liste, 0.0);
        AnchorPane.setBottomAnchor(liste, 0.0);

    }

    public void showReponses() throws IOException {
        Parent reponse = FXMLLoader.load(getClass().getResource("/fxml/reponseReclamations.fxml"));
        mainContent.getChildren().setAll(reponse);
    }

    @FXML
    private void toggleSidebar() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), sidebar);
        if (isSidebarVisible) {
            transition.setToX(-230.0);
            isSidebarVisible = false;
        } else {
            transition.setToX(0.0);
            isSidebarVisible = true;
        }
        transition.play();
    }





}



