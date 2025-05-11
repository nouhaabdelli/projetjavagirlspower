package gui;

import entities.Demande;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class MesDemandesController {
    @FXML
    private TableView<Demande> tableViewDemandes;
    @FXML
    private TableColumn<Demande, Integer> colId;
    @FXML
    private TableColumn<Demande, String> colType;
    @FXML
    private TableColumn<Demande, String> colStatut;
    @FXML
    private TableColumn<Demande, LocalDate> colDateSoumission;
    @FXML
    private TableColumn<Demande, String> colDescription;
    @FXML
    private TableColumn<Demande, LocalDate> colDateValidation;
    @FXML
    private TableColumn<Demande, Void> colActions;
    @FXML
    private Label lblPrediction;
    private int utilisateurId;
    private boolean isAdmin;
    private ObservableList<Demande> observableDemandes = FXCollections.observableArrayList();

    public MesDemandesController() {
    }

    public void initialize() {
        this.setUtilisateurId(1);
        this.setAdmin(true);
        this.setupTableColumns();
        this.loadDemandesFromDB();
    }

    private void setupTableColumns() {
        this.colId.setCellValueFactory(new PropertyValueFactory("id"));
        this.colType.setCellValueFactory(new PropertyValueFactory("type"));
        this.colStatut.setCellValueFactory(new PropertyValueFactory("statut"));
        this.colDateSoumission.setCellValueFactory(new PropertyValueFactory("dateSoumission"));
        this.colDescription.setCellValueFactory(new PropertyValueFactory("description"));
        this.colDateValidation.setCellValueFactory(new PropertyValueFactory("dateValidation"));
        this.colActions.setCellFactory((param) -> new TableCell<Demande, Void>() {
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final Button btnValider = new Button("Valider");
            private final Button btnPredire = new Button("Prédire");

            {
                this.btnModifier.setOnAction((e) -> MesDemandesController.this.modifierDemande(this.getCurrentDemande()));
                this.btnSupprimer.setOnAction((e) -> MesDemandesController.this.supprimerDemande(this.getCurrentDemande()));
                this.btnValider.setOnAction((e) -> MesDemandesController.this.validerDemande(this.getCurrentDemande()));
                this.btnPredire.setOnAction((e) -> MesDemandesController.this.predireTempsReponse(this.getCurrentDemande()));
            }

            private Demande getCurrentDemande() {
                return (Demande)this.getTableView().getItems().get(this.getIndex());
            }

            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    this.setGraphic((Node)null);
                } else {
                    this.btnValider.setVisible(MesDemandesController.this.isAdmin);
                    HBox hbox = new HBox((double)5.0F, new Node[]{this.btnModifier, this.btnSupprimer, this.btnValider, this.btnPredire});
                    this.setGraphic(hbox);
                }

            }
        });
    }

    private void validerDemande(Demande demande) {
        demande.setStatut("Validée");
        LocalDate currentDate = LocalDate.now();
        demande.setDateValidation(currentDate);
        this.updateStatutAndValidationDateInDB(demande);
        this.tableViewDemandes.refresh();
    }

    private void updateStatutAndValidationDateInDB(Demande demande) {
        String url = "jdbc:mysql://localhost:3306/workbridge";
        String user = "root";
        String password = "";
        String query = "UPDATE demande SET statut = ?, date_validation = ? WHERE id = ?";

        try (
                Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement stmt = conn.prepareStatement(query);
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
        this.observableDemandes.clear();
        String query = this.isAdmin ? "SELECT * FROM demande" : "SELECT * FROM demande WHERE utilisateur_id = ?";

        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            if (!this.isAdmin) {
                stmt.setInt(1, this.utilisateurId);
            }

            Demande d;
            try (ResultSet rs = stmt.executeQuery()) {
                for(; rs.next(); this.observableDemandes.add(d)) {
                    d = new Demande(rs.getInt("id"), rs.getDate("date_soumission").toLocalDate(), rs.getString("statut"), rs.getString("type"), rs.getString("description"), rs.getInt("utilisateur_id"));
                    Date dateVal = rs.getDate("date_validation");
                    if (dateVal != null) {
                        d.setDateValidation(dateVal.toLocalDate());
                    }
                }
            }

            this.tableViewDemandes.setItems(this.observableDemandes);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void predireTempsReponse(Demande demande) {
        if (demande.getDateValidation() != null) {
            long jours = this.calculateResponseTime(demande);
            int var10000 = demande.getId();
            String result = "Demande ID " + var10000 + " : " + jours + " jours.";
            this.lblPrediction.setText(result);
        } else {
            this.lblPrediction.setText("Demande ID " + demande.getId() + " : pas encore validée.");
        }

    }

    private long calculateResponseTime(Demande demande) {
        return Duration.between(demande.getDateSoumission().atStartOfDay(), demande.getDateValidation().atStartOfDay()).toDays();
    }

    private void modifierDemande(Demande demande) {
        demande.setStatut("Modifiée");
        this.updateStatutInDB(demande);
    }

    private void supprimerDemande(Demande demande) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette demande ?", new ButtonType[]{ButtonType.YES, ButtonType.NO});
        alert.setHeaderText((String)null);
        alert.showAndWait().ifPresent((response) -> {
            if (response == ButtonType.YES) {
                this.deleteDemandeFromDB(demande);
                this.observableDemandes.remove(demande);
            }

        });
    }

    private void updateStatutInDB(Demande demande) {
        String query = "UPDATE demande SET statut = ? WHERE id = ?";

        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, demande.getStatut());
            stmt.setInt(2, demande.getId());
            stmt.executeUpdate();
            this.tableViewDemandes.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void deleteDemandeFromDB(Demande demande) {
        String query = "DELETE FROM demande WHERE id = ?";

        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, demande.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int getUtilisateurId() {
        return this.utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }
}