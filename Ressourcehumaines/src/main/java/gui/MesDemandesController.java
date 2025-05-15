package gui;
import utils.EnvConfig;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import entities.Demande;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import okhttp3.*;
import org.json.JSONArray;
import services.DemandeDAO;
import utils.GeminiAPIClient;
import entities.Attestation;
import entities.Conge;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONObject;
import java.io.OutputStream;
import entities.Demande;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.json.*;



public class MesDemandesController {

    @FXML
    private TableView<Demande> tableViewDemandes;
    @FXML
    private TableColumn<Demande, Integer> colId;
    @FXML
    private TableColumn<Demande, String> colType;
    @FXML
    private TableColumn<Demande, String> colStatut;
    @FXML
    private TableColumn<Demande, LocalDate> colDateSoumission;
    @FXML
    private TableColumn<Demande, String> colDescription;
    @FXML
    private TableColumn<Demande, LocalDate> colDateValidation;
    @FXML
    private TableColumn<Demande, Void> colActions;
    @FXML
    private Label predictionLabel;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> searchTypeComboBox;
    private int iddemande = 1;
    private int Id;
    private boolean isAdmin;
    private volatile boolean isProcessing = false;
    // Évite les requêtes simultanées
    private static final long DEBOUNCE_DELAY_MS = 1000; // Délai de débordement de 1 seconde
    private long lastClickTime = 0; // Stocke l'heure du dernier clic
    private ObservableList<Demande> observableDemandes = FXCollections.observableArrayList();
    private ObservableList<Demande> filteredDemandes = FXCollections.observableArrayList();


    public MesDemandesController() {
    }

    public void initialize() {
        this.setId(1);  // ID utilisateur par défaut
        this.setAdmin(true);       // Par défaut en mode admin
        this.setupTableColumns();
        this.setupSearch();
        this.loadDemandesFromDB();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDateSoumission.setCellValueFactory(new PropertyValueFactory<>("dateSoumission"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDateValidation.setCellValueFactory(new PropertyValueFactory<>("dateValidation"));

        // Définir une largeur fixe pour la colonne Actions
        colActions.setPrefWidth(600);

        // Configuration de la colonne Actions
        colActions.setCellFactory(param -> new TableCell<Demande, Void>() {
            private final Button btnPredict = new Button("Prédire");
            private final Button btnView = new Button("Voir");
            private final Button btnEdit = new Button("Modifier");
            private final Button btnDelete = new Button("Supprimer");

            {
                // Configuration des boutons avec une largeur fixe
                btnPredict.setPrefWidth(90);

                btnView.setPrefWidth(90);
                btnEdit.setPrefWidth(90);
                btnDelete.setPrefWidth(90);

                // Configuration des gestionnaires d'événements
                btnPredict.setOnAction(e -> {
                    // Debounce: Ignore clicks within 1 second of the last click
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime < DEBOUNCE_DELAY_MS) {
                        System.out.println("Click ignored due to debounce");
                        return;
                    }
                    lastClickTime = currentTime;

                    // Prevent concurrent requests
                    if (isProcessing) {
                        Platform.runLater(() ->
                                predictionLabel.setText("Veuillez attendre la fin du traitement..."));
                        return;
                    }

                    // Get selected Demande
                    Demande demande = getTableView().getItems().get(getIndex());
                    if (demande == null || demande.getDateSoumission() == null) {
                        Platform.runLater(() ->
                                predictionLabel.setText("Erreur: Demande invalide"));
                        return;
                    }

                    // Validate fields to prevent null issues
                    String type = demande.getType() != null ? demande.getType() : "Inconnu";
                    String dateSoumission = demande.getDateSoumission().toString();
                    String statut = demande.getStatut() != null ? demande.getStatut() : "Inconnu";
                    String description = demande.getDescription() != null ? demande.getDescription() : "Aucune description";

                    // Build prompt
                    String prompt = String.format(
                            "Analysez cette demande:\n" +
                                    "Type: %s\n" +
                                    "Soumission: %s\n" +
                                    "Statut: %s\n" +
                                    "Description: %s\n" +
                                    "Prédisez le temps de traitement probable.",
                            type, dateSoumission, statut, description
                    );

                    // Update UI to show processing
                    Platform.runLater(() -> {
                        predictionLabel.setText("Traitement en cours...");
                        btnPredict.setDisable(true); // Disable button during processing
                    });

                    // Run API call in a separate thread
                    new Thread(() -> {
                        try {
                            isProcessing = true;
                            String response = sendToGemini(prompt);
                            Platform.runLater(() -> {
                                predictionLabel.setText("Prédiction: " + response);
                                btnPredict.setDisable(false); // Re-enable button
                            });
                        } catch (IOException ex) {
                            String errorMessage = ex.getMessage().contains("429") ?
                                    "Erreur: Quota API dépassé, réessayez plus tard." :
                                    "Erreur API: " + ex.getMessage();
                            Platform.runLater(() -> {
                                predictionLabel.setText(errorMessage);
                                btnPredict.setDisable(false); // Re-enable button
                            });
                            ex.printStackTrace();
                        } finally {
                            isProcessing = false;
                        }
                    }).start();
                });


                btnView.setOnAction(e -> handleViewDemande(e));
                btnEdit.setOnAction(e -> handleEditDemande(e));
                btnDelete.setOnAction(e -> handleDeleteDemande(e));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Demande demande = getTableView().getItems().get(getIndex());
                    HBox hbox = new HBox(10); // Espacement entre les boutons
                    hbox.setAlignment(javafx.geometry.Pos.CENTER);

                    // Ajouter les boutons de base
                    hbox.getChildren().addAll(btnPredict, btnView, btnEdit, btnDelete);



                    setGraphic(hbox);
                }
            }
        });
    }

    private void setupSearch() {
        // Initialiser le ComboBox des types de recherche
        searchTypeComboBox.getItems().addAll("Tous", "Type", "Statut", "Description");
        searchTypeComboBox.setValue("Tous");

        // Ajouter un listener sur le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterDemandes();
        });

        // Ajouter un listener sur le ComboBox
        searchTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterDemandes();
        });
    }

    private void filterDemandes() {
        String searchText = searchField.getText().toLowerCase();
        String searchType = searchTypeComboBox.getValue();

        filteredDemandes.clear();

        for (Demande demande : observableDemandes) {
            boolean matches = false;

            switch (searchType) {
                case "Type":
                    matches = demande.getType().toLowerCase().contains(searchText);
                    break;
                case "Statut":
                    matches = demande.getStatut().toLowerCase().contains(searchText);
                    break;
                case "Description":
                    matches = demande.getDescription().toLowerCase().contains(searchText);
                    break;
                default: // "Tous"
                    matches = demande.getType().toLowerCase().contains(searchText) ||
                            demande.getStatut().toLowerCase().contains(searchText) ||
                            demande.getDescription().toLowerCase().contains(searchText);
                    break;
            }

            if (matches) {
                filteredDemandes.add(demande);
            }
        }

        tableViewDemandes.setItems(filteredDemandes);
    }

    private void validerDemande(Demande demande) {
        demande.setStatut("Validée");
        LocalDate currentDate = LocalDate.now();
        demande.setDateValidation(currentDate);
        updateStatutAndValidationDateInDB(demande);
        tableViewDemandes.refresh();
    }


    private void updateStatutAndValidationDateInDB(Demande demande) {
        String query = "UPDATE demande SET statut = ?, date_validation = ? WHERE id = ?";
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(1, demande.getStatut());
            stmt.setDate(2, Date.valueOf(demande.getDateValidation()));
            stmt.setInt(3, demande.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDemandesFromDB() {
        observableDemandes.clear();
        String query = isAdmin ? "SELECT * FROM demande" : "SELECT * FROM demande WHERE Id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (!isAdmin) {
                stmt.setInt(1, Id);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Demande d = new Demande(
                            rs.getInt("id"),
                            rs.getDate("date_soumission").toLocalDate(),
                            rs.getString("statut"),
                            rs.getString("type"),
                            rs.getString("description"),
                            rs.getInt("Id")
                    );
                    Date dateVal = rs.getDate("date_validation");
                    if (dateVal != null) {
                        d.setDateValidation(dateVal.toLocalDate());
                    }
                    observableDemandes.add(d);
                }
            }

            filteredDemandes.setAll(observableDemandes);
            tableViewDemandes.setItems(filteredDemandes);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur de chargement", "Impossible de charger les demandes : " + e.getMessage());
        }
    }

    private void supprimerDemande(Demande demande) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette demande ?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                deleteDemandeFromDB(demande);
                observableDemandes.remove(demande);
            }
        });
    }

    private void updateStatutInDB(Demande demande) {
        String query = "UPDATE demande SET statut = ? WHERE iddemande = ?";
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(1, demande.getStatut());
            stmt.setInt(2, demande.getIddemande());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void modifierDemande(Demande demande) throws IOException {
        if (demande == null) {
            showError("Erreur", "Aucune demande sélectionnée");
            return;
        }

        FXMLLoader loader = null;
        String fxmlPath = null;

        // Détermination du type de demande et du chemin FXML correspondant
        switch (demande.getType().toLowerCase()) {
            case "congé":
                fxmlPath = "/ModifierCongéForm.fxml";
                break;
            case "attestation":
                fxmlPath = "/ModifierAttestationForm.fxml";
                break;
            default:
                showError("Type de demande non supporté",
                        "Le type de demande '" + demande.getType() + "' n'est pas supporté pour la modification.");
                return;
        }

        try {
            loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader == null) {
                throw new IOException("Impossible de charger le fichier FXML: " + fxmlPath);
            }

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Modifier " + demande.getType());

            // Configuration du contrôleur approprié avec les données de la demande
            if (demande.getType().equalsIgnoreCase("congé")) {
                if (loader.getController() instanceof ModifierCongeFormController) {
                    ModifierCongeFormController controller = loader.getController();
                    if (demande instanceof Conge) {
                        controller.setConge((Conge) demande);
                    } else {
                        // Si ce n'est pas déjà un Conge, on crée un nouveau Conge avec les données de base
                        Conge conge = new Conge(
                                demande.getIddemande(),
                                demande.getDateSoumission(),
                                demande.getStatut(),
                                demande.getType(),
                                demande.getDescription(),
                                demande.getId(),
                                demande.getDateValidation(),
                                null, // dateDebut sera à définir dans le formulaire
                                null, // dateFin sera à définir dans le formulaire
                                demande.getDescription(), // utiliser description comme motif initial
                                demande.getType() // utiliser type comme typeConge initial
                        );
                        controller.setConge(conge);
                    }
                } else {
                    throw new IllegalStateException("Contrôleur non compatible pour le type de demande: congé");
                }
            } else if (demande.getType().equalsIgnoreCase("attestation")) {
                if (loader.getController() instanceof ModifierAttestationFormController) {
                    ModifierAttestationFormController controller = loader.getController();
                    if (demande instanceof Attestation) {
                        controller.setAttestation((Attestation) demande);
                    } else {
                        // Si ce n'est pas déjà une Attestation, on crée une nouvelle Attestation avec les données de base
                        // On garde le type général comme "attestation" et on utilise la description pour le type spécifique
                        Attestation attestation = new Attestation(
                                demande.getDateSoumission(),
                                demande.getStatut(),
                                "attestation", // type général toujours "attestation"
                                demande.getDescription(),
                                demande.getId(),
                                null, // dateDebut
                                null, // dateFin
                                demande.getDescription(), // motif
                                demande.getDescription(), // typeAttestation spécifique (sera modifié dans le formulaire)
                                demande.getDateValidation()
                        );
                        controller.setAttestation(attestation);
                    }
                } else {
                    throw new IllegalStateException("Contrôleur non compatible pour le type de demande: attestation");
                }
            }

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showError("Erreur de chargement",
                    "Impossible de charger l'interface de modification: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalStateException e) {
            showError("Erreur de configuration", e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    private void deleteDemandeFromDB(Demande demande) {
        String query = "DELETE FROM demande WHERE id = ?";
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setInt(1, demande.getIddemande());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        filterDemandes();
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        searchTypeComboBox.setValue("Tous");
        filteredDemandes.setAll(observableDemandes);
        tableViewDemandes.setItems(filteredDemandes);
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Demande.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors du retour à l'interface principale");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    private void refuserDemande(Demande demande) {
        demande.setStatut("Refusée");
        LocalDate currentDate = LocalDate.now();
        demande.setDateValidation(currentDate);
        updateStatutAndValidationDateInDB(demande);
        tableViewDemandes.refresh();
    }


    @FXML
    private void handleValidateDemande(ActionEvent event) {
        Demande selectedDemande = tableViewDemandes.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            if (!selectedDemande.getStatut().equals("Validée") && !selectedDemande.getStatut().equals("Refusée")) {
                validerDemande(selectedDemande);
                showSuccessAlert("La demande a été validée avec succès.");
            } else {
                showErrorAlert("Cette demande a déjà été traitée.");
            }
        } else {
            showErrorAlert("Veuillez sélectionner une demande à valider.");
        }
    }

    @FXML
    private void handleRejectDemande(ActionEvent event) {
        Demande selectedDemande = tableViewDemandes.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            if (!selectedDemande.getStatut().equals("Validée") && !selectedDemande.getStatut().equals("Refusée")) {
                refuserDemande(selectedDemande);
                showSuccessAlert("La demande a été refusée.");
            } else {
                showErrorAlert("Cette demande a déjà été traitée.");
            }
        } else {
            showErrorAlert("Veuillez sélectionner une demande à refuser.");
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleViewDemande(ActionEvent event) {
        Demande selectedDemande = tableViewDemandes.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            // Afficher les détails de la demande
            String details = String.format(
                    "Détails de la demande:\n" +
                            "Type: %s\n" +
                            "Description: %s\n" +
                            "Date de soumission: %s\n" +
                            "Statut: %s\n" +
                            "Date de validation: %s",



                    selectedDemande.getType(),
                    selectedDemande.getDescription(),
                    selectedDemande.getDateSoumission(),
                    selectedDemande.getStatut(),
                    selectedDemande.getDateValidation() != null ? selectedDemande.getDateValidation() : "Non validée"
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Détails de la demande");
            alert.setHeaderText(null);
            alert.setContentText(details);
            alert.showAndWait();
        } else {
            showErrorAlert("Veuillez sélectionner une demande pour voir ses détails.");
        }
    }

    @FXML
    private void handleEditDemande(ActionEvent event) {
        Demande selectedDemande = tableViewDemandes.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            try {
                modifierDemande(selectedDemande);
            } catch (IOException e) {
                showErrorAlert("Erreur lors de la modification : " + e.getMessage());
            }
        } else {
            showErrorAlert("Veuillez sélectionner une demande à modifier.");
        }
    }

    @FXML
    private void handleDeleteDemande(ActionEvent event) {
        Demande selectedDemande = tableViewDemandes.getSelectionModel().getSelectedItem();
        if (selectedDemande != null) {
            supprimerDemande(selectedDemande);
        } else {
            showErrorAlert("Veuillez sélectionner une demande à supprimer.");
        }
    }

    private String sendToGemini(String prompt) throws IOException {
        String apiKey = EnvConfig.getGeminiApiKey();
        System.out.println("API Key: " + apiKey);
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IOException("Clé API Gemini non configurée");
        }

        String urlStr = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-pro:generateContent?key=" +
                URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

        int maxRetries = 3;
        int retryCount = 0;
        long retryDelaySeconds = 4;

        while (retryCount <= maxRetries) {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(10000);

                String inputJson = String.format("{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}",
                        prompt.replace("\"", "\\\"").replace("\n", "\\n"));

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = inputJson.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    String errorMessage = connection.getResponseMessage();
                    StringBuilder errorResponse = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                        String errorLine;
                        while ((errorLine = br.readLine()) != null) {
                            errorResponse.append(errorLine.trim());
                        }
                        System.out.println("Error Response: " + errorResponse);
                    }

                    if (responseCode == 429 && retryCount < maxRetries) {
                        try {
                            JSONObject errorJson = new JSONObject(errorResponse.toString());
                            if (errorJson.has("error") && errorJson.getJSONObject("error").has("details")) {
                                JSONArray details = errorJson.getJSONObject("error").getJSONArray("details");
                                for (int i = 0; i < details.length(); i++) {
                                    JSONObject detail = details.getJSONObject(i);
                                    if (detail.has("retryDelay")) {
                                        String retryDelayStr = detail.getString("retryDelay").replace("s", "");
                                        retryDelaySeconds = Long.parseLong(retryDelayStr);
                                        break;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            System.out.println("Failed to parse retryDelay: " + e.getMessage());
                        }

                        System.out.println("HTTP 429 - Retrying after " + retryDelaySeconds + " seconds...");
                        try {
                            TimeUnit.SECONDS.sleep(retryDelaySeconds);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            throw new IOException("Interrupted during retry", e);
                        }
                        retryCount++;
                        continue;
                    }
                    throw new IOException("Erreur HTTP: " + responseCode + " - " + errorMessage);
                }

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    System.out.println("Raw API Response: " + response);
                    JSONObject json = new JSONObject(response.toString());
                    if (!json.has("candidates")) {
                        throw new IOException("Réponse API invalide: structure 'candidates' manquante");
                    }

                    JSONArray candidates = json.getJSONArray("candidates");
                    if (candidates.length() == 0) {
                        throw new IOException("Réponse API vide: aucun candidat trouvé");
                    }

                    JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
                    return content.getJSONArray("parts").getJSONObject(0).getString("text");
                }
            } catch (MalformedURLException e) {
                throw new IOException("URL invalide: " + e.getMessage());
            } catch (JSONException e) {
                throw new IOException("Erreur d'analyse JSON: " + e.getMessage());
            }
        }
        throw new IOException("Échec après " + maxRetries + " tentatives en raison d'erreurs HTTP 429");
    }
}
