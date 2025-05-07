package gui;

import entities.pret;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.pretservice;

import java.io.IOException;
import java.sql.SQLException;

public class reponsespretrhcontroller {
    @FXML
    private AnchorPane mainContent;

    @FXML
    private TreeView<String> pretTree;

    private ObservableList<pret> pretList;

    @FXML
    private void initialize() {
        if (pretTree != null) {
            rafraichirTree();

            pretTree.setShowRoot(false);
            pretTree.setCellFactory(param -> new TreeCell<String>() {
                private final Button acceptButton = new Button("Accepter");
                private final Button rejectButton = new Button("Rejeter");

                {
                    acceptButton.setStyle("-fx-font-size: 12px; -fx-text-fill: white; -fx-background-color: #28a745; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #28a745, 10, 0.5, 0, 0); -fx-transition: all 0.3s ease;");
                    rejectButton.setStyle("-fx-font-size: 12px; -fx-text-fill: white; -fx-background-color: #dc3545; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #dc3545, 10, 0.5, 0, 0); -fx-transition: all 0.3s ease;");

                    acceptButton.setOnAction(event -> {
                        pret pret = pretList.get(getIndex());
                        accepterPret(pret);
                    });

                    rejectButton.setOnAction(event -> {
                        pret pret = pretList.get(getIndex());
                        rejeterPret(pret);
                    });

                    acceptButton.setOnMouseEntered(e -> acceptButton.setStyle("-fx-background-color: #218838; -fx-scale-x: 1.1; -fx-scale-y: 1.1; -fx-effect: dropshadow(gaussian, #28a745, 15, 0.7, 0, 0);"));
                    acceptButton.setOnMouseExited(e -> acceptButton.setStyle("-fx-font-size: 12px; -fx-text-fill: white; -fx-background-color: #28a745; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #28a745, 10, 0.5, 0, 0);"));

                    rejectButton.setOnMouseEntered(e -> rejectButton.setStyle("-fx-background-color: #c82333; -fx-scale-x: 1.1; -fx-scale-y: 1.1; -fx-effect: dropshadow(gaussian, #dc3545, 15, 0.7, 0, 0);"));
                    rejectButton.setOnMouseExited(e -> rejectButton.setStyle("-fx-font-size: 12px; -fx-text-fill: white; -fx-background-color: #dc3545; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #dc3545, 10, 0.5, 0, 0);"));
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                        setGraphic(new HBox(10, acceptButton, rejectButton));
                    }
                }
            });
        } else {
            System.err.println("pretTree est null dans reponsespretrhcontroller.initialize()");
        }
    }

    private void rafraichirTree() {
        if (pretTree != null) {
            try {
                pretservice pretService = new pretservice();
                pretList = FXCollections.observableArrayList(pretService.readAll());
                TreeItem<String> root = new TreeItem<>();
                for (pret p : pretList) {
                    String itemText = String.format("ID: %d, Montant: %s, Date: %s, Durée: %d, Urgence: %s, État: %s, Réponse: %s",
                            p.getIdavance(), p.getMontant(), p.getDatePret(), p.getDuree(), p.getNiveauUrgence(), p.getEtat(), p.getReponse());
                    TreeItem<String> item = new TreeItem<>(itemText);
                    root.getChildren().add(item);
                }
                pretTree.setRoot(root);
            } catch (SQLException e) {
                System.err.println("Erreur lors du chargement des prêts : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void accepterPret(pret pret) {
        try {
            pret.setEtat("Accepté");
            pret.setReponse("accepter");
            pretservice pretService = new pretservice();
            pretService.update(pret);
            rafraichirTree();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Prêt accepté avec succès !");
            alert.showAndWait();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'acceptation : " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'acceptation : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void rejeterPret(pret pret) {
        try {
            pret.setEtat("Rejeté");
            pret.setReponse("rejeter");
            pretservice pretService = new pretservice();
            pretService.update(pret);
            rafraichirTree();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Prêt rejeté avec succès !");
            alert.showAndWait();
        } catch (SQLException e) {
            System.err.println("Erreur lors du rejet : " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors du rejet : " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleRetour() {
        try {
            System.out.println("Retour à pret.fxml depuis reponsespretrh.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pret.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/finance.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/finance.css").toExternalForm());
                System.out.println("finance.css chargé avec succès");
            }
            newStage.setScene(scene);
            newStage.show();
            if (mainContent != null && mainContent.getScene() != null) {
                Stage currentStage = (Stage) mainContent.getScene().getWindow();
                if (currentStage != null) {
                    currentStage.close();
                }
            } else {
                System.err.println("mainContent or its scene is null, cannot close the current stage.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de pret.fxml dans reponsespretrhcontroller : " + e.getMessage());
            e.printStackTrace();
        }
    }
}