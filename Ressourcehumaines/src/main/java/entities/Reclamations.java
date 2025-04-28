package entities;
import java.io.File;
import java.time.LocalDate;

public class Reclamations {
        private int id; // Identifiant de la réclamation
        private String titre; // Titre de la réclamation
        private String description; // Description détaillée
        private LocalDate dateDemande; // Date d'envoi de la réclamation
        private String statut; // Statut de la réclamation (en attente, traité, etc.)
        private  String cheminPieceJointe ;

    // Constructeur
        public Reclamations(int id, String titre, String description, LocalDate dateDemande, String statut , String cheminPieceJointe) {
            this.id = id;
            this.titre = titre;
            this.description = description;
            this.dateDemande = dateDemande;
            this.statut = statut;
            this.cheminPieceJointe = cheminPieceJointe;
        }

        // Getters et setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getTitre() { return titre; }
        public void setTitre(String titre) { this.titre = titre; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public LocalDate getDateDemande() { return dateDemande; }
        public void setDateDemande(LocalDate dateDemande) { this.dateDemande = dateDemande; }

        public String getStatut() { return statut; }
        public void setStatut(String statut) { this.statut = statut; }
        public String getCheminPieceJointe() { return cheminPieceJointe; }
    public void setCheminPieceJointe(String cheminPieceJointe) {
            this.cheminPieceJointe = cheminPieceJointe;
    }

    }


