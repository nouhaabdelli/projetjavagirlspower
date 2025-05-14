package gui;

import entities.Certificat;
import entities.Formation;
import entities.User;
import services.CertificatService;
import services.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CertificatController {

    @FXML private TableView<Certificat> certificatTable;
    @FXML private TableColumn<Certificat, String> titreCol, descCol, niveauCol, expirationCol, usernameCol;
    @FXML private TableColumn<Certificat, Integer> anneesCol;

    @FXML private TextField titreField, validiteField, usernameField, codeCertificatField, searchField;
    @FXML private TextArea descriptionField;
    @FXML private ComboBox<String> niveauComboBox;
    @FXML private ComboBox<Integer> userIdComboBox;
    @FXML private DatePicker dateExpirationPicker;
    @FXML private ComboBox<Integer> formationIdComboBox;


    private final ObservableList<Certificat> certificatList = FXCollections.observableArrayList();
    private final CertificatService certificatService = new CertificatService();
    private final Map<Integer, String> userMap = new HashMap<>();
    private final Map<Integer, String> formationMap = new HashMap<>();

    @FXML
    public void initialize() throws SQLException {
        setupTable();

        loadData();
    }
    FormationService formationService = new FormationService();


    private void setupTable() {
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        niveauCol.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        expirationCol.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        anneesCol.setCellValueFactory(new PropertyValueFactory<>("validiteAnnees"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        certificatTable.setItems(certificatList);
    }





    private void loadData() throws SQLException {
        certificatList.clear();
        certificatList.addAll(certificatService.afficherCertificats());
    }


    @FXML
    private void addCertificat() throws SQLException {
        Certificat cert = getCertificatFromForm();
        cert.setUsername(userMap.getOrDefault(cert.getUserId(), ""));

        certificatService.ajouterCertificat(cert);
        loadData();
        clearFields();
    }
    public void setFormData(Certificat certificat) {
        codeCertificatField.setText(String.valueOf(certificat.getCodeCertificat()));
        titreField.setText(certificat.getTitre());
        descriptionField.setText(certificat.getDescription());
        niveauComboBox.setValue(certificat.getNiveau());
        validiteField.setText(String.valueOf(certificat.getValiditeAnnees()));
        // etc.
    }

    @FXML
    private void getSelected(MouseEvent event) {

    }



    @FXML
    private void updateCertificat(ActionEvent event) {
        try {
            Certificat selected = certificatTable.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditFormCertif.fxml"));
            Parent root = loader.load();

            EditCertificatController editController = loader.getController();
            editController.setCertificat(selected);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier Certificat");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Save edited certificate
            certificatService.modifierCertificat(selected); // Add this method if needed
            loadData();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void deleteCertificat() throws SQLException {
        Certificat selected = certificatTable.getSelectionModel().getSelectedItem();

        int code = selected.getCodeCertificat();
        certificatService.supprimerCertificat(code);
        loadData();
        clearFields();
    }

    @FXML
    private void searchCertificat(KeyEvent event) {
        String keyword = searchField.getText().toLowerCase();
        if (keyword.isEmpty()) {
            certificatTable.setItems(certificatList);
        } else {
            ObservableList<Certificat> filtered = FXCollections.observableArrayList();
            for (Certificat c : certificatList) {
                if (c.getTitre().toLowerCase().contains(keyword)
                        || c.getDescription().toLowerCase().contains(keyword)
                        || c.getNiveau().toLowerCase().contains(keyword)) {
                    filtered.add(c);
                }
            }
            certificatTable.setItems(filtered);
        }
    }

    private Certificat getCertificatFromForm() {
        return new Certificat(
                titreField.getText(),
                descriptionField.getText(),
                dateExpirationPicker.getValue().toString(),
                niveauComboBox.getValue(),
                Integer.parseInt(validiteField.getText()),
                usernameField.getText(),
                userIdComboBox.getValue(),
                formationIdComboBox.getValue()
        );
    }

    private void clearFields() {

    }
}
