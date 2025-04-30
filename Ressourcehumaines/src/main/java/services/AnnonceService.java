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
        if (annonce.getTitre() == null || annonce.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être vide.");
        }

        // Validation du contenu
        if (annonce.getContenu() == null || annonce.getContenu().trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu ne peut pas être vide.");
        }

        // Validation de la date de publication
        if (annonce.getDatePublication().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date de publication doit être la date actuelle ou une date future.");
        }

        // Validation de la pièce jointe (si elle n'est pas null, vérifier qu'elle a un nom valide)
        if (annonce.getPieceJointe() != null && annonce.getPieceJointe().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du fichier de la pièce jointe ne peut pas être vide.");
        }


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
