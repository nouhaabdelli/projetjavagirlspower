package entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class avance {
    private int idAvance;
    private BigDecimal montant;
    private int duree;
    private LocalDate dateAvance;
    private String niveauUrgence;
    private String etat;

    // Constructeur vide
    public avance() {}

    // Constructeur avec paramètres
    public avance(int idAvance, BigDecimal montant, int duree,LocalDate dateAvance, String niveauUrgence, String etat) {
        this.idAvance = idAvance;
        this.montant = montant;
        this.duree = duree;
        this.dateAvance = dateAvance;
        this.niveauUrgence = niveauUrgence;
        this.etat = etat;
    }

    // Getters
    public int getIdAvance() { return idAvance; }
    public BigDecimal getMontant() { return montant; }
    public int getDuree() {
        return duree;
    }
    public LocalDate getDateAvance() { return dateAvance; }
    public String getNiveauUrgence() { return niveauUrgence; }
    public String getEtat() { return etat; }

    // Setters
    public void setIdAvance(int idAvance) { this.idAvance = idAvance; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public void setDuree(int duree) {
        this.duree = duree;
    }
    public void setDateAvance(LocalDate dateAvance) { this.dateAvance = dateAvance; }
    public void setNiveauUrgence(String niveauUrgence) { this.niveauUrgence = niveauUrgence; }
    public void setEtat(String etat) { this.etat = etat; }
}