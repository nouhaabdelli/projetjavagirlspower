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
    private int id_user;
    private String prenomUser; // Added to store employee's name

    // Constructeur vide
    public avance() {}

    // Constructor without id_user and prenomUser (to be set programmatically)
    public avance(int idAvance, BigDecimal montant, int duree, LocalDate dateAvance, String niveauUrgence, String etat) {
        this.idAvance = idAvance;
        this.montant = montant;
        this.duree = duree;
        this.dateAvance = dateAvance;
        this.niveauUrgence = niveauUrgence;
        this.etat = etat;
        this.id_user = 0;
    }

    // Full constructor (for service use)
    public avance(int idAvance, BigDecimal montant, int duree, LocalDate dateAvance, String niveauUrgence, String etat, int id_user, String prenomUser) {
        this.idAvance = idAvance;
        this.montant = montant;
        this.duree = duree;
        this.dateAvance = dateAvance;
        this.niveauUrgence = niveauUrgence;
        this.etat = etat;
        this.id_user = id_user;
        this.prenomUser = prenomUser;
    }

    // Getters
    public int getIdAvance() { return idAvance; }
    public BigDecimal getMontant() { return montant; }
    public int getDuree() { return duree; }
    public LocalDate getDateAvance() { return dateAvance; }
    public String getNiveauUrgence() { return niveauUrgence; }
    public String getEtat() { return etat; }
    public int getId_user() { return id_user; }
    public String getPrenomUser() { return prenomUser; }

    // Setters
    public void setIdAvance(int idAvance) { this.idAvance = idAvance; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    public void setDuree(int duree) { this.duree = duree; }
    public void setDateAvance(LocalDate dateAvance) { this.dateAvance = dateAvance; }
    public void setNiveauUrgence(String niveauUrgence) { this.niveauUrgence = niveauUrgence; }
    public void setEtat(String etat) { this.etat = etat; }
    public void setId_user(int id_user) { this.id_user = id_user; }
    public void setPrenomUser(String prenomUser) { this.prenomUser = prenomUser; }
}