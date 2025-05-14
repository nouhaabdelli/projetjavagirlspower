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
        // Validate id_user exists in user table
        if (a.getId_user() > 0) {
            String checkQuery = "SELECT COUNT(*) FROM user WHERE Id = ?";
            try (PreparedStatement pstmtCheck = cnx.prepareStatement(checkQuery)) {
                pstmtCheck.setInt(1, a.getId_user());
                ResultSet rs = pstmtCheck.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new SQLException("L'ID utilisateur " + a.getId_user() + " n'existe pas dans la table user.");
                }
            }
        }

        String query = "INSERT INTO avance (montant, duree, dateAvance, niveauUrgence, etat, id_user) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setBigDecimal(1, a.getMontant());
        ps.setInt(2, a.getDuree());
        ps.setDate(3, Date.valueOf(a.getDateAvance()));
        ps.setString(4, a.getNiveauUrgence());
        ps.setString(5, a.getEtat());
        ps.setInt(6, a.getId_user());
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
        String query = "SELECT a.idAvance, a.montant, a.duree, a.dateAvance, a.niveauUrgence, a.etat, a.id_user, u.Prenom AS prenomUser " +
                "FROM avance a " +
                "LEFT JOIN user u ON a.id_user = u.Id";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            avance a = new avance(
                    rs.getInt("idAvance"),
                    rs.getBigDecimal("montant"),
                    rs.getInt("duree"),
                    rs.getDate("dateAvance").toLocalDate(),
                    rs.getString("niveauUrgence"),
                    rs.getString("etat"),
                    rs.getInt("id_user"),
                    rs.getString("prenomUser")
            );
            avances.add(a);
        }
        return avances;
    }
}