package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Dashboard implements Initializable {
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button usersBtn;
    @FXML
    private Button demandsBtn;
    @FXML
    private Button complaintsBtn;
    @FXML
    private Button trainingsBtn;
    @FXML
    private Button eventsBtn;
    @FXML
    private Button reportsBtn;
    @FXML
    private TextField searchField;
    @FXML
    private GridPane contentArea;

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        // Charger le tableau de bord par défaut au démarrage
        showDashboard(null);

        // Ajouter des écouteurs pour les boutons
        dashboardBtn.setOnAction(e -> showDashboard(e));
        usersBtn.setOnAction(e -> showUsers(e));
        demandsBtn.setOnAction(e -> showDemands(e));
        complaintsBtn.setOnAction(e -> showComplaints(e));
        trainingsBtn.setOnAction(e -> showTrainings(e));
        eventsBtn.setOnAction(e -> showEvents(e));
        reportsBtn.setOnAction(e -> showReports(e));
    }

    @FXML
    private void showDashboard(javafx.event.ActionEvent event) {
        updateContent("Dashboard", new VBox[] {
                createCard("Gestion des demandes", "12"),
                createCard("Gestion des réclamations", "5"),
                createCard("Gestion des utilisateurs", "3"),
                createCard("Gestion des formations", "8"),
                createCard("Gestion des annonces", "4"),
                createCard("Gestion des événements", "6")
        });
        dashboardBtn.setStyle("-fx-text-fill: white; -fx-background-color: #4a5568;");
        resetOtherButtonsStyle();
    }

    @FXML
    private void showUsers(javafx.event.ActionEvent event) {
        updateContent("Gestion des utilisateurs", new VBox[] {
                createCard("Utilisateurs actifs", "3"),
                createCard("Nouveaux utilisateurs", "1")
        });
        usersBtn.setStyle("-fx-text-fill: white; -fx-background-color: #4a5568;");
        resetOtherButtonsStyle();
    }

    @FXML
    private void showDemands(javafx.event.ActionEvent event) {
        updateContent("Gestion des demandes", new VBox[] {
                createCard("Demandes en attente", "12"),
                createCard("Demandes traitées", "25")
        });
        demandsBtn.setStyle("-fx-text-fill: white; -fx-background-color: #4a5568;");
        resetOtherButtonsStyle();
    }

    @FXML
    private void showComplaints(javafx.event.ActionEvent event) {
        updateContent("Gestion des réclamations", new VBox[] {
                createCard("Réclamations ouvertes", "5"),
                createCard("Réclamations résolues", "10")
        });
        complaintsBtn.setStyle("-fx-text-fill: white; -fx-background-color: #4a5568;");
        resetOtherButtonsStyle();
    }

    @FXML
    private void showTrainings(javafx.event.ActionEvent event) {
        updateContent("Gestion des formations", new VBox[] {
                createCard("Formations planifiées", "8"),
                createCard("Formations terminées", "15")
        });
        trainingsBtn.setStyle("-fx-text-fill: white; -fx-background-color: #4a5568;");
        resetOtherButtonsStyle();
    }

    @FXML
    private void showEvents(javafx.event.ActionEvent event) {
        updateContent("Gestion des événements", new VBox[] {
                createCard("Événements à venir", "6"),
                createCard("Événements passés", "12")
        });
        eventsBtn.setStyle("-fx-text-fill: white; -fx-background-color: #4a5568;");
        resetOtherButtonsStyle();
    }

    @FXML
    private void showReports(javafx.event.ActionEvent event) {
        updateContent("Rapports & Statistiques", new VBox[] {
                createCard("Rapports mensuels", "10"),
                createCard("Statistiques annuelles", "5")
        });
        reportsBtn.setStyle("-fx-text-fill: white; -fx-background-color: #4a5568;");
        resetOtherButtonsStyle();
    }

    private VBox createCard(String title, String count) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 5; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 1);");
        card.getChildren().add(new Label(title) {{
            setStyle("-fx-font-size: 16px;");
        }});
        card.getChildren().add(new Label(count) {{
            setStyle("-fx-font-size: 24px; -fx-text-fill: #4a5568;");
        }});
        return card;
    }

    private void updateContent(String title, VBox[] cards) {
        contentArea.getChildren().clear();
        contentArea.addRow(0, cards);
    }

    private void resetOtherButtonsStyle() {
        usersBtn.setStyle("-fx-text-fill: #a0aec0; -fx-background-color: #2d3748;");
        demandsBtn.setStyle("-fx-text-fill: #a0aec0; -fx-background-color: #2d3748;");
        complaintsBtn.setStyle("-fx-text-fill: #a0aec0; -fx-background-color: #2d3748;");
        trainingsBtn.setStyle("-fx-text-fill: #a0aec0; -fx-background-color: #2d3748;");
        eventsBtn.setStyle("-fx-text-fill: #a0aec0; -fx-background-color: #2d3748;");
        reportsBtn.setStyle("-fx-text-fill: #a0aec0; -fx-background-color: #2d3748;");
    }
}