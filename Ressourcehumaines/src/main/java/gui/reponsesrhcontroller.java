package gui;

import entities.avance;
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
import services.avanceservice;
import java.io.IOException;
import java.sql.SQLException;

public class reponsesrhcontroller {
    @FXML
    private AnchorPane mainContent;

    @FXML
    private TreeView<String> avanceTree;

    private ObservableList<avance> avanceList;

    @FXML
    private void initialize() {
        if (avanceTree != null) {
            // Charger les données initiales
            rafraichirTree();

            // Configurer le TreeView
            avanceTree.setShowRoot(false);
            avanceTree.setCellFactory(param -> new TreeCell<String>() {
                private final Button acceptButton = new Button("Accepter");
                private final Button rejectButton = new Button("Rejeter");

                {
                    acceptButton.setStyle("-fx-font-size: 12px; -fx-text-fill: white; -fx-background-color: #28a745; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #28a745, 10, 0.5, 0, 0); -fx-transition: all 0.3s ease;");
                    rejectButton.setStyle("-fx-font-size: 12px; -fx-text-fill: white; -fx-background-color: #dc3545; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #dc3545, 10, 0.5, 0, 0); -fx-transition: all 0.3s ease;");

                    acceptButton.setOnAction(event -> {
                        avance avance = avanceList.get(getIndex());
                        accepterAvance(avance);
                    });

                    rejectButton.setOnAction(event -> {
                        avance avance = avanceList.get(getIndex());
                        rejeterAvance(avance);
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
            System.err.println("avanceTree est null dans reponsesrhcontroller.initialize()");
        }
    }

    private void rafraichirTree() {
        if (avanceTree != null) {
            try {
                avanceservice avanceService = new avanceservice();
                avanceList = FXCollections.observableArrayList(avanceService.readAll());
                TreeItem<String> root = new TreeItem<>();
                for (avance a : avanceList) {
                    String itemText = String.format("Employé: %s, Montant: %s, Durée: %d, Date: %s, Urgence: %s, État: %s",
                            a.getPrenomUser() != null ? a.getPrenomUser() : "Inconnu",
                            a.getMontant(), a.getDuree(), a.getDateAvance(), a.getNiveauUrgence(), a.getEtat());
                    TreeItem<String> item = new TreeItem<>(itemText);
                    root.getChildren().add(item);
                }
                avanceTree.setRoot(root);
            } catch (SQLException e) {
                System.err.println("Erreur lors du chargement des avances : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void accepterAvance(avance avance) {
        try {
            avance.setEtat("Accepté");
            avanceservice avanceService = new avanceservice();
            avanceService.update(avance);
            rafraichirTree();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Avance acceptée avec succès !");
            alert.showAndWait();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'acceptation : " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'acceptation : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void rejeterAvance(avance avance) {
        try {
            avance.setEtat("Rejeté");
            avanceservice avanceService = new avanceservice();
            avanceService.update(avance);
            rafraichirTree();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Avance rejetée avec succès !");
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
            System.out.println("Tentative de retour à avance.fxml depuis reponsesrh.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/avance.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            Scene scene = new Scene(root);
            if (getClass().getResource("/style/reponsesrh.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/style/reponsesrh.css").toExternalForm());
                System.out.println("reponsesrh.css chargé avec succès");
            } else {
                System.err.println("Erreur : Le fichier reponsesrh.css n'est pas trouvé dans les ressources.");
            }
            newStage.setScene(scene);
            newStage.show();
            // Use avanceTree as a fallback if mainContent is null
            if (mainContent != null && mainContent.getScene() != null) {
                Stage currentStage = (Stage) mainContent.getScene().getWindow();
                if (currentStage != null) {
                    currentStage.close();
                }
            } else if (avanceTree != null && avanceTree.getScene() != null) {
                Stage currentStage = (Stage) avanceTree.getScene().getWindow();
                if (currentStage != null) {
                    currentStage.close();
                }
            } else {
                System.err.println("Both mainContent and avanceTree scenes are null, cannot close the current stage.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de avance.fxml dans reponsesrhcontroller : " + e.getMessage());
            e.printStackTrace();
        }
    }
}