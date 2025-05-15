package main;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/Frontlistepret.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        if (getClass().getResource("/style/finance.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
            System.out.println("finance.css chargé avec succès dans MainApp");
        } else {
            System.err.println("Erreur : Le fichier finance.css n'est pas trouvé dans les ressources. Vérifiez l'emplacement.");
        }
        primaryStage.setTitle("Finance");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}