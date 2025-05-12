package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.PDFSignatureUtil;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.Desktop;

public class AttestationFormController {

    @FXML
    private ComboBox<String> comboTypeAttestation;

    @FXML
    private TextField textObjet;

    @FXML
    private TextArea textMotif;

    @FXML
    private TextArea textDescriptif;

    @FXML
    private Hyperlink hyperlinkPieceJointe;

    @FXML
    private Button btnTelecharger;

    @FXML
    private Button btnSigner;

    private File fichierJoint;
    private File fichierSigne;
    private File attestationSignee;

    @FXML
    public void initialize() {
        // Remplir le comboBox avec des types d'attestation courants
        comboTypeAttestation.getItems().addAll(
                "Attestation de travail",
                "Attestation de salaire",
                "Attestation de stage"
        );
        
        // Désactiver les boutons initialement
        btnSigner.setDisable(true);
        btnTelecharger.setDisable(true);

        // Ajouter un listener pour mettre à jour le texte descriptif
        comboTypeAttestation.setOnAction(event -> updateDescriptifText());
    }

    private void updateDescriptifText() {
        String type = comboTypeAttestation.getValue();
        if (type != null) {
            switch (type) {
                case "Attestation de travail":
                    textDescriptif.setText("Cette attestation confirme votre statut d'employé dans l'entreprise. " +
                            "Elle inclut généralement votre date d'embauche, votre poste actuel et votre statut (CDI, CDD, etc.). " +
                            "Cette attestation est souvent demandée pour des démarches administratives ou pour louer un logement.");
                    break;
                case "Attestation de salaire":
                    textDescriptif.setText("Cette attestation détaille vos informations salariales. " +
                            "Elle inclut votre salaire brut, net, les primes et avantages. " +
                            "Cette attestation est généralement requise pour des prêts bancaires ou des locations immobilières.");
                    break;
                case "Attestation de stage":
                    textDescriptif.setText("Cette attestation confirme votre période de stage dans l'entreprise. " +
                            "Elle inclut la durée du stage, le service d'accueil, et les missions réalisées. " +
                            "Cette attestation est importante pour valider votre formation et enrichir votre CV.");
                    break;
                default:
                    textDescriptif.clear();
            }
        }
    }

    @FXML
    void ajoutpiece(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une pièce jointe");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fichierJoint = selectedFile;
            hyperlinkPieceJointe.setText(selectedFile.getName());
            btnSigner.setDisable(false);
        }
    }

    @FXML
    void signerDocument(ActionEvent event) {
        if (fichierJoint == null) {
            showAlert("Veuillez d'abord sélectionner un document à signer.");
            return;
        }

        try {
            // Créer le nom du fichier signé
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String outputPath = fichierJoint.getParent() + File.separator + 
                              "attestation_signed_" + timestamp + ".pdf";

            // Obtenir le texte descriptif selon le type d'attestation
            String descriptifText = getDescriptifText(comboTypeAttestation.getValue());

            // Créer le texte de signature avec le descriptif
            String signatureText = String.format(
                "ATTESTATION\n\n" +
                "Type: %s\n\n" +
                "Description:\n%s\n\n" +
                "Objet: %s\n\n" +
                "Signé électroniquement le %s",
                comboTypeAttestation.getValue(),
                descriptifText,
                textObjet.getText(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );

            // Signer le document
            PDFSignatureUtil.createDigitalSignature(fichierJoint.getAbsolutePath(), outputPath, signatureText);
            
            // Mettre à jour le fichier attestation signée
            attestationSignee = new File(outputPath);
            hyperlinkPieceJointe.setText(attestationSignee.getName());
            
            // Activer le bouton de téléchargement
            btnTelecharger.setDisable(false);
            
            // Ouvrir automatiquement le PDF signé
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(attestationSignee);
            }
            
            showAlert("Attestation signée avec succès !");
        } catch (Exception e) {
            showAlert("Erreur lors de la signature de l'attestation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getDescriptifText(String typeAttestation) {
        if (typeAttestation == null) return "";
        
        switch (typeAttestation) {
            case "Attestation de travail":
                return "Cette attestation confirme votre statut d'employé dans l'entreprise. " +
                       "Elle inclut votre date d'embauche, votre poste actuel et votre statut (CDI, CDD, etc.). " +
                       "Cette attestation est souvent demandée pour des démarches administratives ou pour louer un logement.";
            
            case "Attestation de salaire":
                return "Cette attestation détaille vos informations salariales. " +
                       "Elle inclut votre salaire brut, net, les primes et avantages. " +
                       "Cette attestation est généralement requise pour des prêts bancaires ou des locations immobilières.";
            
            case "Attestation de stage":
                return "Cette attestation confirme votre période de stage dans l'entreprise. " +
                       "Elle inclut la durée du stage, le service d'accueil, et les missions réalisées. " +
                       "Cette attestation est importante pour valider votre formation et enrichir votre CV.";
            
            default:
                return "";
        }
    }

    @FXML
    void telechargerPDF(ActionEvent event) {
        if (attestationSignee != null && attestationSignee.exists()) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(attestationSignee);
                }
            } catch (IOException e) {
                showAlert("Erreur lors de l'ouverture du PDF : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Aucun fichier signé disponible.");
        }
    }

    @FXML
    private void retourPagePrecedente(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Demande.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ajoute(ActionEvent event) {
        String typeAttestation = comboTypeAttestation.getValue();
        String objet = textObjet.getText().trim();
        String motif = textMotif.getText().trim();
        LocalDate dateSoumission = LocalDate.now();
        String statut = "en attente";

        // Vérification des champs obligatoires
        if (typeAttestation == null) {
            showAlert("Le type d'attestation est obligatoire.");
            return;
        }

        if (objet.isEmpty()) {
            showAlert("L'objet est obligatoire.");
            return;
        }

        if (motif.isEmpty()) {
            showAlert("Le motif est obligatoire.");
            return;
        }

        if (objet.length() < 3) {
            showAlert("L'objet doit comporter au moins 3 caractères.");
            return;
        }

        if (fichierJoint == null) {
            showAlert("Veuillez ajouter une pièce jointe.");
            return;
        }

        // Vérifier si le document est signé
        if (!PDFSignatureUtil.verifySignature(fichierJoint.getAbsolutePath())) {
            showAlert("Le document doit être signé électroniquement avant d'être soumis.");
            return;
        }

        // Connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/workbridge";
        String username = "root";
        String password = "";

        String query = "INSERT INTO demande (type, type_attestation, objet, description, motif, " +
                      "date_soumission, statut, fichier, utilisateur_id) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "attestation");
            stmt.setString(2, typeAttestation);
            stmt.setString(3, objet);
            stmt.setString(4, objet);
            stmt.setString(5, motif);
            stmt.setDate(6, Date.valueOf(dateSoumission));
            stmt.setString(7, statut);
            stmt.setString(8, fichierJoint.getAbsolutePath());
            stmt.setInt(9, 1);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Demande d'attestation envoyée avec succès !");
                clearForm();
            } else {
                showAlert("Une erreur est survenue lors de l'ajout de la demande.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur de base de données : " + e.getMessage());
        }
    }

    // Afficher la liste des demandes (fonctionnalité supplémentaire)
    @FXML
    void afficherDemandes(ActionEvent event) {
        // Tu peux ici rediriger vers une autre scène ou charger une liste de demandes
        System.out.println("Afficher la liste des demandes");
    }

    // Affichage d'une alerte
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Réinitialiser le formulaire
    private void clearForm() {
        comboTypeAttestation.setValue(null);
        textObjet.clear();
        textMotif.clear();
        textDescriptif.clear();
        fichierJoint = null;
        attestationSignee = null;
        hyperlinkPieceJointe.setText("Aucun fichier sélectionné");
        btnSigner.setDisable(true);
        btnTelecharger.setDisable(true);
    }
}
