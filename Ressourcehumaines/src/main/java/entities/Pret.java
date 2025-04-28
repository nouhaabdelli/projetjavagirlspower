package entities;

import javax.swing.*;
import java.util.Scanner;

public class

Pret {

    private int idPret;
    private double montant;
    private int duree; // en mois
    private String datePret;
    private String niveauUrgence;
    private     String motif;

    public Pret() {}

    public Pret(int idPret, double montant, int duree, String datePret, String niveauUrgence, String motif) {
        this.idPret = idPret;
        this.montant = montant;
        this.duree = duree;
        this.datePret = datePret;
        this.niveauUrgence = niveauUrgence;
        this.motif = motif;
    }

    // ======= Getters =======
    public int getIdPret() {
        return idPret;
    }

    public double getMontant() {
        return montant;
    }

    public int getDuree() {
        return duree;
    }

    public String getDatePret() {
        return datePret;
    }

    public String getNiveauUrgence() {
        return niveauUrgence;
    }

    public String getMotif() {
        return motif;
    }

    // ======= Setters =======
    public void setIdPret(int idPret) {
        this.idPret = idPret;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setDatePret(String datePret) {
        this.datePret = datePret;
    }

    public void setNiveauUrgence(String niveauUrgence) {
        this.niveauUrgence = niveauUrgence;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    // ======= Méthodes principales =======
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

//    public void modifier() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("=== Modifier le prêt ===");
//        System.out.print("Nouveau montant : ");
//        montant = scanner.nextDouble();
//        System.out.print("Nouvelle durée (en mois) : ");
//        duree = scanner.nextInt();
//        scanner.nextLine();
//        System.out.print("Nouvelle date du prêt (YYYY-MM-DD) : ");
//        datePret = scanner.nextLine();
//        System.out.print("Nouveau niveau d'urgence : ");
//        niveauUrgence = scanner.nextLine();
//        System.out.print("Nouveau motif du prêt : ");
//        motif = scanner.nextLine();
//    }
//
//    public void afficher() {
//        System.out.println("\n=== Détails du prêt ===");
//        System.out.println("ID Prêt : " + idPret);
//        System.out.println("Montant : " + montant + " TND");
//        System.out.println("Durée : " + duree + " mois");
//        System.out.println("Date du prêt : " + datePret);
//        System.out.println("Niveau d'urgence : " + niveauUrgence);
//        System.out.println("Motif : " + motif);
//    }
//
//    public void supprimer() {
//        System.out.println("\n=== Suppression du prêt ===");
//        idPret = 0;
//        montant = 0;
//        duree = 0;
//        datePret = "";
//        niveauUrgence = "";
//        motif = "";
//        System.out.println("Prêt supprimé !");
//    }
//
//    public static void main(String[] args) {
//        Pret pret = new Pret();
//        pret.ajouter();
//        pret.afficher();
//        pret.modifier();
//        pret.afficher();
//        pret.supprimer();
//        pret.afficher();
//    }
}

