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
    private int userId;

    // Constructeur vide
    public avance() {}

    // Constructeur complet
    public avance(int idAvance, BigDecimal montant, int duree, LocalDate dateAvance, String niveauUrgence, String etat, int userId) {
        this.idAvance = idAvance;
        this.montant = montant;
        this.duree = duree;
        this.dateAvance = dateAvance;
        this.niveauUrgence = niveauUrgence;
        this.etat = etat;
        this.userId = userId;
    }

    // Getters
    public int getIdAvance() {
        return idAvance;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public int getDuree() {
        return duree;
    }

    public LocalDate getDateAvance() {
        return dateAvance;
    }

    public String getNiveauUrgence() {
        return niveauUrgence;
    }

    public String getEtat() {
        return etat;
    }

    public int getUserId() {
        return userId;
    }

    // Setters
    public void setIdAvance(int idAvance) {
        this.idAvance = idAvance;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setDateAvance(LocalDate dateAvance) {
        this.dateAvance = dateAvance;
    }

    public void setNiveauUrgence(String niveauUrgence) {
        this.niveauUrgence = niveauUrgence;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
