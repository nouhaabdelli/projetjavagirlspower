package gui;

import javafx.scene.Node;
import javafx.stage.Stage;
import entities.*;
import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import services.UserService;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
public class Afficherdetail {
    @FXML
    private Text adresseText;
    @FXML
    private Text cinText;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Text emailText;
    @FXML
    private Text genreText;
    @FXML
    private Hyperlink hyperlinkPieceJointe;
    @FXML
    private Text nomText;
    @FXML
    private Text notificationsText;
    @FXML
    private Text numTelephoneText;
    @FXML
    private Text prenomText;
    @FXML
    private Text prioriteText;
    @FXML
    private Text titreText;
    @FXML
    private UserService userService = new UserService();
    private Reclamations reclamation;
    private User user;
      public void setReclamation(Reclamations reclamation, User user) {
          this.reclamation = reclamation;
          this.user = user;
          // Affichage des informations de l'utilisateur
          nomText.setText("Nom : " + user.getNom());
          prenomText.setText("Prénom : " + user.getPrenom());
          emailText.setText("Email : " + user.getEmail());
          cinText.setText("CIN : " + user.getCin());
          numTelephoneText.setText("Numéro de téléphone : " + user.getNumTelephone());
          genreText.setText("Genre : " + user.getGenre());
          adresseText.setText("Adresse : " + user.getAdresse());

          // Affichage des informations de la réclamation
          titreText.setText(reclamation.getTitre());
          descriptionTextArea.setText(reclamation.getDescription());

          // Affichage des notifications et priorités
          notificationsText.setText(reclamation.getRecevoirNotifications());
          prioriteText.setText(reclamation.getPriorite());

          if (reclamation.getCheminPieceJointe() != null && !reclamation.getCheminPieceJointe().isEmpty()) {
              hyperlinkPieceJointe.setText("Ouvrir pièce jointe");
              hyperlinkPieceJointe.setOnAction(e -> {
                  try {
                      Desktop.getDesktop().open(new File(reclamation.getCheminPieceJointe()));
                  } catch (IOException ex) {
                      ex.printStackTrace();
                  }
              });
          } else {
              hyperlinkPieceJointe.setText("Aucune pièce jointe");
              hyperlinkPieceJointe.setDisable(true);
          } }
          @FXML
          void quitterButton(ActionEvent event) {
              Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
              stage.close();
          }

      }