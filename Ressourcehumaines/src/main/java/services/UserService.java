package services;

import entities.user;
import utils.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserService {
    private final Connection connection = MyConnection.getInstance().getConnection();

    public user getUserById(int id) {
        String sql = "SELECT Nom, Prenom, Email, cin ,NumTelephone,Genre,Adresse FROM user WHERE Id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new user(
                        id,
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getString("Email"),
                        rs.getString("cin"),
                        rs.getString("NumTelephone"),
                        rs.getString("Genre"),
                        rs.getString("Adresse")
                        );
            }
        } catch (Exception e) {
            e.printStackTrace();  // Affiche l'exception complète dans la console
            System.out.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return null;
    }
}
