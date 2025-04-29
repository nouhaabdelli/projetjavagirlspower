package main;
import java.sql.SQLException;

import entities.pret;
import services.pretservice;
import java.time.LocalDate;
public class main {
    public static void main(String[] args) {
        pretservice ps = new pretservice();

        // Créer une réclamation
        pret p = new pret(0,"700", "12", LocalDate.now(), "moyenne",null,"normal","mail" ,4);

        try {
            ps.create(p); // Ajouter une réclamation
            System.out.println(ps.readAll()); // Lire toutes les réclamations et les afficher
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
