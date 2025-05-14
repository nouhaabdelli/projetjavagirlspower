package gui;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.TextField;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;

public class mapApp {

    @FXML
    private WebView mapWebView;

    @FXML
    private TextField lieuTextField;

    @FXML
    public void initialize() {
        WebEngine webEngine = mapWebView.getEngine();
        String url = getClass().getResource("/map.html").toExternalForm(); // adapte ce chemin selon ton projet
        webEngine.load(url);

        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) webEngine.executeScript("window");
                window.setMember("java", new javaBridge(lieu -> {
                    javafx.application.Platform.runLater(() -> lieuTextField.setText(lieu));
                }));
            }
        });
    }
}
