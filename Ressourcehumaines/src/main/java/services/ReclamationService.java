package services;

import entities.Reclamations;
import utils.MyConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public   class ReclamationService implements IService<Reclamations> {

    private Connection cnx;

    public ReclamationService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(Reclamations reclamation) throws SQLException {
        String query = "INSERT INTO reclamations(titre, description, date_creation, statut , cheminPieceJointe , priorite ,RecevoirNotifications ,userId ) " +
                "VALUES (?, ?, ?, ?,?,?,? ,?  )";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, reclamation.getTitre());
        ps.setString(2, reclamation.getDescription());
        ps.setDate(3, Date.valueOf(reclamation.getDateDemande())); // Attention conversion LocalDate -> SQL Date
        ps.setString(4, reclamation.getStatut());
        ps.setString(5, reclamation.getCheminPieceJointe());
        ps.setString(6, reclamation.getPriorite());
        ps.setString(7, reclamation.getRecevoirNotifications());
        ps.setInt(8, reclamation.getUserId());

        ps.executeUpdate();
    }

    @Override
    public void update(Reclamations reclamation) throws SQLException {
        String query = "UPDATE reclamations SET titre = ?, description = ?, date_creation = ?, statut = ? , cheminPieceJointe = ? , priorite = ? ,RecevoirNotifications=?  WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, reclamation.getTitre());
        ps.setString(2, reclamation.getDescription());
        ps.setDate(3, Date.valueOf(reclamation.getDateDemande()));
        ps.setString(4, reclamation.getStatut());
        ps.setString(5, reclamation.getCheminPieceJointe());
        ps.setString(6, reclamation.getPriorite());
        ps.setString(7, reclamation.getRecevoirNotifications());
        ps.setInt(8, reclamation.getId());



        ps.executeUpdate();
    }

    @Override
    public void delete(Reclamations reclamation) throws SQLException {
        String query = "DELETE FROM reclamations WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, reclamation.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Reclamations> readAll() throws SQLException {
        List<Reclamations> reclamations = new ArrayList<>();
        String query = "SELECT id, titre, description, date_creation, statut, cheminPieceJointe, priorite, RecevoirNotifications FROM reclamations";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            String titre = rs.getString("titre");
            String description = rs.getString("description");
            Date dateDemande = rs.getDate("date_creation");
            String status = rs.getString("statut");
            String cheminPieceJointe = rs.getString("cheminPieceJointe"); // tu avais mis null, ici on lit depuis la BDD
            String priorite = rs.getString("priorite");
            String recevoirNotifications = rs.getString("RecevoirNotifications");


            Reclamations reclamation = new Reclamations(
                    id,
                    titre,
                    description,
                    dateDemande.toLocalDate()  ,
                    status,
                    cheminPieceJointe,
                    priorite,
                    recevoirNotifications,
                   4
            );

            reclamations.add(reclamation);
        }

        return reclamations;
    }

    public void setStatutTraite(int id) throws SQLException {
        String sql = "UPDATE reclamations SET statut = 'traitée' WHERE id = ?";
      try (
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Reclamations get(Long id) {
        String sql = "SELECT * FROM reclamations WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Reclamations r = new Reclamations();
                r.setId(rs.getInt("id"));
                r.setTitre(rs.getString("titre")); // selon les colonnes de votre table
                r.setDescription(rs.getString("description"));
                r.setDateDemande(rs.getDate("date_creation").toLocalDate()); // ou .toLocalDateTime()
                // ajoutez les autres attributs nécessaires
                return r;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}

