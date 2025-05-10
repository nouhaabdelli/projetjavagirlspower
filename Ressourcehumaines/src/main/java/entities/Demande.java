package entities;

import java.time.LocalDate;

public class Demande {

    private int id;
    private LocalDate dateSoumission;
    private String statut = "en attente";
    private String type;
    private String description;
    private int utilisateurId;

    public Demande() {
    }

    // Constructeur avec tous les paramètres
    public Demande(int id, LocalDate dateSoumission, String statut, String type, String description, int utilisateurId) {
        this.id = id;
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.type = type;
        this.description = description;
        this.utilisateurId = utilisateurId;
    }

    // Constructeur sans id, utile pour les ajouts
    public Demande(LocalDate dateSoumission, String statut, String type, String description, int utilisateurId) {
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.type = type;
        this.description = description;
        this.utilisateurId = utilisateurId;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(LocalDate dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}