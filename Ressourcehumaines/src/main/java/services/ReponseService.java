package services;
import entities.Reponses;
import entities.Reponses;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ReponseService {
    private Connection cnx;

    public ReponseService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    public void create(Reponses reponse) throws SQLException {
        String query = "INSERT INTO reponses (titre, contenu, date_reponse, importance, fichier_joint, reclamation_id) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, reponse.getTitre());
        ps.setString(2, reponse.getContenu());
        ps.setDate(3, Date.valueOf(reponse.getDateReponse())); // conversion LocalDate → SQL Date
        ps.setString(4, reponse.getImportance());
        ps.setString(5, reponse.getFichierJoint());
        ps.setDate(6, Date.valueOf(reponse.getDateReponse()));
        ps.executeUpdate();
    }

    public void update(Reponses reponse) throws SQLException {
        String query = "UPDATE reponses SET titre = ?, contenu = ?, date_reponse = ?, importance = ?, fichier_joint = ?, dateModification = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, reponse.getTitre());
        ps.setString(2, reponse.getContenu());
        ps.setDate(3, Date.valueOf(reponse.getDateReponse()));
        ps.setString(4, reponse.getImportance());
        ps.setString(5, reponse.getFichierJoint());
        ps.setDate(6, Date.valueOf(reponse.getDateReponse()));
        ps.setInt(7, reponse.getId());
        ps.executeUpdate();
    }

    public void delete(Reponses reponse) throws SQLException {
        String query = "DELETE FROM reponses WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, reponse.getId());
        ps.executeUpdate();
    }

    public List<Reponses> readAll() throws SQLException {
        List<Reponses> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponses";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Reponses reponse = new Reponses(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("contenu"),
                    rs.getDate("date_reponse").toLocalDate(),
                    rs.getDate("getDateModification()").toLocalDate(),
                    rs.getString("importance"),
                    rs.getString("fichier_joint"),
                    2
            );
            reponses.add(reponse);
        }

        return reponses;
    }
}
