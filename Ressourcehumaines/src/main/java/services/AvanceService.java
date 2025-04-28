package services;

import entities.Avance;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvanceService implements IService<Avance> {

    private Connection cnx;

    public AvanceService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(Avance avance) throws SQLException {
        String query = "INSERT INTO avance (montant, date_avance, niveau_urgence, motif) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setDouble(1, avance.getMontant());
            ps.setDate(2, Date.valueOf(avance.getDateAvance())); // Assuming dateAvance is a String in format "yyyy-mm-dd"
            ps.setString(3, avance.getNiveauUrgence());
            ps.setString(4, avance.getMotif());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Avance avance) throws SQLException {
        String query = "UPDATE avance SET montant = ?, date_avance = ?, niveau_urgence = ?, motif = ? WHERE id_avance = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setDouble(1, avance.getMontant());
            ps.setDate(2, Date.valueOf(avance.getDateAvance()));
            ps.setString(3, avance.getNiveauUrgence());
            ps.setString(4, avance.getMotif());
            ps.setInt(5, avance.getIdAvance());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Avance avance) throws SQLException {
        String query = "DELETE FROM avance WHERE id_avance = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, avance.getIdAvance());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Avance> readAll() throws SQLException {
        List<Avance> avances = new ArrayList<>();
        String query = "SELECT * FROM avance";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Avance a = new Avance(
                        rs.getInt("id_avance"),
                        rs.getDouble("montant"),
                        rs.getDate("date_avance").toString(),
                        rs.getString("niveau_urgence"),
                        rs.getString("motif")
                );
                avances.add(a);
            }
        }
        return avances;
    }
}
