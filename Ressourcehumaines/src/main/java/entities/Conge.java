package entities;
import java.security.PublicKey;
import java.time.LocalDate;

public class Conge extends Demande{
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private String typeConge;  // New attribute for type of leave

    // Constructor including the new attribute

    public Conge() {}
    public Conge(LocalDate dateSoumission, String statut, String type, String description, int utilisateurId, LocalDate dateDebut, LocalDate dateFin, String motif, String typeConge) {
        super(dateSoumission, statut, type, description, utilisateurId);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.typeConge = typeConge;
    }

    @Override
    public String toString() {
        return "Conge{" +
                "dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", motif='" + motif + '\'' +
                ", typeConge='" + typeConge + '\'' +
                '}';
    }

    public Conge(LocalDate dateSoumission, String statut, String type, String description, int utilisateurId, LocalDate dateDebut) {
        super(dateSoumission, statut, type, description, utilisateurId);
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(String typeConge) {
        this.typeConge = typeConge;
    }
}