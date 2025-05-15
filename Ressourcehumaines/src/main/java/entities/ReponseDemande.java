package entities;

import java.time.LocalDate;

public class ReponseDemande {
    private int id;
    private String statut;  // "validée" ou "rejetée"
    private LocalDate dateValidation;  // La date de validation ou de rejet
    private Demande demande;  // La demande à laquelle la réponse est associée

    // Constructeur avec tous les champs nécessaires
    public ReponseDemande(int id, String statut, LocalDate dateValidation, Demande demande) {
        this.id = id;
        this.statut = statut;
        this.dateValidation = dateValidation;
        this.demande = demande;
    }

    // Constructeur sans ID
    public ReponseDemande(String statut, LocalDate dateValidation, Demande demande) {
        this.statut = statut;
        this.dateValidation = dateValidation;
        this.demande = demande;
    }

    // Getters et Setters
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatut() {
        return this.statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDate getDateValidation() {
        return this.dateValidation;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }

    public Demande getDemande() {
        return this.demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", statut='" + statut + '\'' +
                ", dateValidation=" + dateValidation +
                ", demandeId=" + (demande != null ? demande.getId() : "null") +
                '}';
    }
}