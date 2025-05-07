package entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class pret {
    private int idavance;
    private BigDecimal montant;
    private int duree;
    private LocalDate datePret;
    private String niveauUrgence;
    private String etat;
    private String reponse;

    public pret() {}

    public pret(int idavance, BigDecimal montant, int duree, LocalDate datePret, String niveauUrgence, String etat) {
        this.idavance = idavance;
        this.montant = montant;
        this.duree = duree;
        this.datePret = datePret;
        this.niveauUrgence = niveauUrgence;
        this.etat = etat;
        this.reponse = null; // Allow reponse to be null initially
    }

    public pret(int idavance, BigDecimal montant, int duree, LocalDate datePret, String niveauUrgence, String etat, String reponse) {
        this.idavance = idavance;
        this.montant = montant;
        this.duree = duree;
        this.datePret = datePret;
        this.niveauUrgence = niveauUrgence;
        this.etat = etat;
        this.reponse = reponse;
    }

    public int getIdavance() { return idavance; }
    public void setIdavance(int idavance) { this.idavance = idavance; }
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }
    public LocalDate getDatePret() { return datePret; }
    public void setDatePret(LocalDate datePret) { this.datePret = datePret; }
    public String getNiveauUrgence() { return niveauUrgence; }
    public void setNiveauUrgence(String niveauUrgence) { this.niveauUrgence = niveauUrgence; }
    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }

    @Override
    public String toString() {
        return "pret{" +
                "idavance=" + idavance +
                ", montant=" + montant +
                ", duree=" + duree +
                ", datePret=" + datePret +
                ", niveauUrgence='" + niveauUrgence + '\'' +
                ", etat='" + etat + '\'' +
                ", reponse='" + reponse + '\'' +
                '}';
    }
}