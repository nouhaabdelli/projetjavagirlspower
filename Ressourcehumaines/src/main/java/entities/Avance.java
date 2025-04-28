package entities;

import java.util.Scanner;

public class Avance {
    private int idAvance;
    private double montant;
    private String dateAvance;
    private String niveauUrgence;
    private String motif;

    public Avance() {}

    public Avance(int idAvance, double montant, String dateAvance, String niveauUrgence, String motif) {
        this.idAvance = idAvance;
        this.montant = montant;
        this.dateAvance = dateAvance;
        this.niveauUrgence = niveauUrgence;
        this.motif = motif;
    }

    // ===== Ajout des getters =====
    public int getIdAvance() {
        return idAvance;
    }

    public double getMontant() {
        return montant;
    }

    public String getDateAvance() {
        return dateAvance;
    }

    public String getNiveauUrgence() {
        return niveauUrgence;
    }

    public String getMotif() {
        return motif;
    }

    // ===== Ajout des setters =====
    public void setIdAvance(int idAvance) {
        this.idAvance = idAvance;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setDateAvance(String dateAvance) {
        this.dateAvance = dateAvance;
    }

    public void setNiveauUrgence(String niveauUrgence) {
        this.niveauUrgence = niveauUrgence;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public void ajouter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Ajouter une avance ===");

        System.out.print("ID de l'avance : ");
        idAvance = scanner.nextInt();

        System.out.print("Montant : ");
        montant = scanner.nextDouble();
        scanner.nextLine(); // pour consommer le saut de ligne

        System.out.print("Date de l'avance (YYYY-MM-DD) : ");
        dateAvance = scanner.nextLine();

        System.out.print("Niveau d'urgence (Faible / Moyen / Élevé) : ");
        niveauUrgence = scanner.nextLine();

        System.out.println("Choisissez un motif : ");
        System.out.println("1. Frais médicaux");
        System.out.println("2. Urgence familiale");
        System.out.println("3. Achat d’équipement");
        System.out.println("4. Problèmes financiers");
        System.out.println("5. Mariage");
        System.out.println("6. Autres");
        System.out.print("Votre choix (1-6) : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // pour consommer le saut de ligne

        switch (choix) {
            case 1:
                motif = "Frais médicaux";
                break;
            case 2:
                motif = "Urgence familiale";
                break;
            case 3:
                motif = "Achat d’équipement";
                break;
            case 4:
                motif = "Problèmes financiers";
                break;
            case 5:
                motif = "Mariage";
                break;
            case 6:
                System.out.print("Veuillez préciser le motif : ");
                motif = scanner.nextLine();
                break;
            default:
                motif = "Motif non spécifié";
                break;
        }
    }

    public void modifier() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Modifier l'avance ===");

        System.out.print("Nouveau montant : ");
        montant = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Nouvelle date (YYYY-MM-DD) : ");
        dateAvance = scanner.nextLine();

        System.out.print("Nouveau niveau d'urgence (Faible / Moyen / Élevé) : ");
        niveauUrgence = scanner.nextLine();

        System.out.println("Choisissez un nouveau motif : ");
        System.out.println("1. Frais médicaux");
        System.out.println("2. Urgence familiale");
        System.out.println("3. Achat d’équipement");
        System.out.println("4. Problèmes financiers");
        System.out.println("5. Mariage");
        System.out.println("6. Autres");
        System.out.print("Votre choix (1-6) : ");
        int choix = scanner.nextInt();
        scanner.nextLine();

        switch (choix) {
            case 1:
                motif = "Frais médicaux";
                break;
            case 2:
                motif = "Urgence familiale";
                break;
            case 3:
                motif = "Achat d’équipement";
                break;
            case 4:
                motif = "Problèmes financiers";
                break;
            case 5:
                motif = "Mariage";
                break;
            case 6:
                System.out.print("Veuillez préciser le nouveau motif : ");
                motif = scanner.nextLine();
                break;
            default:
                motif = "Motif non spécifié";
                break;
        }
    }

    public void afficher() {
        System.out.println("\n=== Détails de l'avance ===");
        System.out.println("ID Avance : " + idAvance);
        System.out.println("Montant : " + montant);
        System.out.println("Date de l'avance : " + dateAvance);
        System.out.println("Niveau d'urgence : " + niveauUrgence);
        System.out.println("Motif : " + motif);
    }

    public void supprimer() {
        System.out.println("\n=== Suppression de l'avance ===");
        idAvance = 0;
        montant = 0;
        dateAvance = "";
        niveauUrgence = "";
        motif = "";
        System.out.println("Avance supprimée !");
    }

    public static void main(String[] args) {
        Avance avance = new Avance();
        avance.ajouter();
        avance.afficher();
        avance.modifier();
        avance.supprimer();
        avance.afficher();
    }
}
