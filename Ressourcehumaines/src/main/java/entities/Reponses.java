package entities;

import java.time.LocalDate;

public class Reponses {
    private int id;
    private String titre;
    private String contenu;
    private LocalDate dateReponse;
    private LocalDate dateModification;
    private String importance;
    private String fichierJoint;
    private int reclamationId;
    private int userId;

    // Constructeurs
    public Reponses() {}

    public Reponses(int id, String titre, String contenu, LocalDate dateReponse, LocalDate dateModification,
                    String importance, String fichierJoint, int reclamationId , int userId) {
        this.id = id;
        this.titre = titre;
        this.contenu = contenu;
        this.dateReponse = dateReponse;
        this.dateModification = dateModification;
        this.importance = importance;
        this.fichierJoint = fichierJoint;
        this.reclamationId = reclamationId;
        this.userId = userId;
    }

    public Reponses(String titre, String contenu, LocalDate dateReponse, String importance,
                    String fichierJoint, int reclamationId , int userId) {
        this.titre = titre;
        this.contenu = contenu;
        this.dateReponse = dateReponse;
        this.importance = importance;
        this.fichierJoint = fichierJoint;
        this.reclamationId = reclamationId;
        this.userId = userId;
    }

    public Reponses(int i, String titreTxt, String contenuTxt, LocalDate date, Object o, String importanceTxt, String fichier, int reclamationId) {
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
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

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getFichierJoint() {
        return fichierJoint;
    }

    public void setFichierJoint(String fichierJoint) {
        this.fichierJoint = fichierJoint;
    }

    public int getReclamationId() {
        return reclamationId;
    }

    public void setReclamationId(int reclamationId) {
        this.reclamationId = reclamationId;
    }
    public int getUserId() {
        return userId;

    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Reponses{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", contenu='" + contenu + '\'' +
                ", dateReponse=" + dateReponse +
                ", dateModification=" + dateModification +
                ", importance='" + importance + '\'' +
                ", fichierJoint='" + fichierJoint + '\'' +
                ", reclamationId=" + reclamationId +
                '}';
    }
}
