package main;

import services.pretservice;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class mainfx extends Application {

    private pretservice pretService = new pretservice();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listeavance.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("FXML file '/fxml/listepret.fxml' not found in resources");
            }
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Gestion Prêt");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Échec du chargement du fichier FXML");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}