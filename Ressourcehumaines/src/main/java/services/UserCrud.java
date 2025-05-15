package services;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import entities.User;

public class UserCrud {
    private Connection connection;

    // Constructeur : connexion à la base
    public  UserCrud() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge","root","");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Ajouter un utilisateur
    public void ajouterUser(User user) {
        String sql = "INSERT INTO user (nom, prenom, dateNaissance, motDePasse, email, numTelephone, role, rib, nombreEnfant, cnam, dateEmbauche, photoProfil, statut, adresse, genre, situationFamiliale,cin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getNom());
            pstmt.setString(2, user.getPrenom());
            pstmt.setDate(3, Date.valueOf(user.getDateNaissance()));
            pstmt.setString(4, user.getMotDePasse());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getNumTelephone());
            pstmt.setString(7, user.getRole());
            pstmt.setString(8, user.getRib());
            pstmt.setInt(9, user.getNombreEnfant());
            pstmt.setString(10, user.getCnam());
            pstmt.setDate(11, Date.valueOf(user.getDateEmbauche()));
            pstmt.setString(12, user.getPhotoProfil());
            pstmt.setString(13, user.getStatut());
            pstmt.setString(14, user.getAdresse());
            pstmt.setString(15, user.getGenre());
            pstmt.setString(16, user.getSituationFamiliale());
            pstmt.setInt(17, user.getCin());

            pstmt.executeUpdate();
            System.out.println("✅ Utilisateur ajouté !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modifier un utilisateur
    public void modifierUser(User user) {
        String sql = "UPDATE user SET nom=?, prenom=?, dateNaissance=?, motDePasse=?, email=?, numTelephone=?, role=?, rib=?, nombreEnfant=?, cnam=?, dateEmbauche=?, photoProfil=?, statut=?, adresse=?, genre=?, situationFamiliale=? WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getNom());
            pstmt.setString(2, user.getPrenom());
            pstmt.setDate(3, Date.valueOf(user.getDateNaissance()));
            pstmt.setString(4, user.getMotDePasse());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getNumTelephone());
            pstmt.setString(7, user.getRole());
            pstmt.setString(8, user.getRib());
            pstmt.setInt(9, user.getNombreEnfant());
            pstmt.setString(10, user.getCnam());
            pstmt.setDate(11, Date.valueOf(user.getDateEmbauche()));
            pstmt.setString(12, user.getPhotoProfil());
            pstmt.setString(13, user.getStatut());
            pstmt.setString(14, user.getAdresse());
            pstmt.setString(15, user.getGenre());
            pstmt.setString(16, user.getSituationFamiliale());
            pstmt.setInt(17, user.getId());

            pstmt.executeUpdate();
            System.out.println("✅ Utilisateur modifié !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un utilisateur
    public void supprimerUser(int id) {
        String sql = "DELETE FROM user WHERE id=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("🗑️ Utilisateur supprimé !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Afficher tous les utilisateurs
    public List<User> afficherUser() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setDateNaissance(rs.getDate("dateNaissance").toLocalDate());
                user.setMotDePasse(rs.getString("motDePasse"));
                user.setEmail(rs.getString("email"));
                user.setNumTelephone(rs.getString("numTelephone"));
                user.setRole(rs.getString("role"));
                user.setRib(rs.getString("rib"));
                user.setNombreEnfant(rs.getInt("nombreEnfant"));
                user.setCnam(rs.getString("cnam"));
                user.setDateEmbauche(rs.getDate("dateEmbauche").toLocalDate());
                user.setPhotoProfil(rs.getString("photoProfil"));
                user.setStatut(rs.getString("statut"));
                user.setAdresse(rs.getString("adresse"));
                user.setGenre(rs.getString("genre"));
                user.setSituationFamiliale(rs.getString("situationFamiliale"));
                user.setCin(rs.getInt("cin"));

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
}


