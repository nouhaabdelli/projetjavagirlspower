package services;

import entities.avance;
import utils.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class avanceservice implements IService<avance> {

    private Connection cnx;

    public avanceservice() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(avance a) throws SQLException {
        String query = "INSERT INTO avance (montant, duree, dateAvance, niveauUrgence, etat) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setBigDecimal(1, a.getMontant());
        ps.setInt(2, a.getDuree());
        ps.setDate(3, Date.valueOf(a.getDateAvance()));
        ps.setString(4, a.getNiveauUrgence());
        ps.setString(5, a.getEtat());
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            a.setIdAvance(rs.getInt(1));
        }
    }

    @Override
    public void update(avance a) throws SQLException {
        String query = "UPDATE avance SET montant = ?, duree = ?, dateAvance = ?, niveauUrgence = ?, etat = ? WHERE idAvance = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setBigDecimal(1, a.getMontant());
        ps.setInt(2, a.getDuree());
        ps.setDate(3, Date.valueOf(a.getDateAvance()));
        ps.setString(4, a.getNiveauUrgence());
        ps.setString(5, a.getEtat());
        ps.setInt(6, a.getIdAvance());
        ps.executeUpdate();
        System.out.println("Tentative de mise à jour de l'avance avec ID = " + a.getIdAvance());
    }

    @Override
    public void delete(avance a) throws SQLException {
        String query = "DELETE FROM avance WHERE idAvance = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, a.getIdAvance());
        ps.executeUpdate();
    }

    @Override
    public List<avance> readAll() throws SQLException {
        List<avance> avances = new ArrayList<>();
        String query = "SELECT idAvance, montant, duree, dateAvance, niveauUrgence, etat FROM avance";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            avance a = new avance(
                    rs.getInt("idAvance"),
                    rs.getBigDecimal("montant"),
                    rs.getInt("duree"),
                    rs.getDate("dateAvance").toLocalDate(),
                    rs.getString("niveauUrgence"),
                    rs.getString("etat")
            );
            avances.add(a);
        }
        return avances;
    }
}