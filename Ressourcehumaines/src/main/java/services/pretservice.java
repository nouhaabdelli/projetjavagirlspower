package services;
import entities.pret;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class pretservice implements IService<pret>{

    private Connection cnx;

    public pretservice() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(pret p) throws SQLException {
        String query = "INSERT INTO pret ( montant, duree, datePret, niveauUrgence, etat , idPret) VALUES (?, ?, ?, ?, ? ,? )";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setBigDecimal(1, p.getMontant());
        ps.setInt(2, p.getDuree());
        ps.setDate(3, Date.valueOf(p.getDatePret()));
        ps.setString(4, p.getNiveauUrgence());
        ps.setString(5, p.getEtat());
        ps.setInt(6, p.getIdPret()); // Accès direct puisque getUserId() n'existe pas
        ps.executeUpdate();
    }

    @Override
    public void update(pret p) throws SQLException {
        String query = "UPDATE pret SET montant = ?, duree = ?, datePret = ?, niveauUrgence = ?, etat = ? WHERE idPret = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setBigDecimal(1, p.getMontant());
        ps.setInt(2, p.getDuree());
        ps.setDate(3, Date.valueOf(p.getDatePret()));
        ps.setString(4, p.getNiveauUrgence());
        ps.setString(5, p.getEtat());
        ps.setInt(6, p.getIdPret());
        ps.executeUpdate();
       ps.executeUpdate();
        System.out.println("Tentative de mise à jour du prêt avec ID = " + p.getIdPret());

    }

    @Override
    public void delete(pret p) throws SQLException {
        String query = "DELETE FROM pret WHERE idPret = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, p.getIdPret());
        ps.executeUpdate();
    }

    @Override
    public List<pret> readAll() throws SQLException {
        List<pret> prets = new ArrayList<>();
        String query = "SELECT  montant, duree, datePret, niveauUrgence, etat FROM pret";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            pret p = new pret(
                    0,
                    rs.getBigDecimal("montant"),
                    rs.getInt("duree"),
                    rs.getDate("datePret").toLocalDate(),
                    rs.getString("niveauUrgence"),
                    rs.getString("etat")
            );
//            p.setUserId(rs.getInt("userId")); // ajout séparé
            prets.add(p);
        }

        return prets;
    }
}
