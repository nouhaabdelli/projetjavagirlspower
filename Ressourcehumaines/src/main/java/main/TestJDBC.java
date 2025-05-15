package main;

import entities.Certificat;
import services.Certificatservices;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestJDBC {

    public static void main(String[] args) {
        Certificatservices certificatService = new Certificatservices();

        // Format de date compatible avec la base de données (ex: 2025-05-15)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Création d’un certificat
        Certificat certificat = new Certificat(
                "Java Avancé",
                "Formation avancée en Java et frameworks",
                LocalDate.now().plusYears(1).format(formatter),  // dateExpiration
                "Avancé",
                2,
                "amine.gh",                     // username (exemple)
                1,                              // userId (id d’un utilisateur existant)
                10                              // formationid (id d’une formation existante)
        );

        certificat.setCreatedAt(LocalDate.now().format(formatter));

        try {
            certificatService.ajouterCertificat(certificat);
            System.out.println("✅ Certificat ajouté avec succès\n");

            System.out.println("📄 Liste des certificats :");
            certificatService.afficherCertificats().forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("❌ Erreur : " + e.getMessage());
        }
    }
}
