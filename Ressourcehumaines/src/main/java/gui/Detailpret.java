package gui;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import entities.pret;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.File;
import java.io.FileOutputStream;
import java.awt.Desktop;

public class Detailpret {

    @FXML
    private Label labelMontant;
    @FXML
    private Label labelDuree;
    @FXML
    private Label labelDatePret;
    @FXML
    private Label labelNiveauUrgence;
    @FXML
    private Label labelEtat;

    private pret currentPret; // pour stocker les données

    public void setPret(pret p) {
        currentPret = p;
        labelMontant.setText(p.getMontant().toString());
        labelDuree.setText(String.valueOf(p.getDuree()));
        labelDatePret.setText(p.getDatePret().toString());
        labelNiveauUrgence.setText(p.getNiveauUrgence());
        labelEtat.setText(p.getEtat());
    }

    @FXML
    public void genererPDF() {
        if (currentPret == null) return;

        try {
            Document document = new Document();
            File pdfFile = new File("DetailPret.pdf");

            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            Font fontTitre = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLUE);
            Font fontTexte = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Détails du Prêt", fontTitre));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Montant : " + currentPret.getMontant(), fontTexte));
            document.add(new Paragraph("Durée : " + currentPret.getDuree(), fontTexte));
            document.add(new Paragraph("Date du Prêt : " + currentPret.getDatePret(), fontTexte));
            document.add(new Paragraph("Niveau d'urgence : " + currentPret.getNiveauUrgence(), fontTexte));
            document.add(new Paragraph("État : " + currentPret.getEtat(), fontTexte));

            document.close();

            // Ouvrir automatiquement le PDF
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
