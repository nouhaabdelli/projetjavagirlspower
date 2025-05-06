package services;

import entities.avance;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class avanceservice {

    // Replace with your actual database connection logic
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/your_database";
        String user = "your_username";
        String password = "your_password";
        return DriverManager.getConnection(url, user, password);
    }

    public List<avance> readAll() throws SQLException {
        List<avance> avances = new ArrayList<>();
        String query = "SELECT * FROM avance";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                avance avance = new avance();
                avance.setIdAvance(rs.getInt("id_avance"));
                avance.setMontant(rs.getBigDecimal("montant"));
                avance.setDuree(rs.getInt("duree"));
                avance.setDateAvance(rs.getDate("date_avance").toLocalDate());
                avance.setNiveauUrgence(rs.getString("niveau_urgence"));
                avance.setEtat(rs.getString("etat"));
                avances.add(avance);
            }
        }
        return avances;
    }

    public void update(avance avance) throws SQLException {
        String query = "UPDATE avance SET montant = ?, duree = ?, date_avance = ?, niveau_urgence = ?, etat = ? WHERE id_avance = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setBigDecimal(1, avance.getMontant());
            pstmt.setInt(2, avance.getDuree());
            pstmt.setDate(3, Date.valueOf(avance.getDateAvance()));
            pstmt.setString(4, avance.getNiveauUrgence());
            pstmt.setString(5, avance.getEtat());
            pstmt.setInt(6, avance.getIdAvance());

            pstmt.executeUpdate();
        }
    }

    public void delete(avance avance) throws SQLException {
        String query = "DELETE FROM avance WHERE id_avance = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, avance.getIdAvance());
            pstmt.executeUpdate();
        }
    }
}