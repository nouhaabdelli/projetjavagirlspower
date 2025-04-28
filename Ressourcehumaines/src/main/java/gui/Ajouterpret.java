package gui;
import entities.Pret ;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.Scanner;


public class Ajouterpret {
    @FXML
    private TextField datePret;

    @FXML
    private TextField duree;

    @FXML
    private TextField montant;

    @FXML
    void ajouter(ActionEvent event) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Ajouter un prêt ===");
        System.out.print("Montant : ");
        montant = scanner.nextDouble();
        System.out.print("Durée (en mois) : ");
        duree = scanner.nextInt();
        scanner.nextLine(); // consommer la ligne vide
        System.out.print("Date du prêt (YYYY-MM-DD) : ");
        datePret = scanner.nextLine();
//        System.out.print("Niveau d'urgence (Faible / Moyen / Élevé) : ");
//        niveauUrgence = scanner.nextLine();
//        System.out.print("Motif du prêt : ");
//        motif = scanner.nextLine();

    }

}
