package main;

import entities.Annonce;
import services.AnnonceService;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class TestJDBC {

    public static void main(String[] args) {
        AnnonceService annonceService = new AnnonceService();
        Annonce annonce = new Annonce("Titre de l'annonce", "Contenu de l'annonce", LocalDateTime.now(), "piece_jointe.pdf");

        try {
            // Pour insérer une nouvelle annonce dans la base de données
            annonceService.create(annonce);
            System.out.println("Annonce ajoutée avec succès");

            // Lire toutes les annonces
            System.out.println(annonceService.readAll());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
