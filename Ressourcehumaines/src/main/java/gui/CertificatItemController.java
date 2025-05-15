package gui;

import entities.Certificat;
import main.MyListener2;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.SQLException;

public class CertificatItemController {
    @FXML private Label titleLabel;
    @FXML private Label dateLabel;

    @FXML private Label diff;
    @FXML private Label descriptionLabel;

    private Certificat certificat;
    private MyListener2 listener;

    public void setData(Certificat certificat, MyListener2 listener) {
        this.certificat = certificat;
        this.listener = listener;
        titleLabel.setText(certificat.getTitre());
        dateLabel.setText(certificat.getCreatedAt().toString());
        descriptionLabel.setText(certificat.getDescription());
        diff.setText(certificat.getNiveau());
    }
    public void click(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
        listener.onClickListener(certificat);
    }
    @FXML
    private void handleClick() throws SQLException {
        if (listener != null) {
            listener.onClickListener(certificat);
        }
    }
}
