package utils;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFSignatureUtil {
    
    public static void signPDF(String inputPath, String outputPath, String signatureText) throws IOException {
        try (PdfReader reader = new PdfReader(inputPath);
             PdfWriter writer = new PdfWriter(outputPath);
             PdfDocument pdfDoc = new PdfDocument(reader, writer);
             Document document = new Document(pdfDoc)) {

            // Obtenir la dernière page
            PdfPage lastPage = pdfDoc.getLastPage();
            float pageWidth = lastPage.getPageSize().getWidth();
            float pageHeight = lastPage.getPageSize().getHeight();

            // Ajouter le texte de signature avec mise en forme
            String[] lines = signatureText.split("\n");
            for (String line : lines) {
                Paragraph p = new Paragraph(line);
                p.setFontSize(12);
                p.setMarginBottom(5);
                p.setMarginLeft(pageWidth * 0.6f); // Aligner à droite
                document.add(p);
            }

            // Ajouter une ligne de signature
            Paragraph signatureLine = new Paragraph("_____________________________");
            signatureLine.setFontSize(12);
            signatureLine.setMarginTop(10);
            signatureLine.setMarginLeft(pageWidth * 0.6f); // Aligner à droite
            document.add(signatureLine);

            document.close();
        }
    }

    public static void createDigitalSignature(String inputPath, String outputPath, String signatureText) {
        try {
            // Vérifier si le fichier d'entrée existe
            File inputFile = new File(inputPath);
            if (!inputFile.exists()) {
                throw new IOException("Le fichier d'entrée n'existe pas: " + inputPath);
            }

            // Créer le répertoire de sortie si nécessaire
            File outputFile = new File(outputPath);
            outputFile.getParentFile().mkdirs();

            // Signer le PDF
            signPDF(inputPath, outputPath, signatureText);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la signature du PDF: " + e.getMessage());
        }
    }

    public static boolean verifySignature(String pdfPath) {
        try {
            PdfReader reader = new PdfReader(pdfPath);
            PdfDocument pdfDoc = new PdfDocument(reader);
            return pdfDoc.getNumberOfPages() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 