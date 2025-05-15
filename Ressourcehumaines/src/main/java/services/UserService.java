package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import entities.User;
import utils.MyConnection;

public class UserService {
    private final Connection connection = MyConnection.getInstance().getConnection();

    public User getUserById(int id) {

        String sql = "SELECT Nom, Prenom, Email, cin ,NumTelephone,Genre,Adresse FROM user WHERE Id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        id,
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getString("Email"),
                        rs.getInt("cin"),
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





    public Map<String, Integer> getUserRoleCounts() {
        Map<String, Integer> roleCounts = new HashMap<>();
        String query = "SELECT role, COUNT(*) AS total FROM user GROUP BY role";

        try (Connection conn = MyConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String role = rs.getString("role");
                int count = rs.getInt("total");
                roleCounts.put(role, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return roleCounts;

    }






}
