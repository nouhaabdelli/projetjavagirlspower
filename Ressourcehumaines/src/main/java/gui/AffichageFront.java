package gui;

import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AffichageFront implements Initializable {

    @FXML
    private TreeTableView<User> treeFront;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 🔹 Création des colonnes
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


        treeFront.getColumns().setAll(nomCol, prenomCol, emailCol, roleCol, statutCol, adresseCol, genreCol, telCol,cinCol);

        // 🔹 Chargement des données depuis la base
        ObservableList<User> userList = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM user")) {

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

        // 🔹 Remplir TreeTableView
        TreeItem<User> root = new TreeItem<>(new User()); // racine fictive
        for (User u : userList) {
            root.getChildren().add(new TreeItem<>(u));
        }
        treeFront.setRoot(root);
        treeFront.setShowRoot(false);
    }
}
