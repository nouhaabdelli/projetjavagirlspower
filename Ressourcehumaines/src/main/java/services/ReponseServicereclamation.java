package services;
import entities.Reponses;
import utils.MyConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class ReponseServicereclamation {
    private Connection cnx;

    public ReponseServicereclamation() {
        cnx = MyConnection.getInstance().getConnection();
    }

    public void create(Reponses reponse) throws SQLException {
        String query = "INSERT INTO reponses ( contenu, date_reponse , date_modification , importance, fichier_joint ,reclamationId ) VALUES (?, ?,?, ?, ?,?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, reponse.getContenu());
        if (reponse.getDateReponse() != null) {
            ps.setDate(2, Date.valueOf(reponse.getDateReponse())); // Conversion de LocalDate -> SQL Date
        } else {
            ps.setNull(2, java.sql.Types.DATE);
        }
        if (reponse.getDateModification() != null) {
            ps.setDate(3, Date.valueOf(reponse.getDateModification())); // Conversion de LocalDate -> SQL Date
        } else {
            ps.setNull(3, java.sql.Types.DATE);
        }

        ps.setString(4, reponse.getPriorite());
        ps.setString(5, reponse.getFichierJoint());
        ps.setInt(6, reponse.getReclamationId());

        ps.executeUpdate();
    }

    public void update(Reponses reponse) throws SQLException {
        String query = "UPDATE reponses SET  contenu = ?, date_reponse = ?, importance = ?, fichier_joint = ?, date_modification  = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, reponse.getContenu());
        ps.setDate(2, Date.valueOf(reponse.getDateReponse()));
        ps.setString(3, reponse.getPriorite());
        ps.setString(4, reponse.getFichierJoint());
        ps.setDate(5, Date.valueOf(reponse.getDateModification()));
        ps.setInt(6, reponse.getId());
        ps.executeUpdate();
    }

    public void delete(Reponses reponse) throws SQLException {
        String query = "DELETE FROM reponses WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, reponse.getId());

        // Afficher l'ID avant d'exécuter la suppression pour s'assurer que l'ID est correct
        System.out.println("Suppression de la réponse avec ID: " + reponse.getId());

        ps.executeUpdate();
    }

    public List<Reponses> readAll() throws SQLException {
        List<Reponses> reponses = new ArrayList<>();
        String query = "SELECT * FROM reponses";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int  reclamationId =  rs.getInt("ReclamationId");
            int id = rs.getInt("id");
            String contenu = rs.getString("contenu");
            String priorite =  rs.getString("importance");
            String  fichierJoint =   rs.getString("fichier_joint");
            LocalDate dateReponse = null;
            Date sqlDateReponse = rs.getDate("date_reponse");
            if (sqlDateReponse != null) {
                dateReponse = sqlDateReponse.toLocalDate();
            }

            LocalDate dateModification = null;
            Date sqlDateModification = rs.getDate("date_modification");
            if (sqlDateModification != null) {
                dateModification = sqlDateModification.toLocalDate();

            }

            Reponses reponse = new Reponses(
                    id,
                    contenu,
                    dateReponse,
                    dateModification,
                    priorite ,
                    fichierJoint ,
                    reclamationId
                    );

            reponses.add(reponse);
        }

        return reponses;
}
    public Reponses getByReclamationId(int idReclamation) throws SQLException {
        String sql = "SELECT * FROM reponses WHERE reclamationId = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, idReclamation);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Reponses(
                    rs.getInt("id"),
                    rs.getString("contenu"),
                    rs.getDate("date_reponse").toLocalDate(),
                    rs.getDate("date_modification") != null ? rs.getDate("date_modification").toLocalDate() : null,
                    rs.getString("importance"),
                    rs.getString("fichier_joint")
            );
        }
        return null;
    }

}
