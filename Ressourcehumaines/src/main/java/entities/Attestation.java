package entities;

import java.time.LocalDate;

public class Attestation extends Demande {
    private String motif;
    private String typeAttestation;
    private String objet;
    private String fichier;

    public Attestation(int id, LocalDate dateSoumission, String statut, String type, String description, int utilisateurId, LocalDate dateValidation, String motif, String typeAttestation) {
        super(id, dateSoumission, statut, type, description, utilisateurId, dateValidation);
        this.motif = motif;
        this.typeAttestation = typeAttestation;
    }

    public Attestation(LocalDate dateSoumission, String statut, String type, String description, int utilisateurId, LocalDate dateDebut, LocalDate dateFin, String motif, String typeConge, LocalDate dateValidation) {
        super(dateSoumission, statut, type, description, utilisateurId, dateValidation);
        this.typeAttestation = type;
        this.motif = motif;
    }

    public String getMotif() {
        return this.motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getTypeAttestation() {
        return this.typeAttestation;
    }

    public void setTypeAttestation(String typeAttestation) {
        this.typeAttestation = typeAttestation;
    }

    public String getObjet() {
        return this.objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getFichier() {
        return this.fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public String toString() {
        String var10000 = this.motif;
        return "Attestation{motif='" + var10000 + "', typeAttestation='" + this.typeAttestation + "'} " + super.toString();
    }
}
