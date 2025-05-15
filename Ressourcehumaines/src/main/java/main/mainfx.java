package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class mainfx extends Application {

    public static void main(String[] args) {
        launch(args);  // Lancer l'application JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger l'interface principale "annonces.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/maquette.fxml"));
            Parent root = loader.load();

            // Créer et configurer la scène
            Scene scene = new Scene(root);

            // Afficher la fenêtre principale
            primaryStage.setTitle("Gestion des Annonces");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();  // Afficher les erreurs de chargement
        }
    }
}


/*
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenements.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/gestionannonce.css").toExternalForm());


            primaryStage.setTitle("Gestion des Événements");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
/*package main ;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TestFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML pour le Dashboard
        BorderPane root = FXMLLoader.load(getClass().getResource("/Dashboard.fxml"));

        // Créer une scène et l'attacher à la fenêtre principale
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dashboard Application");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  // Lancer l'application
    }
}
*/
