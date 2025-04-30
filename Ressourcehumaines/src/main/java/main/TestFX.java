package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestFX extends Application {

    public static void main(String[] args) {
        launch(args);  // Lancer l'application JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterAnnonce.fxml"));
            Parent root = loader.load();

            // Créer la scène et l'afficher
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            primaryStage.setTitle("Ajouter Annonce");
            primaryStage.setScene(scene);
            primaryStage.show();  // Afficher la fenêtre

        } catch (Exception e) {
            e.printStackTrace();  // Afficher l'erreur si le chargement échoue
        }
    }
}
