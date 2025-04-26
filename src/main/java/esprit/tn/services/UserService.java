package esprit.tn.services;

import esprit.tn.interfaces.iServices;
import esprit.tn.models.User;
import esprit.tn.util.MaConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements iServices<User> {
    private final Connection conn;

    public UserService() {
        this.conn = MaConnexion.getInstance().getConn();
    }

    @Override
    public void add(User user) {
        String sql = "INSERT INTO user (nom, prenom, age, isActive) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getPrenom());
            stmt.setInt(3, user.getAge());

            // Assuming getter is isActive()
            stmt.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
        }
    }

    @Override
    public void remove(User user) {
        String sql = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getId()); // Assuming User has an id field
            stmt.executeUpdate();
            System.out.println("User removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error removing user: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id")); // Assuming id field exists
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setAge(rs.getInt("age"));

                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving users: " + e.getMessage());
        }
        return users;
    }

    @Override
    public List<User> getOne() {
        // This method seems misnamed; assuming it retrieves a single user by some criteria
        // For now, returning an empty list to match the interface
        System.err.println("getOne method not implemented.");
        return new ArrayList<>();
    }

    // Alternative: If getOne should retrieve a single user by ID
    public User getOneById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setAge(rs.getInt("age"));

                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void edit(User user) {
        String sql = "UPDATE user SET nom = ?, prenom = ?, age = ?WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getPrenom());
            stmt.setInt(3, user.getAge());

            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
            System.out.println("User updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
    }
}