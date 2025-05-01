/*package main;

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
            // Charger l'interface principale "annonces.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/annonces.fxml"));
            Parent root = loader.load();

            // Créer et configurer la scène
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/gestionannonce.css").toExternalForm());

            // Afficher la fenêtre principale
            primaryStage.setTitle("Gestion des Annonces");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();  // Afficher les erreurs de chargement
        }
    }
}*/

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
            // Charger l'interface "evenements.fxml" (au lieu de "annonces.fxml")
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenements.fxml"));  // Modifiez le chemin vers votre fichier FXML
            Parent root = loader.load();

            // Créer et configurer la scène
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/gestionannonce.css").toExternalForm());  // Modifiez le chemin CSS si nécessaire

            // Afficher la fenêtre principale
            primaryStage.setTitle("Gestion des Événements");  // Modifiez le titre de la fenêtre
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();  // Afficher les erreurs de chargement
        }
    }
}


