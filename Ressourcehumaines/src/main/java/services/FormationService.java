package services;

import entities.Formation;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormationService {
    private Connection connexion;

    public FormationService() {
        connexion = MyConnection.getInstance().getInstance().getConnection();
    }

    public void ajouterFormation(Formation f) throws SQLException {
        String req = "INSERT INTO formation (titre, description, domaine, lieu, date_debut, date_fin) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = connexion.prepareStatement(req);
        ps.setString(1, f.getTitre());
        ps.setString(2, f.getDescription());
        ps.setString(3, f.getDomaine());
        ps.setString(4, f.getLieu());
        ps.setString(5, f.getDateDebut());
        ps.setString(6, f.getDateFin());
        ps.executeUpdate();
    }

    public List<Formation> afficherFormations() throws SQLException {
        List<Formation> formations = new ArrayList<>();
        String req = "SELECT * FROM formation";
        Statement stm = connexion.createStatement();
        ResultSet rs = stm.executeQuery(req);

        while (rs.next()) {
            Formation f = new Formation(
                    rs.getInt("idFormation"),
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getString("domaine"),
                    rs.getString("lieu"),
                    rs.getString("date_debut"),
                    rs.getString("date_fin")
            );
            formations.add(f);
        }
        return formations;
    }

    public void supprimerFormation(int idFormation) throws SQLException {
        String req = "DELETE FROM formation WHERE idformation = ?";
        PreparedStatement ps = connexion.prepareStatement(req);
        ps.setInt(1, idFormation);
        ps.executeUpdate();
    }

    public void modifierFormation( Formation f) throws SQLException {
        String req = "UPDATE formation SET titre=?, description=?, domaine=?, lieu=?, date_debut=?, date_fin=? WHERE idformation=?";
        PreparedStatement ps = connexion.prepareStatement(req);
        ps.setString(1, f.getTitre());
        ps.setString(2, f.getDescription());
        ps.setString(3, f.getDomaine());
        ps.setString(4, f.getLieu());
        ps.setString(5, f.getDateDebut());
        ps.setString(6, f.getDateFin());
        ps.setInt(7, f.getIdFormation());
        ps.executeUpdate();
    }
}
