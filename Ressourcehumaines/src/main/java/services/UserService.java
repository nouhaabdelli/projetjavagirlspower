package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import utils.MyConnection;

public class UserService {

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
