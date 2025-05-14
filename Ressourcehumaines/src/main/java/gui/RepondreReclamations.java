package gui;
import entities.Reclamations;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import services.EmailService;
import services.ReclamationService;
import utils.MyConnection;

import java.io.IOException;
import java.sql.*;
import java.util.List;


public class RepondreReclamations {
    @FXML
    private AnchorPane mainContent;
    @FXML
    private TableColumn<?, ?> Fichier_jointe;
    @FXML
    private TableColumn<Reclamations, Void> Reponse;
    @FXML
    private TableColumn<?, ?> Statut;
    @FXML
    private TableColumn<?, ?> colAction;
    @FXML
    private TableColumn<?, ?> colContenu;
    @FXML
    private TableColumn<?, ?> colDate;
    @FXML
    private TableColumn<?, ?> colStatus;
    @FXML
    private TableColumn<?, ?> colTitre;
    @FXML
    private TableView<Reclamations> tableViewReclamations;
    private final ReclamationService reclamationService = new ReclamationService();
    @FXML
    public void initialize() {
        try {
            List<Reclamations> reclamationsList = reclamationService.readAll();
            ObservableList<Reclamations> observableList = FXCollections.observableList(reclamationsList);
            tableViewReclamations.setItems(observableList);
            colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
            colContenu.setCellValueFactory(new PropertyValueFactory<>("description"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("dateDemande"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("priorite"));
            colAction.setCellValueFactory(new PropertyValueFactory<>("RecevoirNotifications"));
            Fichier_jointe.setCellValueFactory(new PropertyValueFactory<>("cheminPieceJointe"));
            Statut.setCellValueFactory(new PropertyValueFactory<>("statut"));
            ajouterBoutonRepondre();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void ajouterBoutonRepondre() {
        Reponse.setCellFactory(param -> new TableCell<Reclamations, Void>() {
            Button btn = new Button("Répondre");

            {
                btn.setOnAction(event -> {

                    Reclamations reclamation = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AjoutReponse.fxml"));
                        Parent root = loader.load();
                        AjouterReponse  controller = loader.getController();
                        controller.setReclamationId(reclamation.getId());
                        controller.setReclamation(reclamation);
                        mainContent.getChildren().setAll(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Répondre à : " + reclamation.getTitre());
                });
            }
            protected void updateItem(Void item, boolean empty) {


                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reclamations reclamation = getTableView().getItems().get(getIndex());

                    if ("traitée".equalsIgnoreCase(reclamation.getStatut())) {

                        btn.setStyle("-fx-background-color: #4a8839; -fx-text-fill: white;");
                        btn.setDisable(true); // facultatif

                    } else {
                        btn.setStyle("-fx-background-color: rgba(129,5,5,0.65); -fx-text-fill: white;");
                        btn.setDisable(false);
                    }
                    setGraphic(btn);

                }



            }
        });
    }



 }












