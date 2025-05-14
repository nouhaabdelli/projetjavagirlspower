package entities;

import java.time.LocalDateTime;

public class Evenement {

    private int idEvenement;
    private String nomEvenement;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String lieu;
    private String organisateur;
    private int participantsMax;
    private String statut;  // Peut être "prévu", "annulé", "terminé", etc.
    private String photo;  // Peut être null

    public Evenement(String nomEvenement, String description, LocalDateTime dateDebut, LocalDateTime dateFin, String lieu, String organisateur, int participantsMax, String statut, String photo) {
        this.nomEvenement = nomEvenement;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.organisateur = organisateur;
        this.participantsMax = participantsMax;
        this.statut = statut;
        this.photo = photo;
    }

    public Evenement(int idEvenement, String nomEvenement, String description, LocalDateTime dateDebut, LocalDateTime dateFin, String lieu, String organisateur, int participantsMax, String statut, String photo) {
        this.idEvenement = idEvenement;
        this.nomEvenement = nomEvenement;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.organisateur = organisateur;
        this.participantsMax = participantsMax;
        this.statut = statut;
        this.photo = photo;
    }

    public int getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(int idEvenement) {
        this.idEvenement = idEvenement;
    }

    public String getNomEvenement() {
        return nomEvenement;
    }

    public void setNomEvenement(String nomEvenement) {
        this.nomEvenement = nomEvenement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getOrganisateur() {
        return organisateur;
    }

    public void setOrganisateur(String organisateur) {
        this.organisateur = organisateur;
    }

    public int getParticipantsMax() {
        return participantsMax;
    }

    public void setParticipantsMax(int participantsMax) {
        this.participantsMax = participantsMax;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "idEvenement=" + idEvenement +
                ", nomEvenement='" + nomEvenement + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", lieu='" + lieu + '\'' +
                ", organisateur='" + organisateur + '\'' +
                ", participantsMax=" + participantsMax +
                ", statut='" + statut + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}

