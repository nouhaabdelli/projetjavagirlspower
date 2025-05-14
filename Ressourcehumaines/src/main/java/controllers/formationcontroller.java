package com.esprit.controllers;

import com.esprit.models.Certificat;
import com.esprit.models.Formation;
import com.esprit.models.User;
import com.esprit.services.CertificatService;
import com.esprit.services.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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
        loadNiveauComboBox();
        loadUsers();
        loadData();
        loadFormations();
    }
    FormationService formationService = new FormationService();
    private void loadFormations() throws SQLException {

        formationMap.clear();
        formationIdComboBox.getItems().clear();
        List<Formation> formations = formationService.afficherFormations();
        for (Formation formation : formations) {
            formationMap.put(formation.getIdFormation(), formation.getTitre());
            formationIdComboBox.getItems().add(formation.getIdFormation());
        }
    }
    private void loadUsers() throws SQLException {
        userMap.clear();
        userIdComboBox.getItems().clear();

        List<User> users = certificatService.getAllUsers();
        for (User user : users) {
            userMap.put(user.getId(), user.getUsername());
            userIdComboBox.getItems().add(user.getId());
        }
    }
    private void setupTable() {
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        niveauCol.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        expirationCol.setCellValueFactory(new PropertyValueFactory<>("dateExpiration"));
        anneesCol.setCellValueFactory(new PropertyValueFactory<>("validiteAnnees"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        certificatTable.setItems(certificatList);
    }

    private void loadNiveauComboBox() {
        niveauComboBox.setItems(FXCollections.observableArrayList("Débutant", "Intermédiaire", "Avancé"));
    }



    private void loadData() throws SQLException {
        certificatList.clear();
        certificatList.addAll(certificatService.afficherCertificats());
    }

    @FXML
    private void onUserIdSelected() {
        Integer selectedId = userIdComboBox.getValue();
        usernameField.setText(userMap.getOrDefault(selectedId, ""));
    }
    @FXML
    private void addCertificat() throws SQLException {
        Certificat cert = getCertificatFromForm();
        cert.setUsername(userMap.getOrDefault(cert.getUserId(), ""));

        certificatService.ajouterCertificat(cert);
        loadData();
        clearFields();
    }
    @FXML
    private void getSelected(MouseEvent event) {
        Certificat selected = certificatTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            codeCertificatField.setText(String.valueOf(selected.getCodeCertificat()));
            titreField.setText(selected.getTitre());
            descriptionField.setText(selected.getDescription());
            niveauComboBox.setValue(selected.getNiveau());
            dateExpirationPicker.setValue(LocalDate.parse(selected.getDateExpiration()));
            validiteField.setText(String.valueOf(selected.getValiditeAnnees()));
            userIdComboBox.setValue(selected.getUserId());
            usernameField.setText(selected.getUsername());
        }
    }



    @FXML
    private void updateCertificat() throws SQLException {
        if (codeCertificatField.getText().isEmpty()) return;
        Certificat cert = getCertificatFromForm();
        cert.setCodeCertificat(Integer.parseInt(codeCertificatField.getText()));
        certificatService.modifierCertificat(cert);
        loadData();
        clearFields();
    }

    @FXML
    private void deleteCertificat() throws SQLException {
        if (codeCertificatField.getText().isEmpty()) return;
        int code = Integer.parseInt(codeCertificatField.getText());
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
        codeCertificatField.clear();
        titreField.clear();
        descriptionField.clear();
        niveauComboBox.setValue(null);
        dateExpirationPicker.setValue(null);
        validiteField.clear();
        userIdComboBox.setValue(null);
        usernameField.clear();
    }
}
