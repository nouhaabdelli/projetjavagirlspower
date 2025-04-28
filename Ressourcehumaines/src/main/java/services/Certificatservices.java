package services;

import entities.Certificat;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Certificatservices { // Assurez-vous que le nom du fichier est CertificatServices.java

    public void ajouterCertificat(Certificat certificat) {
        String sql = "INSERT INTO certificat (titre, description, date_delivrance, date_expiration, niveau, code_certificat, validité_année, renouvelable, statut, id, created_at, update_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = MyConnection.getInstance().getCnx();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, certificat.getTitre());
            pstmt.setString(2, certificat.getDescription());
            pstmt.setDate(3, certificat.getDateDelivrance());
            pstmt.setDate(4, certificat.getDateExpiration());
            pstmt.setString(5, certificat.getNiveau());
            pstmt.setString(6, certificat.getCodeCertificat());
            pstmt.setInt(7, certificat.getValiditeAnnee());
            pstmt.setBoolean(8, certificat.isRenouvelable());
            pstmt.setString(9, certificat.getStatut());
            pstmt.setInt(10, certificat.getId()); // <-- attention
            pstmt.setDate(11, certificat.getCreatedAt());
            pstmt.setDate(12, certificat.getUpdatedAt());

            pstmt.executeUpdate();
            System.out.println("✅ Certificat ajouté !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    public List<Certificat> afficherCertificats() {
        List<Certificat> certificats = new ArrayList<>();
        String sql = "SELECT * FROM certificat";
        try (Connection conn = MyConnection.getInstance().getCnx();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Certificat c = new Certificat();
                c.setIdCertificat(rs.getInt("id_certificat"));
                c.setTitre(rs.getString("titre"));
                c.setDescription(rs.getString("description"));
                c.setDateDelivrance(rs.getDate("date_delivrance"));
                c.setDateExpiration(rs.getDate("date_expiration"));
                c.setNiveau(rs.getString("niveau"));
                c.setCodeCertificat(rs.getString("code_certificat"));
                c.setValiditeAnnee(rs.getInt("validité_année"));
                c.setRenouvelable(rs.getBoolean("renouvelable"));
                c.setStatut(rs.getString("statut"));
                c.setId(rs.getInt("id"));
                c.setCreatedAt(rs.getDate("created_at"));
                c.setUpdatedAt(rs.getDate("update_at"));
                certificats.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affichage : " + e.getMessage());
        }
        return certificats;
    }

    public void modifierCertificat(Certificat certificat) {
        String sql = "UPDATE certificat SET titre = ?, description = ? WHERE id_certificat = ?";
        try (Connection conn = MyConnection.getInstance().getCnx();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, certificat.getTitre());
            pstmt.setString(2, certificat.getDescription());
            pstmt.setInt(3, certificat.getIdCertificat());

            pstmt.executeUpdate();
            System.out.println("✅ Certificat modifié !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification : " + e.getMessage());
        }
    }

    public void supprimerCertificat(int idCertificat) {
        String sql = "DELETE FROM certificat WHERE id_certificat = ?";
        try (Connection conn = MyConnection.getInstance().getCnx();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCertificat);
            pstmt.executeUpdate();
            System.out.println("✅ Certificat supprimé !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }
}
