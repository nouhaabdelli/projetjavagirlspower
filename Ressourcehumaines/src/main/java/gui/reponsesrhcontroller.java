package gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;

public class reponsesrhcontroller {

    @FXML
    private TreeView<String> responseTree;

    @FXML
    private void initialize() {
        TreeItem<String> root = new TreeItem<>("Réponses RH");
        // Example data (replace with dynamic data from database)
        TreeItem<String> response1 = new TreeItem<>("Réponse 1");
        TreeItem<String> response2 = new TreeItem<>("Réponse 2");
        root.getChildren().addAll(response1, response2);

        response1.getChildren().add(new TreeItem<>("Contenu: Exemple 1"));
        response2.getChildren().add(new TreeItem<>("Contenu: Exemple 2"));

        responseTree.setRoot(root);
        root.setExpanded(true);

        // Add buttons for each response
        responseTree.setCellFactory(param -> new TreeCell<String>() {
            private final Button acceptButton = new Button("Accepter");
            private final Button rejectButton = new Button("Rejeter");

            {
                acceptButton.getStyleClass().add("accept-button");
                rejectButton.getStyleClass().add("reject-button");
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    if (getTreeItem().getParent() != null && !getTreeItem().isLeaf()) {
                        setGraphic(new HBox(10, acceptButton, rejectButton));
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }
}