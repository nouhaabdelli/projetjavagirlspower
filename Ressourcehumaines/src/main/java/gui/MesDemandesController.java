package gui;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import gui.ModifierAttestationFormController;
import gui.ModifierCongeFormController;

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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MesDemandesController {

    @FXML private TableView<Demande> tableViewDemandes;
    @FXML private TableColumn<Demande, Integer> colId;
    @FXML private TableColumn<Demande, String> colType;
    @FXML private TableColumn<Demande, String> colStatut;
    @FXML private TableColumn<Demande, LocalDate> colDateSoumission;
    @FXML private TableColumn<Demande, String> colDescription;
    @FXML private TableColumn<Demande, LocalDate> colDateValidation;
    @FXML private TableColumn<Demande, Void> colActions;
    @FXML private Label predictionLabel;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchTypeComboBox;

    private int utilisateurId;
    private boolean isAdmin;
    private ObservableList<Demande> observableDemandes = FXCollections.observableArrayList();
    private ObservableList<Demande> filteredDemandes = FXCollections.observableArrayList();

    private static final String OPENAI_API_KEY = "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";
    private static final int MAX_RETRIES = 3;
    private static final int INITIAL_RETRY_DELAY = 5000; // 5 secondes
    private static final int MAX_RETRY_DELAY = 30000; // 30 secondes maximum

    public MesDemandesController() {}

    public void initialize() {
        this.setUtilisateurId(1);  // ID utilisateur par défaut
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

        colActions.setCellFactory(param -> new TableCell<Demande, Void>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final Button btnValider = new Button("Valider");
            private final Button btnRefuser = new Button("Refuser");
            private final Button btnPredire = new Button("Prédire");

            {
                btnModifier.setOnAction(e -> {
                    try {
                        modifierDemande(getCurrentDemande());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.out.println("Erreur lors de la modification de la demande : " + ex.getMessage());
                    }
                });
                btnSupprimer.setOnAction(e -> supprimerDemande(getCurrentDemande()));
                btnValider.setOnAction(e -> validerDemande(getCurrentDemande()));
                btnRefuser.setOnAction(e -> refuserDemande(getCurrentDemande()));
                btnPredire.setOnAction(e -> predireTempsReponse(getCurrentDemande()));

                // Style des boutons
                btnRefuser.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;");
            }

            private Demande getCurrentDemande() {
                return getTableView().getItems().get(getIndex());
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Demande demande = getCurrentDemande();
                    
                    // Créer une liste de boutons de base (pour tous les utilisateurs)
                    List<Button> baseButtons = new ArrayList<>();
                    baseButtons.add(btnModifier);
                    baseButtons.add(btnSupprimer);
                    baseButtons.add(btnPredire);

                    // Si c'est un admin, ajouter les boutons de validation et refus
                    if (isAdmin) {
                        baseButtons.add(btnValider);
                        baseButtons.add(btnRefuser);
                        
                        // Désactiver les boutons si la demande est déjà traitée
                        boolean isProcessed = demande.getStatut().equals("Validée") || demande.getStatut().equals("Refusée");
                        btnValider.setDisable(isProcessed);
                        btnRefuser.setDisable(isProcessed);
                    }

                    // Créer le HBox avec les boutons appropriés
                    HBox hbox = new HBox(5);
                    hbox.getChildren().addAll(baseButtons);
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

    private void predireTempsReponse(Demande demande) {
        try {
            predictionLabel.setText("Calcul de la prédiction en cours...");
            String prompt = String.format(
                "Estimez le temps de réponse pour une demande de type %s avec le motif suivant : %s",
                demande.getType(),
                demande.getDescription()
            );
            
            String prediction = sendRequestToOpenAI(prompt);
            if (prediction != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(prediction);
                    if (jsonResponse.has("choices") && jsonResponse.getJSONArray("choices").length() > 0) {
                        String predictionText = jsonResponse.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content").trim();
                        predictionLabel.setText("Prédiction : " + predictionText);
                    } else {
                        predictionLabel.setText("Aucune prédiction disponible");
                    }
                } catch (Exception e) {
                    predictionLabel.setText("Erreur lors du traitement de la réponse : " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                predictionLabel.setText("Impossible d'obtenir une prédiction");
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("Limite de requêtes API dépassée")) {
                predictionLabel.setText("L'API est temporairement surchargée. Veuillez réessayer dans quelques minutes.");
        } else {
                predictionLabel.setText("Erreur lors de la prédiction : " + errorMessage);
            }
            e.printStackTrace();
        }
    }

    private String sendRequestToOpenAI(String prompt) {
        HttpURLConnection conn = null;
        int retryCount = 0;
        int retryDelay = INITIAL_RETRY_DELAY;

        while (retryCount < MAX_RETRIES) {
            try {
                String apiKey = OPENAI_API_KEY;
                String apiUrl = "https://api.openai.com/v1/chat/completions";

                URL url = new URL(apiUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + apiKey);
                conn.setConnectTimeout(10000); // 10 secondes
                conn.setReadTimeout(10000);    // 10 secondes
                conn.setDoOutput(true);

                String jsonInputString = String.format(
                    "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"max_tokens\": 100}",
                    prompt.replace("\"", "\\\"")
                );

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

                int responseCode = conn.getResponseCode();
                if (responseCode == 401) {
                    throw new IOException("Erreur d'authentification API OpenAI. Vérifiez votre clé API.");
                } else if (responseCode == 429) {
                    // Récupérer le délai d'attente suggéré par l'API si disponible
                    String retryAfter = conn.getHeaderField("Retry-After");
                    if (retryAfter != null) {
                        retryDelay = Math.min(Integer.parseInt(retryAfter) * 1000, MAX_RETRY_DELAY);
                    } else {
                        retryDelay = Math.min(retryDelay * 2, MAX_RETRY_DELAY);
                    }
                    
                    if (retryCount < MAX_RETRIES - 1) {
                        String message = String.format("Limite d'API atteinte. Nouvelle tentative dans %d secondes...", retryDelay/1000);
                        System.out.println(message);
                        Platform.runLater(() -> predictionLabel.setText(message));
                        Thread.sleep(retryDelay);
                        retryCount++;
                        continue;
                    } else {
                        throw new IOException("Limite de requêtes API dépassée. Veuillez réessayer dans quelques minutes.");
                    }
                } else if (responseCode == 503) {
                    throw new IOException("Service API temporairement indisponible. Veuillez réessayer plus tard.");
                } else if (responseCode != 200) {
                    throw new IOException("Erreur API OpenAI. Code de réponse : " + responseCode);
                }

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    return response.toString();
                }
            } catch (SocketException e) {
                if (retryCount < MAX_RETRIES - 1) {
                    String message = String.format("Erreur de connexion. Nouvelle tentative dans %d secondes...", retryDelay/1000);
                    System.out.println(message);
                    Platform.runLater(() -> predictionLabel.setText(message));
                    try {
                        Thread.sleep(retryDelay);
                        retryDelay = Math.min(retryDelay * 2, MAX_RETRY_DELAY);
                        retryCount++;
                        continue;
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Tentative de reconnexion interrompue", ie);
                    }
                }
                throw new RuntimeException("Erreur de connexion réseau. Vérifiez votre connexion internet.", e);
            } catch (IOException e) {
                if (retryCount < MAX_RETRIES - 1) {
                    String message = String.format("Erreur de communication. Nouvelle tentative dans %d secondes...", retryDelay/1000);
                    System.out.println(message);
                    Platform.runLater(() -> predictionLabel.setText(message));
                    try {
                        Thread.sleep(retryDelay);
                        retryDelay = Math.min(retryDelay * 2, MAX_RETRY_DELAY);
                        retryCount++;
                        continue;
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Tentative de reconnexion interrompue", ie);
                    }
                }
                throw new RuntimeException("Erreur de communication avec l'API OpenAI : " + e.getMessage(), e);
            } catch (Exception e) {
                throw new RuntimeException("Erreur inattendue : " + e.getMessage(), e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        throw new RuntimeException("Nombre maximum de tentatives atteint");
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
        String query = isAdmin ? "SELECT * FROM demande" : "SELECT * FROM demande WHERE utilisateur_id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (!isAdmin) {
                stmt.setInt(1, utilisateurId);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Demande d = new Demande(
                            rs.getInt("id"),
                            rs.getDate("date_soumission").toLocalDate(),
                            rs.getString("statut"),
                            rs.getString("type"),
                            rs.getString("description"),
                            rs.getInt("utilisateur_id")
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
        String query = "UPDATE demande SET statut = ? WHERE id = ?";
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(1, demande.getStatut());
            stmt.setInt(2, demande.getId());
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
                            demande.getId(),
                            demande.getDateSoumission(),
                            demande.getStatut(),
                            demande.getType(),
                            demande.getDescription(),
                            demande.getUtilisateurId(),
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
                            demande.getUtilisateurId(),
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
            stmt.setInt(1, demande.getId());
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

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
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

}