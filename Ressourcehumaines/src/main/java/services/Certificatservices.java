package services;

import entities.Certificat;
import entities.User;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Certificatservices {

    private Connection connexion;

    public Certificatservices() {
        connexion = MyConnection.getInstance().getConnection();
    }

    public void ajouterCertificat(Certificat certificat) throws SQLException {
        String sql = "INSERT INTO certificat (code_certificat, titre, description, date_expiration, niveau, validite_annees, username, formationid, user_id, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connexion.prepareStatement(sql);

            ps.setInt(1, certificat.getCodeCertificat());
            ps.setString(2, certificat.getTitre());
            ps.setString(3, certificat.getDescription());
            ps.setString(4, certificat.getDateExpiration());
            ps.setString(5, certificat.getNiveau());
            ps.setInt(6, certificat.getValiditeAnnees());
            ps.setString(7, certificat.getUsername());
            ps.setInt(8, certificat.getFormationid());
            if (certificat.getUserId() != null) {
                ps.setInt(9, certificat.getUserId());
            } else {
                ps.setNull(9, java.sql.Types.INTEGER);
            }
            ps.setString(10, certificat.getCreatedAt());

            ps.executeUpdate();
            System.out.println("✅ Certificat ajouté !");

    }

    public List<Certificat> afficherCertificats() throws SQLException {
        List<Certificat> certificats = new ArrayList<>();
        String sql = "SELECT * FROM certificat";

             Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Certificat c = new Certificat();
                c.setCodeCertificat(rs.getInt("code_certificat"));
                c.setTitre(rs.getString("titre"));
                c.setDescription(rs.getString("description"));
                c.setDateExpiration(rs.getString("date_expiration"));
                c.setNiveau(rs.getString("niveau"));
                c.setValiditeAnnees(rs.getInt("validite_annees"));
                c.setUsername(rs.getString("username"));
                c.setFormationid(rs.getInt("formationid"));
                c.setUserId(rs.getInt("user_id"));
                c.setCreatedAt(rs.getString("created_at"));
                certificats.add(c);
            }

        return certificats;
    }

    public void modifierCertificat(Certificat certif) throws SQLException {
        String sql = "UPDATE certificat SET titre = ?, description = ?, niveau = ?, validite_annees = ?, username = ?, formationid = ?, user_id = ?, date_expiration = ?, created_at = ? WHERE code_certificat = ?";

             PreparedStatement pstmt = connexion.prepareStatement(sql);

            pstmt.setString(1, certif.getTitre());
            pstmt.setString(2, certif.getDescription());
            pstmt.setString(3, certif.getNiveau());
            pstmt.setInt(4, certif.getValiditeAnnees());
            pstmt.setString(5, certif.getUsername());
            pstmt.setInt(6, certif.getFormationid());
            if (certif.getUserId() != null) {
                pstmt.setInt(7, certif.getUserId());
            } else {
                pstmt.setNull(7, java.sql.Types.INTEGER);
            }
            pstmt.setString(8, certif.getDateExpiration());
            pstmt.setString(9, certif.getCreatedAt());
            pstmt.setInt(10, certif.getCodeCertificat());

            pstmt.executeUpdate();
            System.out.println("✅ Certificat modifié !");

    }

    public void supprimerCertificat(int codeCertificat) throws SQLException {
        String sql = "DELETE FROM certificat WHERE code_certificat = ?";
             PreparedStatement pstmt = connexion.prepareStatement(sql);

            pstmt.setInt(1, codeCertificat);
            pstmt.executeUpdate();
            System.out.println("✅ Certificat supprimé !");

    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        String query = "SELECT id, nom,prenom FROM user";
        try (Statement stmt = connexion.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("nom") + " " + rs.getString("prenom");
                userList.add(new User(id, username));
                System.out.println(username);
            }
        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }
        return userList;
    }
}
