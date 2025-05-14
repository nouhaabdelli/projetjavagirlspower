package services;

import entities.Notifications;
import entities.Reclamations;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Notificationservice {
    private Connection cnx;

    public Notificationservice() {
        cnx = MyConnection.getInstance().getConnection();
    }

    private final ReclamationService reclamationService = new ReclamationService();

    public void createReclamationNotification(String message, Long reclamationId) throws SQLException {
        String sql = "INSERT INTO notifications (message, created_at, seen, reclamation_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, message);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setBoolean(3, false);
            stmt.setLong(4, reclamationId);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    System.out.println("Notification created with ID: " + generatedKeys.getLong(1));
                }
            }
        }
    }

    public List<Notifications> fetchReclamationNotifications() throws SQLException {
        String sql = "SELECT * FROM notifications WHERE message = 'Nouvelle réclamation ajoutée' ORDER BY created_at DESC";
        List<Notifications> notifications = new ArrayList<>();

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notifications notif = new Notifications();
                    notif.setId(rs.getInt("id"));
                    notif.setMessage(rs.getString("message"));
                    notif.setCreatedAt(rs.getTimestamp("created_at"));
                    notif.setSeen(rs.getBoolean("seen"));

                    notif.setReclamationId(rs.getInt("reclamation_id"));

                    notifications.add(notif);
                }
            }
        }
        return notifications;
    }

    public void markAsSeen(Long notificationId) throws SQLException {
        String sql = "UPDATE notifications SET seen = TRUE WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setLong(1, notificationId);
            stmt.executeUpdate();
        }
    }

    public int countUnseenReclamationNotifications() throws SQLException {
        String sql = "SELECT COUNT(*) FROM notifications WHERE seen = FALSE AND message = 'Nouvelle réclamation ajoutée'";
        try (PreparedStatement stmt = cnx.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
