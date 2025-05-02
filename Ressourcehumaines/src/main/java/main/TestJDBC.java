package main;

import entities.Evenement;
import services.EvenementService;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class TestJDBC {

    public static void main(String[] args) {
        EvenementService evenementService = new EvenementService();


        Evenement evenement = new Evenement(
                "Concert de musique",
                "Un concert incroyable",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1).plusHours(3),
                "Salle de concert",
                "Organisateur XYZ",
                100,
                "prévu",
                "photo_concert.jpg"
        );

        try {

            evenementService.create(evenement);
            System.out.println("Événement ajouté avec succès");


            System.out.println(evenementService.readAll());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
