package main;

import entities.Demande;
import entities.Utilisateur;
import services.DemandeDAO;
import services.UtilisateurService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TestJDBC {

    public static void main(String[] args) throws SQLException {
        // Connexion à la base de données
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");

        // Services
        UtilisateurService utilisateurService = new UtilisateurService(connection);
    }
}