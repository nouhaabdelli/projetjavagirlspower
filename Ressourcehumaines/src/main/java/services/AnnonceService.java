package services;

import entities.Annonce;
import utils.MyConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AnnonceService implements IService<Annonce> {

    private Connection cnx;

    public AnnonceService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(Annonce annonce) throws SQLException {
        String query = "INSERT INTO annonce(titre, contenu, date_publication, piece_jointe) VALUES(?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, annonce.getTitre());
        ps.setString(2, annonce.getContenu());
        ps.setTimestamp(3, Timestamp.valueOf(annonce.getDatePublication()));  // Convert LocalDateTime to Timestamp
        ps.setString(4, annonce.getPieceJointe());
        ps.executeUpdate();
    }

    @Override
    public void update(Annonce annonce) throws SQLException {
        String query = "UPDATE annonce SET titre = ?, contenu = ?, date_publication = ?, piece_jointe = ? WHERE id_annonce = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, annonce.getTitre());
        ps.setString(2, annonce.getContenu());
        ps.setTimestamp(3, Timestamp.valueOf(annonce.getDatePublication()));  // Convert LocalDateTime to Timestamp
        ps.setString(4, annonce.getPieceJointe());
        ps.setInt(5, annonce.getIdAnnonce());
        ps.executeUpdate();
    }

    @Override
    public void delete(Annonce annonce) throws SQLException {
        String query = "DELETE FROM annonce WHERE id_annonce = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, annonce.getIdAnnonce());
        ps.executeUpdate();
    }

    @Override
    public List<Annonce> readAll() throws SQLException {
        List<Annonce> annonces = new ArrayList<>();
        String query = "SELECT * FROM annonce";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int idAnnonce = rs.getInt("id_annonce");
            String titre = rs.getString("titre");
            String contenu = rs.getString("contenu");
            Timestamp datePublicationTimestamp = rs.getTimestamp("date_publication");
            String pieceJointe = rs.getString("piece_jointe");

            // Convert Timestamp to LocalDateTime
            LocalDateTime datePublication = datePublicationTimestamp.toLocalDateTime();

            Annonce annonce = new Annonce(idAnnonce, titre, contenu, datePublication, pieceJointe);
            annonces.add(annonce);
        }

        return annonces;
    }
}
