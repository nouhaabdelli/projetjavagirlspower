package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MyConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/workbridge"; // ⚠️ changer ton_nom_base
    private static final String USER = "root";
    private static final String PASSWORD = ""; // en général vide avec XAMPP

    private static Connection connection;

    // Méthode pour obtenir la connexion
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion réussie !");
            } catch (SQLException e) {
                System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
            }
        }
        return connection;
    }
}