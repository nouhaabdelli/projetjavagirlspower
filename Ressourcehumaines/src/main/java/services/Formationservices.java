package services;

import entities.Formation;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Formationservices {

    public void ajouterFormation(Formation formation) {
        String sql = "INSERT INTO formation (titre, description, domaine, lieu, date_debut, date_fin) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, formation.getTitre());
            pstmt.setString(2, formation.getDescription());
            pstmt.setString(3, formation.getDomaine());
            pstmt.setString(4, formation.getLieu());
            pstmt.setDate(5, formation.getDateDebut());
            pstmt.setDate(6, formation.getDateFin());

            pstmt.executeUpdate();
            System.out.println(" Formation ajoutée !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    public List<Formation> afficherFormations() {
        List<Formation> formations = new ArrayList<>();
        String sql = "SELECT * FROM formation";
        try (Connection conn = MyConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Formation f = new Formation();
                f.setIdFormation(rs.getInt("id_formation"));
                f.setTitre(rs.getString("titre"));
                f.setDescription(rs.getString("description"));
                f.setDomaine(rs.getString("domaine"));
                f.setLieu(rs.getString("lieu"));
                f.setDateDebut(rs.getDate("date_debut"));
                f.setDateFin(rs.getDate("date_fin"));
                formations.add(f);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affichage : " + e.getMessage());
        }
        return formations;
    }

    public void modifierFormation(Formation formation) {
        String sql = "UPDATE formation SET titre = ?, description = ?, domaine = ?, lieu = ?, date_debut = ?, date_fin = ? WHERE id_formation = ?";
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, formation.getTitre());
            pstmt.setString(2, formation.getDescription());
            pstmt.setString(3, formation.getDomaine());
            pstmt.setString(4, formation.getLieu());
            pstmt.setDate(5, formation.getDateDebut());
            pstmt.setDate(6, formation.getDateFin());
            pstmt.setInt(7, formation.getIdFormation());

            pstmt.executeUpdate();
            System.out.println(" Formation modifiée !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification : " + e.getMessage());
        }
    }

    public void supprimerFormation(int idFormation) {
        String sql = "DELETE FROM formation WHERE id_formation = ?";
        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFormation);
            pstmt.executeUpdate();
            System.out.println(" Formation supprimée !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }
}
