package entities;
import java.util.Scanner;


import java.util.Scanner;

public class Pret {
    private int idPret;
    private double montant;
    private int duree; // en mois
    private String datePret;
    private String niveauUrgence;
    private String motif;

    public Pret() {}

    public void ajouter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Ajouter un prêt ===");
        System.out.print("ID du prêt : ");
        idPret = scanner.nextInt();
        System.out.print("Montant : ");
        montant = scanner.nextDouble();
        System.out.print("Durée (en mois) : ");
        duree = scanner.nextInt();
        scanner.nextLine(); // consommer la ligne vide
        System.out.print("Date du prêt (YYYY-MM-DD) : ");
        datePret = scanner.nextLine();
        System.out.print("Niveau d'urgence (Faible / Moyen / Élevé) : ");
        niveauUrgence = scanner.nextLine();
        System.out.print("Motif du prêt : ");
        motif = scanner.nextLine();
    }

    public void modifier() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Modifier le prêt ===");
        System.out.print("Nouveau montant : ");
        montant = scanner.nextDouble();
        System.out.print("Nouvelle durée (en mois) : ");
        duree = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nouvelle date du prêt (YYYY-MM-DD) : ");
        datePret = scanner.nextLine();
        System.out.print("Nouveau niveau d'urgence : ");
        niveauUrgence = scanner.nextLine();
        System.out.print("Nouveau motif du prêt : ");
        motif = scanner.nextLine();
    }

    public void afficher() {
        System.out.println("\n=== Détails du prêt ===");
        System.out.println("ID Prêt : " + idPret);
        System.out.println("Montant : " + montant + " TND");
        System.out.println("Durée : " + duree + " mois");
        System.out.println("Date du prêt : " + datePret);
        System.out.println("Niveau d'urgence : " + niveauUrgence);
        System.out.println("Motif : " + motif);
    }

    public void supprimer() {
        System.out.println("\n=== Suppression du prêt ===");
        idPret = 0;
        montant = 0;
        duree = 0;
        datePret = "";
        niveauUrgence = "";
        motif = "";
        System.out.println("Prêt supprimé !");
    }

    public static void main(String[] args) {
        Pret pret = new Pret();
        pret.ajouter();
        pret.afficher();
        pret.modifier();
        pret.afficher();
        pret.supprimer();
        pret.afficher();
    }
}
