package esprit.tn;
import esprit.tn.models.User;
import java.sql.*;
import java.sql.Connection;
import esprit.tn.util.MaConnexion;
public class Main {
    public static void main(String[] args) {

        // Connexion à la base de données
        MaConnexion maConnexion = new MaConnexion();
        Connection Connection = MaConnexion.getConn();

    }
}