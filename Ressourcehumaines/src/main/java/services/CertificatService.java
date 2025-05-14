package services;

import entities.Certificat;
import entities.User;
import utils.DBConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CertificatService {
    private Connection connexion;

    public CertificatService() {
        connexion = DBConnexion.getInstance().getCnx();
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        String query = "SELECT id, nom,prenom FROM user";
        try (Statement stmt = connexion.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("nom")+" "+rs.getString("prenom");
                userList.add(new User(id, username));
                System.out.println(username);
            }
        } catch (Exception e) {
            System.err.println("Error fetching users: " + e.getMessage());
        }

        return userList;
    }
    public void ajouterCertificat(Certificat c) throws SQLException {
        String req = "INSERT INTO certificat (titre, description, date_expiration, niveau, validite_annees, code_certificat, user_id,username, created_at,formationid) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?,?, NOW(),?)";
        PreparedStatement ps = connexion.prepareStatement(req);
        ps.setString(1, c.getTitre());
        ps.setString(2, c.getDescription());
        ps.setString(3, c.getDateExpiration());
        ps.setString(4, c.getNiveau());
        ps.setInt(5, c.getValiditeAnnees());
        ps.setInt(6, c.getCodeCertificat());
        ps.setObject(7, c.getUserId());
        ps.setObject(8, c.getUsername());
        ps.setObject(9, c.getFormationid());
        ps.executeUpdate();
    }

    public List<Certificat> afficherCertificats() throws SQLException {
        List<Certificat> certificats = new ArrayList<>();
        String req = "SELECT * FROM certificat";
        Statement stm = connexion.createStatement();
        ResultSet rs = stm.executeQuery(req);

        while (rs.next()) {
            Certificat c = new Certificat(
                    rs.getInt("code_certificat"),
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getString("date_expiration"),
                    rs.getString("niveau"),
                    rs.getInt("validite_annees"),
                    rs.getString("username"),
                    // username not in DB
                    rs.getInt("user_id"),
                    rs.getInt("formationid"),
                    rs.getString("created_at")
            );
            certificats.add(c);
        }
        return certificats;
    }
    public List<Certificat> afficherCertificats(int id) throws SQLException {
        List<Certificat> certificats = new ArrayList<>();
            String req = "SELECT * FROM certificat where formationid='"+id+"'";
        Statement stm = connexion.createStatement();
        ResultSet rs = stm.executeQuery(req);

        while (rs.next()) {
            Certificat c = new Certificat(
                    rs.getInt("code_certificat"),
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getString("date_expiration"),
                    rs.getString("niveau"),
                    rs.getInt("validite_annees"),
                    rs.getString("username"),

                    // username not in DB
                    rs.getInt("user_id"),
                    rs.getInt("formationid"),
                    rs.getString("created_at")
                );
            certificats.add(c);
        }
        return certificats;
    }

    public void supprimerCertificat(int codeCertificat) throws SQLException {
        String req = "DELETE FROM certificat WHERE code_certificat = ?";
        PreparedStatement ps = connexion.prepareStatement(req);
        ps.setInt(1, codeCertificat);
        ps.executeUpdate();
    }

    public void modifierCertificat(Certificat c) throws SQLException {
        String req = "UPDATE certificat SET titre=?, description=?, date_expiration=?, niveau=?, validite_annees=? WHERE code_certificat=?";
        PreparedStatement ps = connexion.prepareStatement(req);
        ps.setString(1, c.getTitre());
        ps.setString(2, c.getDescription());
        ps.setString(3, c.getDateExpiration());
        ps.setString(4, c.getNiveau());
        ps.setInt(5, c.getValiditeAnnees());
        ps.setInt(6, c.getCodeCertificat());
        ps.executeUpdate();
    }
}
