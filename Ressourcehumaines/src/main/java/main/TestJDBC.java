import entities.Certificat;
import entities.Formation;
import services.Certificatservices;
import services.Formationservices;

import java.sql.SQLException;

public class TestJDBC {

    public static void main(String[] args) {
        Certificatservices certificatService = new Certificatservices();
        Formationservices formationService = new Formationservices();

        // Création d'un certificat
        Certificat cert = new Certificat();
        cert.setTitre("Certification Java");
        cert.setDescription("Certification avancée en Java");
        cert.setDateDelivrance(java.sql.Date.valueOf("2025-05-01"));
        cert.setDateExpiration(java.sql.Date.valueOf("2030-05-01"));
        cert.setNiveau("Avancé");
        cert.setCodeCertificat("JAVA-ADV-2025");
        cert.setValiditeAnnee(5);
        cert.setRenouvelable(true);
        cert.setStatut("Actif");
        cert.setId(1);
        cert.setCreatedAt(new java.sql.Date(System.currentTimeMillis()));
        cert.setUpdatedAt(new java.sql.Date(System.currentTimeMillis()));

        // Création d'une formation
        Formation formation = new Formation();
        formation.setTitre("Formation Base de Données");
        formation.setDescription("Formation complète sur SQL et PL/SQL");
        formation.setDomaine("Informatique");
        formation.setLieu("Tunis");
        formation.setDateDebut(java.sql.Date.valueOf("2025-06-01"));
        formation.setDateFin(java.sql.Date.valueOf("2025-06-15"));

        try {
            // Ajouter un certificat
            certificatService.ajouterCertificat(cert);

            // Lire tous les certificats
            System.out.println("Liste des certificats :");
            System.out.println(certificatService.afficherCertificats());

            // Ajouter une formation
            formationService.ajouterFormation(formation);

            // Lire toutes les formations
            System.out.println("Liste des formations :");
            System.out.println(formationService.afficherFormations());

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
