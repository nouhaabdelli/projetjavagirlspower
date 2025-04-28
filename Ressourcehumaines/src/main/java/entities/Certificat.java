package entities;

import java.sql.Date;

public class Certificat {
    private int idCertificat;
    private String titre;
    private String description;
    private Date dateDelivrance;
    private Date dateExpiration;
    private String niveau;
    private String codeCertificat;
    private int validiteAnnee;
    private boolean renouvelable;
    private String statut;
    private int id; // id utilisateur
    private Date createdAt;
    private Date updatedAt;

    // Constructeur vide
    public Certificat() {}

    // Constructeur complet
    public Certificat(int idCertificat, String titre, String description, Date dateDelivrance,
                      Date dateExpiration, String niveau, String codeCertificat,
                      int validiteAnnee, boolean renouvelable, String statut,
                      int id, Date createdAt, Date updatedAt) {
        this.idCertificat = idCertificat;
        this.titre = titre;
        this.description = description;
        this.dateDelivrance = dateDelivrance;
        this.dateExpiration = dateExpiration;
        this.niveau = niveau;
        this.codeCertificat = codeCertificat;
        this.validiteAnnee = validiteAnnee;
        this.renouvelable = renouvelable;
        this.statut = statut;
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters et Setters

    public int getIdCertificat() {
        return idCertificat;
    }

    public void setIdCertificat(int idCertificat) {
        this.idCertificat = idCertificat;
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

    public Date getDateDelivrance() {
        return dateDelivrance;
    }

    public void setDateDelivrance(Date dateDelivrance) {
        this.dateDelivrance = dateDelivrance;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getCodeCertificat() {
        return codeCertificat;
    }

    public void setCodeCertificat(String codeCertificat) {
        this.codeCertificat = codeCertificat;
    }

    public int getValiditeAnnee() {
        return validiteAnnee;
    }

    public void setValiditeAnnee(int validiteAnnee) {
        this.validiteAnnee = validiteAnnee;
    }

    public boolean isRenouvelable() {
        return renouvelable;
    }

    public void setRenouvelable(boolean renouvelable) {
        this.renouvelable = renouvelable;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
