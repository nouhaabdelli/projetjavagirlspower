package main;

import entities.User;
import services.UserCrud;

import java.time.LocalDate;
import java.util.List;

public class UserJDBC {

    public static void main(String[] args) {
        UserCrud userCrud = new UserCrud();

        // 🔵 Créer un nouvel utilisateur
        User user = new User(
                0, // id sera ignoré lors de l'ajout car auto-incrémenté en base
                "Dupont",
                "Jean",
                LocalDate.of(1990, 5, 20),
                "password123",
                "jean.dupont@example.com",
                "1234567890",
                "admin",
                "TN59123456789123456789",
                2,
                "CNAM12345",
                LocalDate.of(2020, 1, 15),
                "profile.jpg",
                "actif",
                "123 rue de Paris",
                "Homme",
                "Célibataire" , 14517898
        );

        // ➡️ Ajouter un utilisateur
        userCrud.ajouterUser(user);

        // ➡️ Modifier un utilisateur (exemple en changeant le prénom et le nom)
        user.setId(1); // Attention : mets l'ID réel existant dans ta base si tu veux tester la modification
        user.setNom("Durand");
        user.setPrenom("Paul");
        userCrud.modifierUser(user);

        // ➡️ Afficher tous les utilisateurs
        List<User> users = userCrud.afficherUser();
        System.out.println("📋 Liste des utilisateurs :");
        for (User u : users) {
            System.out.println(u);
        }

        // ➡️ Supprimer un utilisateur
        userCrud.supprimerUser(1); // Remplacer 1 par l'ID réel que tu veux supprimer
    }
}
