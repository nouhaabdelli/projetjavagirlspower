package entities;
import java.time.LocalDate;

public class Reclamations {
        private int id; // Identifiant de la réclamation
        private String titre; // Titre de la réclamation
        private String description; // Description détaillée
        private LocalDate dateDemande; // Date d'envoi de la réclamation
        private String statut; // Statut de la réclamation (en attente, traité, etc.)

        // Constructeur
        public Reclamations(int id, String titre, String description, LocalDate dateDemande, String statut) {
            this.id = id;
            this.titre = titre;
            this.description = description;
            this.dateDemande = dateDemande;
            this.statut = statut;
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
    }


