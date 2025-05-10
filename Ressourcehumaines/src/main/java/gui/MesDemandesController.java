package gui;

import entities.Demande;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.sql.*;
import java.time.LocalDate;

public class MesDemandesController {

    @FXML private TableView<Demande> tableViewDemandes;
    @FXML private TableColumn<Demande, Integer> colId;
    @FXML private TableColumn<Demande, String> colType;
    @FXML private TableColumn<Demande, String> colStatut;
    @FXML private TableColumn<Demande, LocalDate> colDateSoumission;
    @FXML private TableColumn<Demande, String> colDescription;
    @FXML private TableColumn<Demande, Void> colActions;

    private int utilisateurId;
    private boolean isAdmin;
    private ObservableList<Demande> observableDemandes = FXCollections.observableArrayList();

    public void initialize() {
        setUtilisateurId(1);   // À adapter pour l'utilisateur actuel
        setAdmin(true);         // À adapter pour vérifier si l'utilisateur est admin

        setupTableColumns();
        loadDemandesFromDB();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colDateSoumission.setCellValueFactory(new PropertyValueFactory<>("dateSoumission"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Setup actions column with buttons (Modifier, Supprimer, Valider)
        colActions.setCellFactory(param -> new TableCell<Demande, Void>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final Button btnValider = new Button("Valider");

            {
                btnModifier.setOnAction(e -> modifierDemande(getTableView().getItems().get(getIndex())));
                btnSupprimer.setOnAction(e -> supprimerDemande(getTableView().getItems().get(getIndex())));
                btnValider.setOnAction(e -> validerDemande(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    btnValider.setVisible(isAdmin);  // Only show 'Valider' for admin
                    HBox hbox = new HBox(5, btnModifier, btnSupprimer, btnValider);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void loadDemandesFromDB() {
        observableDemandes.clear();
        String url = "jdbc:mysql://localhost:3306/workbridge";
        String user = "root";
        String password = "";

        String query = isAdmin ? "SELECT * FROM demande" : "SELECT * FROM demande WHERE utilisateur_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (!isAdmin) stmt.setInt(1, utilisateurId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Demande d = new Demande(
                            rs.getInt("id"),
                            rs.getDate("date_soumission").toLocalDate(),  // Correct column name in DB (date_soumission)
                            rs.getString("statut"),
                            rs.getString("type"),
                            rs.getString("description"),
                            rs.getInt("utilisateur_id")
                    );
                    observableDemandes.add(d);
                }
            }

            tableViewDemandes.setItems(observableDemandes);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifierDemande(Demande demande) {
        // Simulate modifying the demande status to "Modifiée"
        System.out.println("Modifier : " + demande);
        demande.setStatut("Modifiée");
        updateStatutInDB(demande);
    }

    private void supprimerDemande(Demande demande) {
        // Confirmation alert before deleting
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Voulez-vous vraiment supprimer cette demande ?",
                ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                // Perform delete operation
                deleteDemandeFromDB(demande);
                observableDemandes.remove(demande);  // Remove the item from the list
            }
        });
    }

    private void validerDemande(Demande demande) {
        // Change the statut to "Validée" and update in DB
        demande.setStatut("Validée");
        updateStatutInDB(demande);
    }

    private void deleteDemandeFromDB(Demande demande) {
        String url = "jdbc:mysql://localhost:3306/workbridge";
        String user = "root";
        String password = "";

        String query = "DELETE FROM demande WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, demande.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStatutInDB(Demande demande) {
        String url = "jdbc:mysql://localhost:3306/workbridge";
        String user = "root";
        String password = "";

        String query = "UPDATE demande SET statut = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, demande.getStatut());
            stmt.setInt(2, demande.getId());
            stmt.executeUpdate();
            tableViewDemandes.refresh();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters et setters
    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
