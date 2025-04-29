package services;
import entities.Pret;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PretService implements IService<Pret> {

    private Connection cnx;

    public PretService(Connection connection) {
        this.cnx = connection;
    }

    @Override
    public void create(Pret pret) throws SQLException {
        String sql = "INSERT INTO pret (idPret, montant, duree, datePret, niveauUrgence, motif) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = cnx.prepareStatement(sql);
            ps.setInt(1, pret.getIdPret());
            ps.setDouble(2, pret.getMontant());
            ps.setInt(3, pret.getDuree());
            ps.setString(4, pret.getDatePret());
            ps.setString(5, pret.getNiveauUrgence());
            ps.setString(6, pret.getMotif());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du prêt : " + e.getMessage());
            throw e;
        } finally {
            if (ps != null) {
                ps.close(); // Fermeture du PreparedStatement
            }
        }
    }

    @Override
    public void update(Pret pret) throws SQLException {
        String sql = "UPDATE pret SET montant = ?, duree = ?, datePret = ?, niveauUrgence = ?, motif = ? WHERE idPret = ?";
        PreparedStatement ps = null;
        try {
            ps = cnx.prepareStatement(sql);
            ps.setDouble(1, pret.getMontant());
            ps.setInt(2, pret.getDuree());
            ps.setString(3, pret.getDatePret());
            ps.setString(4, pret.getNiveauUrgence());
            ps.setString(5, pret.getMotif());
            ps.setInt(6, pret.getIdPret());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du prêt : " + e.getMessage());
            throw e;
        } finally {
            if (ps != null) {
                ps.close(); // Fermeture du PreparedStatement
            }
        }
    }

    @Override
    public void delete(Pret pret) throws SQLException {
        String sql = "DELETE FROM pret WHERE idPret = ?";
        PreparedStatement ps = null;
        try {
            ps = cnx.prepareStatement(sql);
            ps.setInt(1, pret.getIdPret());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du prêt : " + e.getMessage());
            throw e;
        } finally {
            if (ps != null) {
                ps.close(); // Fermeture du PreparedStatement
            }
        }
    }

    @Override
    public List<Pret> readAll() throws SQLException {
        List<Pret> prets = new ArrayList<>();
        String sql = "SELECT * FROM pret";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = cnx.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Pret p = new Pret();
                p.setIdPret(rs.getInt("idPret"));
                p.setMontant(rs.getDouble("montant"));
                p.setDuree(rs.getInt("duree"));
                p.setDatePret(rs.getString("datePret"));
                p.setNiveauUrgence(rs.getString("niveauUrgence"));
                p.setMotif(rs.getString("motif"));
                prets.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la lecture des prêts : " + e.getMessage());
            throw e;
        } finally {
            if (rs != null) {
                rs.close(); // Fermeture du ResultSet
            }
            if (stmt != null) {
                stmt.close(); // Fermeture du Statement
            }
        }
        return prets;
    }
}
