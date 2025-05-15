package entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Certificat {

    private int codeCertificat;
    private String titre;
    private String description;
    private String dateExpiration;
    private String niveau;
    private int validiteAnnees;
    private String username;
    private int formationid; // Added formationid
    private Integer userId; // Assuming it's a foreign key (adjust as needed)
    private String createdAt;


    // Constructor with formationid
    public Certificat(int codeCertificat, String titre, String description, String dateExpiration, String niveau,
                      int validiteAnnees, String username, Integer userId, int formationid, String createdAt) {
        this.codeCertificat = codeCertificat;
        this.titre = titre;
        this.description = description;
        this.dateExpiration = dateExpiration;
        this.niveau = niveau;
        this.validiteAnnees = validiteAnnees;
        this.username = username;
        this.userId = userId;
        this.formationid = formationid; // Initialize formationid
        this.createdAt = createdAt;
    }

    // Constructor without codeCertificat
    public Certificat(String titre, String description, String dateExpiration, String niveau, int validiteAnnees,
                      String username, int userId, int formationid) {
        this.titre = titre;
        this.description = description;
        this.dateExpiration = dateExpiration;
        this.niveau = niveau;
        this.validiteAnnees = validiteAnnees;
        this.username = username;
        this.userId = userId;
        this.formationid = formationid; // Initialize formationid
    }

    public Certificat() {

    }

    // Getters and Setters
    public int getCodeCertificat() {
        return codeCertificat;
    }

    public void setCodeCertificat(int codeCertificat) {
        this.codeCertificat = codeCertificat;
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

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public int getValiditeAnnees() {
        return validiteAnnees;
    }

    public void setValiditeAnnees(int validiteAnnees) {
        this.validiteAnnees = validiteAnnees;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getFormationid() {
        return formationid;
    }

    public void setFormationid(int formationid) {
        this.formationid = formationid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Certificat{" +
                "codeCertificat=" + codeCertificat +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", dateExpiration='" + dateExpiration + '\'' +
                ", niveau='" + niveau + '\'' +
                ", validiteAnnees=" + validiteAnnees +
                ", username='" + username + '\'' +
                ", formationid=" + formationid +
                ", userId=" + userId +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
