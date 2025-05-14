package services;

import entities.pret;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class pretservice {
    private Connection conn;

    public pretservice() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<pret> readAll() throws SQLException {
        List<pret> pretList = new ArrayList<>();
        String query = "SELECT r.idavance, r.montant, r.dateAvance AS datePret, r.niveauUrgence, r.etat, r.reponse, r.id_user, u.Prenom AS prenomUser " +
                "FROM reponsepretrh r " +
                "LEFT JOIN user u ON r.id_user = u.Id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                pret p = new pret(
                        rs.getInt("idavance"),
                        rs.getBigDecimal("montant"),
                        0, // duree is not in the query, default to 0
                        rs.getDate("datePret").toLocalDate(),
                        rs.getString("niveauUrgence"),
                        rs.getString("etat"),
                        rs.getString("reponse"),
                        rs.getInt("id_user"),
                        rs.getString("prenomUser")
                );
                pretList.add(p);
            }
        }
        return pretList;
    }

    public void update(pret pret) throws SQLException {
        String query = "UPDATE reponsepretrh SET montant = ?, dateAvance = ?, niveauUrgence = ?, etat = ?, reponse = ?, id_user = ? WHERE idavance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setBigDecimal(1, pret.getMontant());
            pstmt.setDate(2, java.sql.Date.valueOf(pret.getDatePret()));
            pstmt.setString(3, pret.getNiveauUrgence());
            pstmt.setString(4, pret.getEtat());
            pstmt.setString(5, pret.getReponse());
            pstmt.setInt(6, pret.getId_user());
            pstmt.setInt(7, pret.getIdavance());
            pstmt.executeUpdate();
        }
    }

    public void delete(pret pret) throws SQLException {
        String query = "DELETE FROM reponsepretrh WHERE idavance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, pret.getIdavance());
            pstmt.executeUpdate();
        }
    }

    public void create(pret pret) throws SQLException {
        String query = "INSERT INTO reponsepretrh (idavance, montant, dateAvance, niveauUrgence, etat, id_user) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, pret.getIdavance()); // idavance sera auto-incrémenté si 0
            pstmt.setBigDecimal(2, pret.getMontant());
            pstmt.setDate(3, java.sql.Date.valueOf(pret.getDatePret()));
            pstmt.setString(4, pret.getNiveauUrgence());
            pstmt.setString(5, pret.getEtat());
            pstmt.setInt(6, pret.getId_user());
            pstmt.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    pret.setIdavance(generatedKeys.getInt(1));
                }
            }
        }
    }
}