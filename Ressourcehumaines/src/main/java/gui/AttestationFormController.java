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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AttestationFormController {

    @FXML private ComboBox<String> comboTypeAttestation;
    @FXML private TextArea textDescriptif;
    @FXML private TextField textObjet;
    @FXML private TextArea textMotif;
    @FXML private Hyperlink hyperlinkPieceJointe;
    @FXML private Button btnSigner;
    @FXML private Button btnTelecharger;
    @FXML private Button btnAjouter;
    @FXML private TextArea attestationPreview;

    private File pdfGenere = null;
    private File fichierJoint = null;

    @FXML
    public void initialize() {
        comboTypeAttestation.getItems().addAll(
                "Attestation de présence",
                "Attestation de réussite",
                "Attestation de stage"
        );
        btnSigner.setDisable(false);
        btnTelecharger.setDisable(true);
        comboTypeAttestation.setOnAction(event -> updateDescriptifText());
        textObjet.textProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        textMotif.textProperty().addListener((obs, oldVal, newVal) -> updatePreview());
    }

    private void updateDescriptifText() {
        String type = comboTypeAttestation.getValue();
        if (type != null) {
            switch (type) {
                case "Attestation de présence":
                    textDescriptif.setText("Attestation de présence :Nous soussignés, [Nom de l’entreprise ou de l’établissement], attestons par la présente que Mademoiselle/Monsieur [Nom et Prénom] a été présent(e) au sein de notre structure en qualité de [stagiaire / collaborateur / étudiant(e), etc.] pendant la période allant du [date de début] au [date de fin].\n" +
                            "\n" +
                            "Cette attestation est délivrée à la demande de l’intéressé(e), pour servir et valoir ce que de droit.\n" +
                            "\n" +
                            "Fait à [lieu], le [date].");
                    break;
                case "Attestation de réussite":
                    textDescriptif.setText("Attestation de réussite : Nous soussignés, [Nom de l’établissement ou de l’entreprise], attestons que Mademoiselle/Monsieur [Nom et Prénom], a accompli avec succès [le stage / la formation / le projet] réalisé au sein de notre structure, du [date de début] au [date de fin].\n" +
                            "\n" +
                            "Au cours de cette période, Mademoiselle/Monsieur [Nom] a démontré un engagement sérieux, une bonne capacité d’apprentissage ainsi qu’un esprit d’initiative. Les objectifs fixés au départ ont été atteints avec efficacité et professionnalisme.\n" +
                            "\n" +
                            "En témoignage de sa réussite, la présente attestation lui est délivrée pour servir et valoir ce que de droit.\n" +
                            "\n" +
                            "Fait à [lieu], le [date].");
                    break;
                case "Attestation de stage":
                    textDescriptif.setText("Attestation de stage :Dans le cadre de sa formation, Mademoiselle/Monsieur [Nom et Prénom] a effectué un stage au sein de notre entreprise du [date de début] au [date de fin]. Ce stage avait pour objectif de lui permettre de découvrir le fonctionnement d’une entreprise, d’acquérir une expérience pratique et de mettre en application les connaissances théoriques acquises au cours de sa formation.\n" +
                            "\n" +
                            "Au cours de cette période, le/la stagiaire a été intégré(e) dans le service [nom du service ou département], où il/elle a participé à diverses missions liées à [secteur d’activité ou domaine général : logistique, production, qualité, administration, etc.]. Il/elle a démontré un bon sens de l’initiative, de la rigueur, et une capacité d’adaptation appréciée par l’équipe encadrante.\n" +
                            "\n" +
                            "Son implication, son sérieux et sa motivation ont contribué au bon déroulement de son stage.\n" +
                            "\n");
                    break;
                default:
                    textDescriptif.clear();
            }
        }
        updatePreview();
    }

    private void updatePreview() {
        String type = comboTypeAttestation.getValue();
        String objet = textObjet.getText().trim();
        String motif = textMotif != null ? textMotif.getText().trim() : "";
        attestationPreview.setText(genererTexteAttestation(type, objet, motif));
    }

    @FXML
    void ajoutpiece(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une pièce jointe (optionnelle)");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fichierJoint = selectedFile;
            hyperlinkPieceJointe.setText(selectedFile.getName());
        }
    }

    @FXML
    void signerDocument(ActionEvent event) {
        String type = comboTypeAttestation.getValue();
        String objet = textObjet.getText().trim();
        String motif = textMotif != null ? textMotif.getText().trim() : "";

        if (type == null || objet.isEmpty()) {
            showAlert("Veuillez remplir le type d'attestation et l'objet.");
            return;
        }

        String texte = genererTexteAttestation(type, objet, motif);
        attestationPreview.setText(texte);

        // Générer le PDF dans un fichier temporaire
        try {
            File tempFile = File.createTempFile("attestation_", ".pdf");
            genererAttestationPDF(texte, tempFile);
            pdfGenere = tempFile;
            btnTelecharger.setDisable(false);
            showAlert("PDF généré et signé avec succès !");
        } catch (Exception e) {
            showAlert("Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }

    @FXML
    void telechargerPDF(ActionEvent event) {
        if (pdfGenere != null && pdfGenere.exists()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer l'attestation");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    java.nio.file.Files.copy(pdfGenere.toPath(), file.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    showAlert("PDF téléchargé avec succès !");
                    // Ouvre le PDF téléchargé
                    java.awt.Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    showAlert("Erreur lors du téléchargement ou de l'ouverture du PDF : " + e.getMessage());
                }
            }
        } else {
            showAlert("Aucun PDF généré à télécharger.");
        }
    }

    @FXML
    void ajoute(ActionEvent event) {
        String typeAttestation = comboTypeAttestation.getValue();
        String objet = textObjet.getText().trim();
        String motif = textMotif != null ? textMotif.getText().trim() : "";
        LocalDate dateSoumission = LocalDate.now();
        String statut = "en attente";

        if (typeAttestation == null || objet.isEmpty()) {
            showAlert("Veuillez remplir le type d'attestation et l'objet.");
            return;
        }

        // Connexion à la base de données
        String url = "jdbc:mysql://localhost:3306/workbridge";
        String username = "root";
        String password = "";

        String query = "INSERT INTO demande (type, type_attestation, objet, description, motif, date_soumission, statut, fichier, utilisateur_id) " +
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
            stmt.setString(8, fichierJoint != null ? fichierJoint.getAbsolutePath() : null);
            stmt.setInt(9, 1); // Remplace par l'ID utilisateur réel si besoin

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
    private String genererTexteAttestation(String type, String objet, String motif) {
        if (type == null || type.isEmpty()) return "Type d'attestation non spécifié.";

        switch (type) {
            case "Attestation de présence":
                return "Nous soussignés, certifions que l'intéressé(e) a été présent(e) au sein de notre établissement durant la période indiquée.\n"
                        + "Objet : " + objet + ".\n"
                        + "Motif : " + motif + ".\n\n"
                        + "Cette attestation est délivrée à la demande de l’intéressé(e), pour servir et valoir ce que de droit.";

            case "Attestation de réussite":
                return "Nous soussignés, certifions que l'intéressé(e) a accompli avec succès les objectifs fixés et a validé son année ou cycle de formation.\n"
                        + "Objet : " + objet + ".\n"
                        + "Motif : " + motif + ".\n\n"
                        + "La présente attestation est délivrée pour servir et valoir ce que de droit.";

            case "Attestation de stage":
                return "Nous soussignés, certifions que l'intéressé(e) a effectué un stage au sein de notre établissement pendant la période convenue.\n"
                        + "Objet : " + objet + ".\n"
                        + "Motif : " + motif + ".\n\n"
                        + "Cette attestation lui est délivrée en reconnaissance de son engagement et de son travail, pour servir et valoir ce que de droit.";

            default:
                return "Type d'attestation non reconnu.";
        }
    }
    @FXML
    private void retourPagePrecedente(ActionEvent event) {
        try {
            // Change "Dashboard.fxml" par le nom exact de ta page précédente
            Parent root = FXMLLoader.load(getClass().getResource("/Demande.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void genererAttestationPDF(String texte, File outputFile) throws IOException {
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                // Titre "ATTESTATION"
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.newLineAtOffset(220, 700); // Centré environ
                content.showText("ATTESTATION");
                content.endText();

                // Corps du texte
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 12);
                content.newLineAtOffset(80, 650); // Marge à gauche
                for (String line : texte.split("\\n")) {
                    content.showText(line);
                    content.newLineAtOffset(0, -18); // Saut de ligne
                }
                content.endText();

                // Signature
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_OBLIQUE, 12);
                content.newLineAtOffset(80, 120);
                String dateSignature = java.time.LocalDate.now().toString();
                content.showText("Signé par le Directeur le " + dateSignature);
                content.endText();
            }

            doc.save(outputFile);
        }
    }
    private void clearForm() {
        comboTypeAttestation.setValue(null);
        textObjet.clear();
        textMotif.clear();
        textDescriptif.clear();
        attestationPreview.clear();
        fichierJoint = null;
        hyperlinkPieceJointe.setText("Aucun fichier sélectionné");
        btnTelecharger.setDisable(true);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}