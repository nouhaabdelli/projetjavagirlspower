package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterUser.fxml")); // Vérifie que le chemin est correct
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Gestion Utilisateurs");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur de chargement FXML : " + e.getMessage());
        }
    }
}

