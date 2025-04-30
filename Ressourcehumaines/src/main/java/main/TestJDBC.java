package main;

import entities.Evenement;
import services.EvenementService;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class TestJDBC {

    public static void main(String[] args) {
        EvenementService evenementService = new EvenementService();

        // Création d'un événement avec des valeurs d'exemple
        Evenement evenement = new Evenement(
                "Concert de musique", // nomEvenement
                "Un concert incroyable", // description
                LocalDateTime.now().plusDays(1), // dateDebut (demain)
                LocalDateTime.now().plusDays(1).plusHours(3), // dateFin (après 3 heures)
                "Salle de concert", // lieu
                "Organisateur XYZ", // organisateur
                100, // participantsMax
                "prévu", // statut
                "photo_concert.jpg" // photo
        );

        try {
            // Pour insérer un nouvel événement dans la base de données
            evenementService.create(evenement);
            System.out.println("Événement ajouté avec succès");

            // Lire tous les événements
            System.out.println(evenementService.readAll());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
