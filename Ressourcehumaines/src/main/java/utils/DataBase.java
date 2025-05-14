package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private Connection connection;
    private static DataBase instance;
    private final String URL = "jdbc:mysql://localhost:3306/workbridge2";
    private final String USER = "root";
    private final String PASSWORD = "";

    private DataBase() {
        try {
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static DataBase getInstance() {
        if (instance == null) {
            instance = new DataBase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
