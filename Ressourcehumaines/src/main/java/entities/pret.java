package entities;
import java.math.BigDecimal;
import java.time.LocalDate;

public class pret {
    // Attributs
    private int idPret;
    private BigDecimal montant;
    private int duree;
    private LocalDate datePret;
    private String niveauUrgence;
    private String etat;
    public int userId;

    // Constructeur vide
    public void Pret() {}

    // Constructeur avec paramètres
    public pret(int idPret, BigDecimal montant, int duree, LocalDate datePret, String niveauUrgence, String etat) {
        this.idPret = idPret;
        this.montant = montant;
        this.duree = duree;
        this.datePret = datePret;
        this.niveauUrgence = niveauUrgence;
        this.etat = etat;
    }

    // Getters
    public int getIdPret() {
        return idPret;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public int getDuree() {
        return duree;
    }

    public LocalDate getDatePret() {
        return datePret;
    }

    public String getNiveauUrgence() {
        return niveauUrgence;
    }

    public String getEtat() {
        return etat;
    }

    // Setters
    public void setIdPret(int idPret) {
        this.idPret = idPret;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setDatePret(LocalDate datePret) {
        this.datePret = datePret;
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
