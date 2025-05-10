package entities;

import java.time.LocalDate;

public class Attestation extends Demande {
    private String typeAttestation;

    public Attestation() {}

    public Attestation(LocalDate dateSoumission, String statut, String type, String description, int utilisateurId, String typeAttestation) {
        super(dateSoumission, statut, type, description, utilisateurId);
        this.typeAttestation = typeAttestation;
    }

    public String getTypeAttestation() { return typeAttestation; }
    public void setTypeAttestation(String typeAttestation) { this.typeAttestation = typeAttestation; }

    @Override
    public String toString() {
        return "Attestation{" +
                "typeAttestation='" + typeAttestation + '\'' +
                "} " + super.toString();
    }
}