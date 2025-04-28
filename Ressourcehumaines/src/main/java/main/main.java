package main;
import java.sql.SQLException;

import entities.Reclamations;
import services.ReclamationService;
import java.time.LocalDate;

public class main {

    public static void main(String[] args) {
        ReclamationService rs = new ReclamationService();

        // Créer une réclamation
        Reclamations r = new Reclamations(0,"Problème de paie", "Je n'ai pas reçu mon salaire", LocalDate.now(), "En attente",null);

        try {
            rs.create(r); // Ajouter une réclamation
            System.out.println(rs.readAll()); // Lire toutes les réclamations et les afficher
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    }

