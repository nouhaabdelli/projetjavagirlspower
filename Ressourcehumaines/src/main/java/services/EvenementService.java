package services;

import entities.Evenement;
import utils.MyConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EvenementService {

    private static Connection cnx;

    public EvenementService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    public void create(Evenement evenement) throws SQLException {

        validerEvenement(evenement);

        String query = "INSERT INTO evenement(nom_evenement, description, date_debut, date_fin, lieu, organisateur, participants_max, statut, photo) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, evenement.getNomEvenement());
        ps.setString(2, evenement.getDescription());
        ps.setTimestamp(3, Timestamp.valueOf(evenement.getDateDebut()));
        ps.setTimestamp(4, Timestamp.valueOf(evenement.getDateFin()));
        ps.setString(5, evenement.getLieu());
        ps.setString(6, evenement.getOrganisateur());
        ps.setInt(7, evenement.getParticipantsMax());
        ps.setString(8, evenement.getStatut());
        ps.setString(9, evenement.getPhoto());
        ps.executeUpdate();
    }

    public void update(Evenement evenement) throws SQLException {

        validerEvenement(evenement);

        String query = "UPDATE evenement SET nom_evenement = ?, description = ?, date_debut = ?, date_fin = ?, lieu = ?, organisateur = ?, participants_max = ?, statut = ?, photo = ? WHERE id_evenement = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, evenement.getNomEvenement());
        ps.setString(2, evenement.getDescription());
        ps.setTimestamp(3, Timestamp.valueOf(evenement.getDateDebut()));
        ps.setTimestamp(4, Timestamp.valueOf(evenement.getDateFin()));
        ps.setString(5, evenement.getLieu());
        ps.setString(6, evenement.getOrganisateur());
        ps.setInt(7, evenement.getParticipantsMax());
        ps.setString(8, evenement.getStatut());
        ps.setString(9, evenement.getPhoto());
        ps.setInt(10, evenement.getIdEvenement());
        ps.executeUpdate();
    }

    public static void delete(Evenement evenement) throws SQLException {
        String query = "DELETE FROM evenement WHERE id_evenement = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, evenement.getIdEvenement());
        ps.executeUpdate();
    }

    public List<Evenement> readAll() throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM evenement";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int idEvenement = rs.getInt("id_evenement");
            String nomEvenement = rs.getString("nom_evenement");
            String description = rs.getString("description");
            Timestamp dateDebutTimestamp = rs.getTimestamp("date_debut");
            Timestamp dateFinTimestamp = rs.getTimestamp("date_fin");
            String lieu = rs.getString("lieu");
            String organisateur = rs.getString("organisateur");
            int participantsMax = rs.getInt("participants_max");
            String statut = rs.getString("statut");
            String photo = rs.getString("photo");

            LocalDateTime dateDebut = dateDebutTimestamp.toLocalDateTime();
            LocalDateTime dateFin = dateFinTimestamp.toLocalDateTime();

            Evenement evenement = new Evenement(idEvenement, nomEvenement, description, dateDebut, dateFin, lieu, organisateur, participantsMax, statut, photo);
            evenements.add(evenement);
        }

        return evenements;
    }


    private void validerEvenement(Evenement evenement) {

        if (evenement.getDateDebut().isAfter(evenement.getDateFin())) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin.");
        }


        if (evenement.getDateDebut().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date de début ne doit pas être avant la date d'aujourd'hui.");
        }


        if (evenement.getParticipantsMax() <= 0) {
            throw new IllegalArgumentException("Le nombre maximal de participants doit être un nombre positif.");
        }
    }

    public void updateParticope(Evenement ev) throws SQLException {
        String req = "UPDATE evenement SET participants_max = ? WHERE id = ?";
        PreparedStatement pst = cnx.prepareStatement(req);
        pst.setInt(1, ev.getParticipantsMax());
        pst.setInt(2, ev.getIdEvenement());
        pst.executeUpdate();
    }

}
