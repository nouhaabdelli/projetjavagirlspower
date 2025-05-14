package entities;

import java.time.LocalDate;

public class Formation {


    private Integer idFormation;

    private String titre;

    private String description;

    private String domaine;

    private String lieu;

    private String dateDebut;

    private String dateFin;


    // Getters and Setters

    public Formation(Integer idFormation, String titre, String description, String domaine, String lieu, String dateDebut, String dateFin) {
        this.idFormation = idFormation;
        this.titre = titre;
        this.description = description;
        this.domaine = domaine;
        this.lieu = lieu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Formation(String titre, String description, String domaine, String lieu, String dateDebut, String dateFin) {
        this.titre = titre;
        this.description = description;
        this.domaine = domaine;
        this.lieu = lieu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Integer getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(Integer idFormation) {
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

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "idFormation=" + idFormation +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", domaine='" + domaine + '\'' +
                ", lieu='" + lieu + '\'' +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                '}';
    }
}
