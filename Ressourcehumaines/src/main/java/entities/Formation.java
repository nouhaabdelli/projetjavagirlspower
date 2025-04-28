package entities;

import java.sql.Date;

public class Formation {
    private int idFormation;
    private String titre;
    private String description;
    private String domaine;
    private String lieu;
    private Date dateDebut;
    private Date dateFin;

    // Constructeur vide
    public Formation() {}

    // Constructeur complet
    public Formation(int idFormation, String titre, String description, String domaine,
                     String lieu, Date dateDebut, Date dateFin) {
        this.idFormation = idFormation;
        this.titre = titre;
        this.description = description;
        this.domaine = domaine;
        this.lieu = lieu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public int getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(int idFormation) {
        this.idFormation = idFormation;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
}
