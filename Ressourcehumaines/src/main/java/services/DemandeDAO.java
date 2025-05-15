package services;

import entities.Demande;
import entities.Conge;
import entities.Attestation;
import utils.MyConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class DemandeDAO {
    private Connection connection;

    public DemandeDAO() {
        this.connection = MyConnection.getInstance().getConnection(); // ✅ Correct
    }

    // Ajouter une demande générale
    public void addDemande(Demande demande) throws SQLException {
        String sql = "INSERT INTO demande (date_soumission, statut, type, description, utilisateur_id, date_de_validation) VALUES (?, 'en attente', ?, ?, ?, null)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(demande.getDateSoumission()));
            stmt.setString(2, demande.getType());
            stmt.setString(3, demande.getDescription());
            stmt.setInt(4, demande.getUtilisateurId());
            stmt.executeUpdate();
        }
    }

    public void modifierDemande(Demande demande) throws SQLException {
        String type = demande.getType();

        switch (type) {
            case "conge":
                Conge conge = (Conge) demande;
                String sqlConge = "UPDATE demande SET description = ?, date_debut = ?, date_fin = ?, motif = ?, type_conge = ? WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlConge)) {
                    stmt.setString(1, conge.getDescription());
                    stmt.setDate(2, Date.valueOf(conge.getDateDebut()));
                    stmt.setDate(3, Date.valueOf(conge.getDateFin()));
                    stmt.setString(4, conge.getMotif());
                    stmt.setString(5, conge.getTypeConge());
                    stmt.setInt(6, conge.getId());
                    stmt.executeUpdate();
                }
                break;

            case "attestation":
                Attestation attestation = (Attestation) demande;
                String sqlAttestation = "UPDATE demande SET description = ?, motif = ?, type_attestation = ? WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sqlAttestation)) {
                    stmt.setString(1, attestation.getDescription());
                    stmt.setString(2, attestation.getMotif());
                    stmt.setString(3, attestation.getTypeAttestation());
                    stmt.setInt(4, attestation.getId());
                    stmt.executeUpdate();
                }
                break;

            default:
                String sql = "UPDATE demande SET description = ? WHERE id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, demande.getDescription());
                    stmt.setInt(2, demande.getId());
                    stmt.executeUpdate();
                }
        }
    }

    // Ajouter une demande de congé (Conge)
    public void addConge(Conge conge) throws SQLException {
        String sql = "INSERT INTO demande (date_soumission, statut, type, description, utilisateur_id, date_de_validation, date_debut, date_fin, motif, type_conge) VALUES (?, 'en attente', 'conge', ?, ?, null, ?, ?, ?, ?)";
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

    // Ajouter une demande d'attestation (Attestation)
    public void addAttestation(Attestation attestation) throws SQLException {
        String sql = "INSERT INTO demande (date_soumission, statut, type, description, utilisateur_id, date_de_validation, type_attestation) VALUES (?, 'en attente', 'attestation', ?, ?, null, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(attestation.getDateSoumission()));
            stmt.setString(2, attestation.getDescription());
            stmt.setInt(3, attestation.getUtilisateurId());
            stmt.setString(4, attestation.getTypeAttestation());
            stmt.executeUpdate();
        }
    }

    // Valider une demande
    public void validerDemande(int demandeId) throws SQLException {
        String sql = "UPDATE demande SET statut = 'validée', date_de_validation = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now())); // Met la date actuelle comme date de validation
            stmt.setInt(2, demandeId);
            stmt.executeUpdate();
        }
    }

    // Récupérer une demande par ID
    public Demande getDemandeById(int id) throws SQLException {
        String sql = "SELECT * FROM demande WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("type");

                    switch (type) {
                        case "conge":
                            Conge conge = new Conge(
                                    rs.getInt("id"),
                                    rs.getDate("date_soumission").toLocalDate(),
                                    rs.getString("statut"),
                                    type,
                                    rs.getString("description"),
                                    rs.getInt("utilisateur_id"),
                                    rs.getDate("date_debut").toLocalDate(),
                                    rs.getDate("date_fin").toLocalDate(),
                                    rs.getString("motif"),
                                    rs.getString("type_conge")
                            );
                            return conge;

                        case "attestation":
                            Attestation attestation = new Attestation(
                                    rs.getInt("id"),
                                    rs.getDate("date_soumission").toLocalDate(),
                                    rs.getString("statut"),
                                    type,
                                    rs.getString("description"),
                                    rs.getInt("utilisateur_id"),
                                    rs.getDate("date_de_validation") != null ? rs.getDate("date_de_validation").toLocalDate() : null,
                                    rs.getString("motif"),
                                    rs.getString("type_attestation")
                            );
                            return attestation;

                        default:
                            Demande demande = new Demande(
                                    rs.getInt("id"),
                                    rs.getDate("date_soumission").toLocalDate(),
                                    rs.getString("statut"),
                                    type,
                                    rs.getString("description"),
                                    rs.getInt("utilisateur_id")
                            );
                            demande.setDateValidation(rs.getDate("date_de_validation") != null ? rs.getDate("date_de_validation").toLocalDate() : null);
                            return demande;
                    }
                }
            }
        }
        return null; // Return null if no result is found
    }

    // Récupérer les demandes par utilisateur ID
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
                            Conge conge = new Conge(
                                    rs.getInt("id"),
                                    rs.getDate("date_soumission").toLocalDate(),
                                    rs.getString("statut"),
                                    rs.getString("type"),
                                    rs.getString("description"),
                                    rs.getInt("utilisateur_id"),
                                    rs.getDate("date_debut").toLocalDate(),
                                    rs.getDate("date_fin").toLocalDate(),
                                    rs.getString("motif"),
                                    rs.getString("type_conge")
                            );
                            demandes.add(conge);
                            break;

                        case "attestation":
                            Attestation attestation = new Attestation(
                                    rs.getInt("id"),
                                    rs.getDate("date_soumission").toLocalDate(),
                                    rs.getString("statut"),
                                    type,
                                    rs.getString("description"),
                                    rs.getInt("utilisateur_id"),
                                    rs.getDate("date_de_validation") != null ? rs.getDate("date_de_validation").toLocalDate() : null,
                                    rs.getString("motif"),
                                    rs.getString("type_attestation")
                            );
                            attestation.setDateValidation(rs.getDate("date_de_validation") != null ? rs.getDate("date_de_validation").toLocalDate() : null);
                            demandes.add(attestation);
                            break;

                        default:
                            Demande demande = new Demande(
                                    rs.getInt("id"),
                                    rs.getDate("date_soumission").toLocalDate(),
                                    rs.getString("statut"),
                                    type,
                                    rs.getString("description"),
                                    rs.getInt("utilisateur_id")
                            );
                            demande.setDateValidation(rs.getDate("date_de_validation") != null ? rs.getDate("date_de_validation").toLocalDate() : null);
                            demandes.add(demande);
                    }
                }
            }
        }
        return demandes;
    }

    // Mettre à jour le statut d'une demande
    // Mettre à jour le statut d'une demande
    public void updateDemandeStatut(Demande demande) {
        String query = "UPDATE demande SET statut = ?, date_validation = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {  // Use the existing 'connection'
            statement.setString(1, demande.getStatut());
            statement.setDate(2, java.sql.Date.valueOf(demande.getDateValidation()));  // Assurez-vous que getDateValidation() renvoie un LocalDate
            statement.setInt(3, demande.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Traite l'exception comme nécessaire (log ou gestion de l'erreur)
        }
    }

    // Récupérer toutes les demandes avec statut "En attente"
    public List<Demande> getAllDemandesEnAttente() throws SQLException {
        List<Demande> demandes = new ArrayList<>();
        String query = "SELECT * FROM demande WHERE statut = 'En attente'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Demande demande = new Demande(
                        resultSet.getInt("id"),
                        resultSet.getString("statut"),
                        resultSet.getString("type"),
                        resultSet.getString("description"),
                        resultSet.getDate("date_soumission").toLocalDate(),
                        resultSet.getDate("date_validation") != null ? resultSet.getDate("date_validation").toLocalDate() : null
                );
                demandes.add(demande);
            }
        }
        return demandes;
    }
}
