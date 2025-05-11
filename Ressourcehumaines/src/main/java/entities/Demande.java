
package entities;

import java.time.LocalDate;

public class Demande {
    private int id;
    private LocalDate dateSoumission;
    private String statut = "en attente";
    private String type;
    private String description;
    private int utilisateurId;
    private LocalDate dateValidation;
    private String tempsPrediction;

    public Demande(int id, LocalDate dateSoumission, String statut, String type, String description, int utilisateurId) {
        this.id = id;
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.type = type;
        this.description = description;
        this.utilisateurId = utilisateurId;
        this.dateValidation = null;
    }

    public Demande(int id, LocalDate dateSoumission, String statut, String type, String description, int utilisateurId, LocalDate dateValidation) {
        this.id = id;
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.type = type;
        this.description = description;
        this.utilisateurId = utilisateurId;
        this.dateValidation = dateValidation;
    }

    public Demande(LocalDate dateSoumission, String statut, String type, String description, int utilisateurId, LocalDate dateValidation) {
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.type = type;
        this.description = description;
        this.utilisateurId = utilisateurId;
        this.dateValidation = dateValidation;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateSoumission() {
        return this.dateSoumission;
    }

    public void setDateSoumission(LocalDate dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public String getStatut() {
        return this.statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getTempsPrediction() {
        return this.tempsPrediction;
    }

    public void setTempsPrediction(String tempsPrediction) {
        this.tempsPrediction = tempsPrediction;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUtilisateurId() {
        return this.utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public LocalDate getDateValidation() {
        return this.dateValidation;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }
}
