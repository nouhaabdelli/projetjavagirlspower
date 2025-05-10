package gui;

import entities.User;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import java.sql.*;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableRow;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import java.io.FileOutputStream;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.HorizontalAlignment;









public class AfficherUser {
    @FXML
    private Button backcrud;
    @FXML
    private Button btnExportPDF;


    @FXML
    private Button modus;

    @FXML
    private Button suppus;

    @FXML
    private TreeTableView<User> treeus;
    @FXML
    void back(ActionEvent event) {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CrudUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void mod(ActionEvent event) {
        TreeItem<User> selectedItem = treeus.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            selectedUser = selectedItem.getValue();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ModifierUser.fxml"));
                Parent root = loader.load();

                ModifierUser controller = loader.getController();
                controller.setUserData(selectedUser); // passer les données

                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun utilisateur sélectionné.");
        }



    }

    @FXML
    void supp(ActionEvent event) {
        if (selectedUser != null) {
            try {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM user WHERE id = ?");
                stmt.setInt(1, selectedUser.getId());
                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    // Supprimer l'élément sélectionné de la vue
                    TreeItem<User> selectedItem = treeus.getSelectionModel().getSelectedItem();
                    selectedItem.getParent().getChildren().remove(selectedItem);
                    System.out.println("Utilisateur supprimé !");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun utilisateur sélectionné.");
        }
    }

    private User selectedUser;
    @FXML
    public void initialize() {
        // 🔹 Création des colonnes pour chaque attribut
        TreeTableColumn<User, Integer> idCol = new TreeTableColumn<>("ID");
        idCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));

        TreeTableColumn<User, String> nomCol = new TreeTableColumn<>("Nom");
        nomCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("nom"));

        TreeTableColumn<User, String> prenomCol = new TreeTableColumn<>("Prénom");
        prenomCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("prenom"));

        TreeTableColumn<User, String> emailCol = new TreeTableColumn<>("Email");
        emailCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("email"));

        TreeTableColumn<User, String> roleCol = new TreeTableColumn<>("Rôle");
        roleCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("role"));

        TreeTableColumn<User, String> statutCol = new TreeTableColumn<>("Statut");
        statutCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("statut"));


        TreeTableColumn<User, String> adresseCol = new TreeTableColumn<>("Adresse");
        adresseCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("adresse"));

        TreeTableColumn<User, String> genreCol = new TreeTableColumn<>("Genre");
        genreCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("genre"));

        TreeTableColumn<User, String> telCol = new TreeTableColumn<>("Téléphone");
        telCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("numTelephone"));
        TreeTableColumn<User, Integer> cinCol = new TreeTableColumn<>("CIN");
        cinCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("cin"));

        TreeTableColumn<User, Void> actionCol = new TreeTableColumn<>("Actions");

        actionCol.setCellFactory(param -> new TreeTableCell<>() {
            private final Button btnEdit = new Button("Modifier");
            private final Button btnDelete = new Button("Supprimer");

            {


                btnEdit.setOnAction((ActionEvent event) -> {
                    User user = getTreeTableRow().getItem();
                    if (user != null) {
                        treeus.getSelectionModel().select(getTreeTableRow().getIndex()); // sélection visuelle
                        selectedUser = user;
                        mod(new ActionEvent(btnEdit, null));
                    }
                });


                btnDelete.setOnAction((ActionEvent event) -> {
                    User user = getTreeTableRow().getItem();
                    if (user != null) {
                        treeus.getSelectionModel().select(getTreeTableRow().getIndex()); // sélection visuelle

                        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation de suppression");
                        alert.setHeaderText("Voulez-vous vraiment supprimer cet utilisateur ?");
                        alert.setContentText("Cette action est irréversible.");

                        alert.showAndWait().ifPresent(response -> {
                            if (response == javafx.scene.control.ButtonType.OK) {
                                selectedUser = user;
                                supp(new ActionEvent(btnDelete, null));
                            }
                        });
                    }
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTreeTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, btnEdit, btnDelete);
                    setGraphic(box);
                }
            }
        });



        treeus.getColumns().setAll(nomCol, prenomCol, emailCol, roleCol, statutCol, adresseCol, genreCol, telCol,cinCol,actionCol);



        ObservableList<User> userList = FXCollections.observableArrayList();


        treeus.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<User> selectedItem = treeus.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    selectedUser = selectedItem.getValue();
                    System.out.println("Utilisateur sélectionné : " + selectedUser.getNom());
                }
            }
        });


        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user");

            while (rs.next()) {
                User u = new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("dateNaissance").toLocalDate(),
                        rs.getString("motDePasse"),
                        rs.getString("email"),
                        rs.getString("numTelephone"),
                        rs.getString("role"),
                        rs.getString("rib"),
                        rs.getInt("nombreEnfant"),
                        rs.getString("cnam"),
                        rs.getDate("dateEmbauche").toLocalDate(),
                        rs.getString("photoProfil"),
                        rs.getString("statut"),
                        rs.getString("adresse"),
                        rs.getString("genre"),
                        rs.getString("situationFamiliale"),
                        rs.getInt("cin")
                );
                userList.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 🔹 Création du root et remplissage
        TreeItem<User> root = new TreeItem<>(new User());
        for (User u : userList) {
            root.getChildren().add(new TreeItem<>(u));
        }

        treeus.setRoot(root);
        treeus.setShowRoot(false);
    }
    private Cell headerStyle(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold())
                .setBackgroundColor(com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY)
                .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                .setBorder(new com.itextpdf.layout.borders.SolidBorder(1));
    }

    @FXML
    void exportPDF(ActionEvent event) {
        try {
            // Chemin de sortie vers le bureau
            String outputPath = System.getProperty("user.home") + "\\Desktop\\utilisateur.pdf";
            System.out.println("Chemin d'exportation : " + outputPath);

            // Création du document PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            document.setMargins(20, 20, 20, 20);

            // ✅ Utilise des doubles antislashs ou des slashes simples
            String logoPath = "C:\\Users\\Hamdi\\Desktop\\pi\\projetjavagirlspower\\Ressourcehumaines\\src\\main\\resources\\images\\work.png";

// ✅ Création et ajout du logo
            ImageData imageData = ImageDataFactory.create(logoPath);
            Image logo = new Image(imageData)
                    .scaleToFit(100, 100)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(logo);


            // Titre stylisé
            Paragraph title = new Paragraph("Liste des utilisateurs")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18)
                    .setBold()
                    .setFontColor(ColorConstants.BLUE);
            document.add(title);
            document.add(new Paragraph("\n")); // saut de ligne

            // Création du tableau
            float[] columnWidths = {100, 100, 150, 100, 100};
            Table table = new Table(columnWidths).setWidth(UnitValue.createPercentValue(100));



            table.addHeaderCell(headerStyle("Nom"));
            table.addHeaderCell(headerStyle("Prénom"));
            table.addHeaderCell(headerStyle("Email"));
            table.addHeaderCell(headerStyle("Rôle"));
            table.addHeaderCell(headerStyle("Statut"));

            // Remplir le tableau
            for (TreeItem<User> item : treeus.getRoot().getChildren()) {
                User user = item.getValue();
                table.addCell(new Cell().add(new Paragraph(user.getNom())).setBorder(new SolidBorder(0.5f)));
                table.addCell(new Cell().add(new Paragraph(user.getPrenom())).setBorder(new SolidBorder(0.5f)));
                table.addCell(new Cell().add(new Paragraph(user.getEmail())).setBorder(new SolidBorder(0.5f)));
                table.addCell(new Cell().add(new Paragraph(user.getRole())).setBorder(new SolidBorder(0.5f)));
                table.addCell(new Cell().add(new Paragraph(user.getStatut())).setBorder(new SolidBorder(0.5f)));
            }

            document.add(table);
            document.close();

            // Écriture dans le fichier
            FileOutputStream fos = new FileOutputStream(outputPath);
            fos.write(baos.toByteArray());
            fos.close();

            // Confirmation
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Export PDF");
            alert.setHeaderText(null);
            alert.setContentText("PDF exporté avec succès !");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}