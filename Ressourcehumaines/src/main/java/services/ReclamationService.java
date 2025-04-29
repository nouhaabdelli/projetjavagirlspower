package services;

import entities.Reclamations;
import utils.MyConnection;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

public   class ReclamationService implements IService<Reclamations> {

    private Connection cnx;

    public ReclamationService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(Reclamations reclamation) throws SQLException {
        String query = "INSERT INTO reclamations(titre, description, date_creation, status , cheminPieceJointe , priorite ,RecevoirNotifications ) " +
                "VALUES (?, ?, ?, ?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, reclamation.getTitre());
        ps.setString(2, reclamation.getDescription());
        ps.setDate(3, Date.valueOf(reclamation.getDateDemande())); // Attention conversion LocalDate -> SQL Date
        ps.setString(4, reclamation.getStatut());
        ps.setString(7, reclamation.RecevoirNotifications());
        ps.setString(6, reclamation.getPriorite());
        ps.setString(5, reclamation.getCheminPieceJointe());
        ps.executeUpdate();
    }

    @Override
    public void update(Reclamations reclamation) throws SQLException {
        String query = "UPDATE reclamations SET titre = ?, description = ?, date_creation = ?, status = ? , cheminPieceJointe = ?   WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, reclamation.getTitre());
        ps.setString(2, reclamation.getDescription());
        ps.setDate(3, Date.valueOf(reclamation.getDateDemande()));
        ps.setString(4, reclamation.getStatut());
        ps.setString(5, reclamation.getCheminPieceJointe());
        ps.setInt(6, reclamation.getId());
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
        String query = "SELECT * FROM reclamations";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            String titre = rs.getString("titre");
            String description = rs.getString("description");
            Date dateCreation = rs.getDate("date_creation");
            String status = rs.getString("status");
            String cheminPieceJointe = null ;
            String priorite = rs.getString("priorite");
            String recevoirNotifications = rs.getString("RecevoirNotifications");

            Reclamations reclamation = new Reclamations(id, titre, description, dateCreation.toLocalDate(), status , cheminPieceJointe,priorite,recevoirNotifications);
            reclamations.add(reclamation);
        }

        return reclamations;
    }
}
