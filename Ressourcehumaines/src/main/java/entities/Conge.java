//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package entities;

import java.time.LocalDate;

public class Conge extends Demande {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private String typeConge;

    public Conge(LocalDate dateSoumission, String statut, String type, String description, int utilisateurId, LocalDate dateValidation, LocalDate dateDebut, LocalDate dateFin, String motif, String typeConge) {
        super(dateSoumission, statut, type, description, utilisateurId, dateValidation);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.typeConge = typeConge;
    }

    public Conge(int id, LocalDate dateSoumission, String statut, String type, String description, int utilisateurId, LocalDate dateValidation, LocalDate dateDebut, LocalDate dateFin, String motif, String typeConge) {
        super(id, dateSoumission, statut, type, description, utilisateurId, dateValidation);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.typeConge = typeConge;
    }

    public Conge(int id, LocalDate dateSoumission, String statut, String type, String description, int utilisateurId, LocalDate dateDebut, LocalDate dateFin, String motif, String typeConge) {
        super(id, dateSoumission, statut, type, description, utilisateurId);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.typeConge = typeConge;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotif() {
        return this.motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getTypeConge() {
        return this.typeConge;
    }

    public void setTypeConge(String typeConge) {
        this.typeConge = typeConge;
    }
}
