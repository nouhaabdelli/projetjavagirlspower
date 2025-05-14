package gui;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;

import java.util.function.Consumer;

public class MapPopupController {

    @FXML
    private WebView mapWebView;

    private Consumer<String> onLieuSelected;

    public void setOnLieuSelected(Consumer<String> callback) {
        this.onLieuSelected = callback;
    }

    @FXML
    public void initialize() {
        WebEngine webEngine = mapWebView.getEngine();
        String url = getClass().getResource("/map.html").toExternalForm();
        webEngine.load(url);

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");

                // Pont Java-JS
                window.setMember("java", new javaBridge((lieu) -> {
                    if (onLieuSelected != null) {
                        javafx.application.Platform.runLater(() -> {
                            onLieuSelected.accept(lieu);

                            // Fermer la fenêtre de carte
                            Stage stage = (Stage) mapWebView.getScene().getWindow();
                            stage.close();
                        });
                    }
                }));
            }
        });
    }
}
