package services;

import entities.Demande;
import entities.Conge;
import java.util.List;
import entities.Attestation;
import java.sql.Connection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DemandeDAO {
    private Connection connection;

    public DemandeDAO(Connection connection) {
        this.connection = connection;
    }

    // ✅ Ajouter une demande générale
    public void addDemande(Demande demande) throws SQLException {
        String sql = "INSERT INTO demande (date_soumission, statut, type, description, utilisateur_id) VALUES (?, 'en attente', ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(demande.getDateSoumission()));
            stmt.setString(2, demande.getType());
            stmt.setString(3, demande.getDescription());
            stmt.setInt(4, demande.getUtilisateurId());
            stmt.executeUpdate();
        }
    }

    // ✅ Ajouter une demande de congé
    public void addConge(Conge conge) throws SQLException {
        String sql = "INSERT INTO demande (date_soumission, statut, type, description, utilisateur_id, date_debut, date_fin, motif, type_conge) " +
                "VALUES (?, 'en attente', 'conge', ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(conge.getDateSoumission()));
            stmt.setString(2, conge.getDescription());
            stmt.setInt(3, conge.getUtilisateurId());
            stmt.setDate(4, Date.valueOf(conge.getDateDebut()));
            stmt.setDate(5, Date.valueOf(conge.getDateFin()));
            stmt.setString(6, conge.getMotif());
            stmt.setString(7, conge.getTypeConge());
            stmt.executeUpdate();
        }
    }

    // ✅ Ajouter une demande d'attestation
    public void addAttestation(Attestation attestation) throws SQLException {
        String sql = "INSERT INTO demande (date_soumission, statut, type, description, utilisateur_id, type_attestation) " +
                "VALUES (?, 'en attente', 'attestation', ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(attestation.getDateSoumission()));
            stmt.setString(2, attestation.getDescription());
            stmt.setInt(3, attestation.getUtilisateurId());
            stmt.setString(4, attestation.getTypeAttestation());
            stmt.executeUpdate();
        }
    }

    // ✅ Récupérer une demande par ID
    public Demande getDemandeById(int id) throws SQLException {
        String sql = "SELECT * FROM demande WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("type");

                    switch (type) {
                        case "conge":
                            Conge conge = new Conge();
                            conge.setId(rs.getInt("id"));
                            conge.setDateSoumission(rs.getDate("date_soumission").toLocalDate());
                            conge.setStatut(rs.getString("statut"));
                            conge.setType(type);
                            conge.setDescription(rs.getString("description"));
                            conge.setUtilisateurId(rs.getInt("utilisateur_id"));
                            conge.setDateDebut(rs.getDate("date_debut").toLocalDate());
                            conge.setDateFin(rs.getDate("date_fin").toLocalDate());
                            conge.setMotif(rs.getString("motif"));
                            conge.setTypeConge(rs.getString("type_conge"));
                            return conge;

                        case "attestation":
                            Attestation att = new Attestation();
                            att.setId(rs.getInt("id"));
                            att.setDateSoumission(rs.getDate("date_soumission").toLocalDate());
                            att.setStatut(rs.getString("statut"));
                            att.setType(type);
                            att.setDescription(rs.getString("description"));
                            att.setUtilisateurId(rs.getInt("utilisateur_id"));
                            att.setTypeAttestation(rs.getString("type_attestation"));
                            return att;

                        default:
                            return new Demande(
                                    rs.getInt("id"),
                                    rs.getDate("date_soumission").toLocalDate(),
                                    rs.getString("statut"),
                                    rs.getString("type"),
                                    rs.getString("description"),
                                    rs.getInt("utilisateur_id")
                            );
                    }
                }
            }
        }
        return null;
    }

    // ✅ Récupérer les demandes d’un utilisateur
    public List<Demande> getDemandeByUtilisateurId(int utilisateurId) throws SQLException {
        List<Demande> demandes = new ArrayList<>();
        String sql = "SELECT * FROM demande WHERE utilisateur_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, utilisateurId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String type = rs.getString("type");

                    switch (type) {
                        case "conge":
                            Conge conge = new Conge();
                            conge.setId(rs.getInt("id"));
                            conge.setDateSoumission(rs.getDate("date_soumission").toLocalDate());
                            conge.setStatut(rs.getString("statut"));
                            conge.setType(type);
                            conge.setDescription(rs.getString("description"));
                            conge.setUtilisateurId(rs.getInt("utilisateur_id"));
                            conge.setDateDebut(rs.getDate("date_debut").toLocalDate());
                            conge.setDateFin(rs.getDate("date_fin").toLocalDate());
                            conge.setMotif(rs.getString("motif"));
                            conge.setTypeConge(rs.getString("type_conge"));
                            demandes.add(conge);
                            break;

                        case "attestation":
                            Attestation att = new Attestation();
                            att.setId(rs.getInt("id"));
                            att.setDateSoumission(rs.getDate("date_soumission").toLocalDate());
                            att.setStatut(rs.getString("statut"));
                            att.setType(type);
                            att.setDescription(rs.getString("description"));
                            att.setUtilisateurId(rs.getInt("utilisateur_id"));
                            att.setTypeAttestation(rs.getString("type_attestation"));
                            demandes.add(att);
                            break;

                        default:
                            Demande d = new Demande(
                                    rs.getInt("id"),
                                    rs.getDate("date_soumission").toLocalDate(),
                                    rs.getString("statut"),
                                    rs.getString("type"),
                                    rs.getString("description"),
                                    rs.getInt("utilisateur_id")
                            );
                            demandes.add(d);
                    }
                }
            }
        }
        return demandes;
    }

    // ✅ Valider une demande
    public void validerDemande(int demandeId) throws SQLException {
        String sql = "UPDATE demande SET statut = 'validée' WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, demandeId);
            stmt.executeUpdate();
        }
    }

    // ✅ Refuser une demande
    public void refuserDemande(int demandeId) throws SQLException {
        String sql = "UPDATE demande SET statut = 'refusée' WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, demandeId);
            stmt.executeUpdate();
        }
    }
}