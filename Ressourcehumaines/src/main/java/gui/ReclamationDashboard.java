package gui;


import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


import java.io.IOException;


public class ReclamationDashboard {

    @FXML
    private VBox sideMenu;

    @FXML
    private Label titrePrincipal;
    private SplitPane splitPane;

    @FXML
    private AnchorPane mainContent; // Contenu principal à droite du SplitPane

    // Méthode pour afficher "Ajouter Réclamation"
    public void showAjouter() throws IOException {
        // Charger la vue "Ajouter Réclamation"
        Parent ajout = FXMLLoader.load(getClass().getResource("/fxml/AjouterReclamation.fxml"));

        // Ajouter la vue dans le mainContent (la partie droite du SplitPane)
        mainContent.getChildren().setAll(ajout);
    }

    // Méthode pour afficher "Liste Réclamations"
    public void showListe() throws IOException {
        // Charger la vue "Liste Réclamations"
        Parent liste = FXMLLoader.load(getClass().getResource("/fxml/listeReclamations.fxml"));

        // Ajouter la vue dans le mainContent (la partie droite du SplitPane)
        mainContent.getChildren().setAll(liste);
        AnchorPane.setLeftAnchor(liste, 45.0);
        AnchorPane.setTopAnchor(liste, 0.0);
        AnchorPane.setRightAnchor(liste, 0.0);
        AnchorPane.setBottomAnchor(liste, 0.0);

    }

    // Méthode pour afficher "Réponses Réclamations"
    public void showReponses() throws IOException {
        // Charger la vue "Réponses Réclamations"
        Parent reponse = FXMLLoader.load(getClass().getResource("/fxml/reponseReclamations.fxml"));

        // Ajouter la vue dans le mainContent (la partie droite du SplitPane)
        mainContent.getChildren().setAll(reponse);
    }
}



