package entities;

import java.time.LocalDate;

public class Reclamations {
        private int id; // Identifiant de la réclamation
        private String titre; // Titre de la réclamation
        private String description; // Description détaillée
        private LocalDate dateDemande; // Date d'envoi de la réclamation
        private String statut; // Statut de la réclamation (en attente, traité, etc.)
        private  String cheminPieceJointe ;
        private String priorite ;
        private String RecevoirNotifications ;
    private int  userId ;
    public Reclamations() {}
    public Reclamations(int id) {
        this.id = id;
    }

    public Reclamations(int id , String titre ,String description ,LocalDate dateDemande,String cheminPieceJointe , String priorite) {
        this.id = id;
    }
    // Constructeur
        public Reclamations(int id, String titre, String description, LocalDate dateDemande, String statut , String cheminPieceJointe , String priorite, String RecevoirNotifications, int userId) {
            this.id = id;
            this.titre = titre;
            this.description = description;
            this.dateDemande = dateDemande;
            this.statut = statut;
            this.cheminPieceJointe = cheminPieceJointe;
            this.priorite = priorite;
            this.RecevoirNotifications = RecevoirNotifications;
            this.userId = userId;

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

         public String getPriorite() { return priorite; }

         public void setPriorite(String priorite) { this.priorite = priorite; }

            public String getRecevoirNotifications() {
                 return RecevoirNotifications;
    }


         public void setRecevoirNotifications(String RecevoirNotifications) {
                      this.RecevoirNotifications = RecevoirNotifications;
         }
         public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "Reclamations{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", dateDemande=" + dateDemande +
                ", dateDemande=" + statut +

                ", priorite='" + priorite + '\'' +
                ", cheminPieceJointe='" + cheminPieceJointe + '\'' +
                '}';
    }


}


