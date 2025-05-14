package entities;

import java.time.LocalDate;

public class Reponses {
    private int id;
    private String contenu;
    private LocalDate dateReponse;
    private LocalDate dateModification;
    private String priorite;
    private String fichierJoint;
    private int userId;
    private int reclamationId;
    public Reponses() {

    }
    public Reponses( int id , String contenu, LocalDate dateReponse, LocalDate dateModification,
                     String priorite, String fichierJoint,int reclamationId) {
        this.id = id;
        this.contenu = contenu;
        this.dateReponse = dateReponse;
        this.dateModification = dateModification;
        this.priorite = priorite;
        this.fichierJoint = fichierJoint;
        this.reclamationId = reclamationId;
    }

    public Reponses( int id , String contenu, LocalDate dateReponse, LocalDate dateModification,
                    String priorite, String fichierJoint ) {
        this.id = id;
        this.contenu = contenu;
        this.dateReponse = dateReponse;
        this.dateModification = dateModification;
        this.priorite = priorite;
        this.fichierJoint = fichierJoint;
    }


    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDate getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(LocalDate dateReponse) {
        this.dateReponse = dateReponse;
    }

    public LocalDate getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
    }

    public String getPriorite() {
        return priorite;
    }
    public void setPriorite(String priorite) {
        this.priorite = priorite;
    }

    public String getFichierJoint() {
        return fichierJoint;
    }

    public void setFichierJoint(String fichierJoint) {
        this.fichierJoint = fichierJoint;
    }


    public int getUserId() {
        return userId;

    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getReclamationId() {
        return reclamationId;
    }
    public void setReclamationId(int reclamationId) {
        this.reclamationId = reclamationId;
    }

    @Override
    public String toString() {
        return "Reponses{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", dateReponse=" + dateReponse +
                ", dateModification=" + dateModification +
                ", importance='" + priorite + '\'' +
                ", fichierJoint='" + fichierJoint + '\'' +
                '}';
    }



}
