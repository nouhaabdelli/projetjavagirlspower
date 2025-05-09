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





public class AfficherUser {
    @FXML
    private Button backcrud;

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

        // ➕ Tu peux ajouter toutes les colonnes de la même manière
        // Exemple :
        TreeTableColumn<User, String> adresseCol = new TreeTableColumn<>("Adresse");
        adresseCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("adresse"));

        TreeTableColumn<User, String> genreCol = new TreeTableColumn<>("Genre");
        genreCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("genre"));

        TreeTableColumn<User, String> telCol = new TreeTableColumn<>("Téléphone");
        telCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("numTelephone"));
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


        // 🔹 Ajout des colonnes à la table
        treeus.getColumns().setAll(nomCol, prenomCol, emailCol, roleCol, statutCol, adresseCol, genreCol, telCol,actionCol);


        // 🔹 Connexion à la base de données et récupération des utilisateurs
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
}