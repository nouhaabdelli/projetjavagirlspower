package main;

import entities.pret;
import services.pretservice;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class main {

    public static void main(String[] args) {
        pretservice ps = new pretservice();

        // Créer un prêt
        pret p = new pret(
                0, // id (généré en base normalement)
                new BigDecimal("5000.00"), // montant
                12,                         // durée en mois
                LocalDate.now(),           // date du prêt
                "élevé",                   // niveau d'urgence
                "en cours"                 // état
        );

        try {
            ps.create(p); // Ajouter un prêt
            System.out.println("Liste des prêts :");
            System.out.println(ps.readAll()); // Lire et afficher tous les prêts
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
        }
    }
}