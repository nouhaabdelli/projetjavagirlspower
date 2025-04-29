package main;

import entities.Pret;
import services.PretService;  // CORRECT: majuscule à PretService
import utils.MyConnection;

import java.sql.SQLException;

public class TestJDBC {
    public static void main(String[] args) {
        // Connexion à la base via ton singleton
        PretService ps = new PretService(MyConnection.getInstance().getConnection());

        // Création d'un prêt
        Pret pret1 = new Pret(1, 5000.0, 12, "2025-04-29", "Élevé", "Mariage");

        // Ajouter le prêt et afficher tous les prêts
        try {
            // Ajouter le prêt
            ps.create(pret1);

            // Afficher tous les prêts
            System.out.println(ps.readAll());
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'opération avec la base de données : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
