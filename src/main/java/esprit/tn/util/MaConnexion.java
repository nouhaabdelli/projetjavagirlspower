package esprit.tn.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MaConnexion {
    // Informations de connexion
    private final String URL = "jdbc:mysql://localhost:3306/rhapp";
    private final String USER = "root";
    private final String PASSWORD = "";

    // Attribut pour stocker la connexion
    private static Connection conn;

    // Singleton : instance statique unique
    private static MaConnexion instance;

    // Constructeur privé (on empêche la création directe depuis l'extérieur)
    private MaConnexion() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion établie avec succès");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion");
            e.printStackTrace(); // Affiche l'erreur exacte
        }
    }

    // Méthode statique pour obtenir l'unique instance
    public static MaConnexion getInstance() {
        if (instance == null) {
            instance = new MaConnexion();
        }
        return instance;
    }

    // Méthode pour obtenir la connexion
    public  getConn() {
        return conn;
    }
}
