package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton Design Pattern pour la connexion à la base de données
public class MyConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/workbridge2";
    private static final String USER = "root";
    private static final String PASS = "";

    private Connection connection;
    private static MyConnection instance;

    // Constructeur privé pour empêcher l'instanciation externe
    private MyConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("✅ Connection to database established successfully.");
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to database: " + e.getMessage());
        }
    }

    // Méthode statique pour obtenir l'instance unique (Singleton)
    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    // Getter pour la connexion
    public Connection getConnection() {
        return connection;
    }
}
