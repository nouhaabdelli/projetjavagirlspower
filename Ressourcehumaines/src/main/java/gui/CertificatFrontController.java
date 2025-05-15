/*package gui;

import entities.Certificat;
import entities.Formation;
import services.Certificatservices;
import services.FormationService;
import main.MyListener2;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.stage.Stage;
import utils.MyConnection;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CertificatFrontController implements Initializable {

    @FXML private VBox commentsContainer;
    @FXML private GridPane commentsGrid;
    @FXML private Label BilletIdLabel;
    @FXML private VBox chosenCard;
    @FXML private TextArea commentField;
    @FXML private TextArea idtf;
    @FXML private TextArea idtf1;
    @FXML private GridPane grid;
    @FXML private ScrollPane scroll;
    @FXML
    private AnchorPane anch;
    private final List<Formation> formations = new ArrayList<>();
    private final List<Certificat> certificats = new ArrayList<>();
    private Connection connection;

    private final int currentUserId = 1;

    private void loadUI(String fxml) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/" + fxml));
            anch.getChildren().setAll(pane);
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = MyConnection.getInstance().getConnection();

        try {
            formations.addAll(loadFormations());
        } catch (SQLException e) {
            showAlert("Error", "Failed to load formations.");
            return;
        }

        if (!formations.isEmpty()) {
            displayFormation(formations.get(0)); // Display first by default
        }

        populateFormationGrid();
    }

    private List<Formation> loadFormations() throws SQLException {
        return new FormationService().afficherFormations();
    }
    Certificatservices cs = new Certificatservices();
    private List<Certificat> loadCertificats(int formationId) throws SQLException {
        System.out.println(formationId);
        System.out.println(cs.afficherCertificats());

        return  cs.afficherCertificats();
    }
    @FXML private Button generatePdfButton;
    @FXML private Label pdfStatusLabel;








    @FXML
    void generatePdf(ActionEvent event) {
        if (selectedCertificat == null) {
            showAlert("Error", "No certificate selected.");
            return;
        }

        Document document = new Document(PageSize.A4.rotate(), 50, 50, 70, 50); // Landscape
        try {
            String outputFilePath = "certificat_" + selectedCertificat.getCodeCertificat() + ".pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputFilePath));
            document.open();

            PdfContentByte canvas = writer.getDirectContent();

            // Background watermark
            try {
                Image bg = Image.getInstance("/css/certif_bg.jpg");
                bg.setAbsolutePosition(0, 0);
                bg.scaleAbsolute(PageSize.A4.rotate());
                bg.setTransparency(new int[]{0x00, 0x10}); // Light fade
                canvas.addImage(bg);
            } catch (Exception ex) {
                System.out.println("Background not found or failed to load");
            }

            // Border
            Rectangle border = new Rectangle(36, 36, 806, 559);
            border.setBorder(Rectangle.BOX);
            border.setBorderWidth(4);
            border.setBorderColor(BaseColor.DARK_GRAY);
            canvas.rectangle(border);

            // Fonts
            Font goldTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 38, Font.BOLD, new BaseColor(218, 165, 32));
            Font italicSubtitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.ITALIC);
            Font nameFont = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLDITALIC, BaseColor.BLACK);
            Font bodyFont = new Font(Font.FontFamily.TIMES_ROMAN, 18);
            Font formationFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD, BaseColor.BLUE);
            Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.ITALIC);

            // Logo
            try {
                Image logo = Image.getInstance("/css/logo.png");
                logo.setAlignment(Image.ALIGN_CENTER);
                logo.scaleToFit(100, 100);
                document.add(logo);
            } catch (Exception ex) {
                System.out.println("Logo not found or failed to load");
            }

            // Title
            Paragraph title = new Paragraph("Certificate of Accomplishment", goldTitleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(30);
            document.add(title);

            // Subtitle
            Paragraph subtitle = new Paragraph("This certificate is proudly presented to", italicSubtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(10);
            document.add(subtitle);

            // Name
            Paragraph name = new Paragraph(selectedCertificat.getUsername(), nameFont);
            name.setAlignment(Element.ALIGN_CENTER);
            name.setSpacingAfter(25);
            document.add(name);

            // Description
            Paragraph desc = new Paragraph("for successfully completing the training program:", bodyFont);
            desc.setAlignment(Element.ALIGN_CENTER);
            desc.setSpacingAfter(10);
            document.add(desc);

            // Formation
            Paragraph formation = new Paragraph(String.valueOf(selectedCertificat.getFormationid()), formationFont);
            formation.setAlignment(Element.ALIGN_CENTER);
            formation.setSpacingAfter(15);
            document.add(formation);

            // Niveau
            Paragraph niveau = new Paragraph("Level Achieved: " + selectedCertificat.getNiveau(), bodyFont);
            niveau.setAlignment(Element.ALIGN_CENTER);
            niveau.setSpacingAfter(20);
            document.add(niveau);

            // Date
            Paragraph date = new Paragraph("Issued on: " + java.time.LocalDate.now(), bodyFont);
            date.setAlignment(Element.ALIGN_CENTER);
            date.setSpacingAfter(50);
            document.add(date);

            // Signature
            Paragraph signature = new Paragraph("___________________________\nDirector of Training", footerFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();
            writer.close();

            showAlert("Success", "Certificate PDF generated successfully: " + outputFilePath);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate certificate PDF.");
        }
    }

    private void populateFormationGrid() {
        int column = 0;
        int row = 1;

        try {
            for (Formation formation : formations) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FormationItem.fxml"));
                AnchorPane pane = loader.load();

                FormationItemController controller = loader.getController();

                controller.setFormation(formation, selectedFormation -> {
                    displayFormation(selectedFormation);
                });

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(10));

                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayFormation(Formation formation) {
        try {


            idtf.setText(String.valueOf(formation.getIdFormation()));
            certificats.clear();
            commentsGrid.getChildren().clear();

            certificats.addAll(loadCertificats(formation.getIdFormation()));
            System.out.println(certificats);
            refreshCertificatsGrid();

        } catch (SQLException e) {
            showAlert("Error", "Failed to load certificates.");
        }
    }

    private void refreshCertificatsGrid() {
        if (!certificats.isEmpty()) {
            setChosenCommentaire(certificats.get(0));

            MyListener2 certificatListener = this::setChosenCommentaire;

            commentsGrid.getChildren().clear();
            int row = 0;

            try {
                for (Certificat cert : certificats) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Certifs.fxml"));
                    AnchorPane pane = loader.load();

                    CertificatItemController controller = loader.getController();
                    controller.setData(cert, certificatListener);

                    commentsGrid.add(pane, 0, row++);
                    GridPane.setMargin(pane, new Insets(10));
                }

                commentsGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                commentsGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                commentsGrid.setMaxWidth(Region.USE_PREF_SIZE);

                commentsGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                commentsGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                commentsGrid.setMaxHeight(Region.USE_PREF_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Certificat selectedCertificat;
    private void setChosenCommentaire(Certificat certificat) {
        try {
            selectedCertificat = certificat;
            idtf1.setText(String.valueOf(certificat.getCodeCertificat()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void commenter(ActionEvent event) throws SQLException {
        // To be implemented: logic to add comment/certificate
    }

    @FXML
    void updatecommenter(ActionEvent event) throws SQLException {
        // To be implemented: logic to update comment/certificate
    }

    @FXML
    private void ajoutFormation() {
        loadUI("FormationAdd.fxml");
    }

    @FXML
    private void AfficherFormation() {
        loadUI("Formations.fxml");
    }

    @FXML
    private void ajouterCertif() {
        loadUI("CertificatAdd.fxml");
    }

    @FXML
    private void AfficherCertif() {
        loadUI("Certificats.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void retourPostFront(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/PostFront.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
*/