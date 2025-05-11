package main;

import entities.Demande;
import entities.Utilisateur;
import services.DemandeDAO;
import services.UtilisateurService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TestJDBC {

    public static void main(String[] args) {
        try {
            // Connexion à la base de données
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/workbridge", "root", "");

            // Services
            UtilisateurService utilisateurService = new UtilisateurService(connection);
            DemandeDAO demandeService = new DemandeDAO(connection);

            // 1. Récupérer l'utilisateur existant
            Utilisateur utilisateur = utilisateurService.getUtilisateurByEmail("johndoe@example.com");

            if (utilisateur != null) {
                // 2. Créer une demande pour cet utilisateur avec dateValidation
                Demande demande = new Demande(
                        LocalDate.now(),               // dateSoumission
                        "en cours",                    // statut
                        "congé",                       // type
                        "Demande de congé pour vacances", // description
                        utilisateur.getId(),           // utilisateurId
                        LocalDate.now()                // dateValidation (actuellement la date du jour)
                );
                demandeService.addDemande(demande);

                // 3. Récupérer les demandes de cet utilisateur
                List<Demande> demandes = demandeService.getDemandeByUtilisateurId(utilisateur.getId());

                // 4. Afficher les descriptions
                for (Demande d : demandes) {
                    System.out.println("Description : " + d.getDescription());
                    System.out.println("Date de soumission : " + d.getDateSoumission());
                    System.out.println("Date de validation : " + d.getDateValidation());  // Affichage de la date de validation
                }
            } else {
                System.out.println("Utilisateur non trouvé.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}