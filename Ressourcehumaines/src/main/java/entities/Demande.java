
package entities;

import java.time.LocalDate;

public class Demande {

    private LocalDate dateSoumission;
    private String statut = "en attente";
    private String type;
    private String description;
    private int iddemande;
    private int Id;
    private LocalDate dateValidation;
    private String tempsPrediction;

    public Demande(int iddemande, LocalDate dateSoumission, String statut, String type, String description, int Id) {
        this.iddemande = iddemande;
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.type = type;
        this.description = description;
        this.Id  =Id;
        this.dateValidation = null;
    }

    public Demande(int iddemande, LocalDate dateSoumission, String statut, String type, String description, int Id, LocalDate dateValidation) {
        this.iddemande = iddemande;
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.type = type;
        this.description = description;
        this.Id = Id;
        this.dateValidation = dateValidation;
    }
    public Demande(int iddemande, String statut, String type, String description,LocalDate dateSoumission , LocalDate dateValidation) {
        this.iddemande = iddemande;
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.type = type;
        this.description = description;
        this.dateValidation = dateValidation;

    }
    public Demande(LocalDate dateSoumission, String statut, String type, String description, int utilisateurId, LocalDate dateValidation) {
        this.dateSoumission = dateSoumission;
        this.statut = statut;
        this.type = type;
        this.description = description;
        this.Id = Id;
        this.dateValidation = dateValidation;
    }

    public int getIddemande() {
        return this.iddemande;
    }

    public void setIddemande(int id) {
        this.iddemande = id;
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

    public int getId() {
        return this.Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public LocalDate getDateValidation() {
        return this.dateValidation;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }
}

