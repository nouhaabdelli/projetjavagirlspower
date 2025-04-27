package entities;
import java.time.LocalDate;

public class reponse {
        private int id; // Identifiant de la réponse
        private int reclamationId; // ID de la réclamation à laquelle la réponse est associée
        private String reponseText; // Texte de la réponse
        private LocalDate dateReponse; // Date à laquelle la réponse a été donnée
        private String statutReponse; // Statut de la réponse (acceptée, refusée, etc.)

        // Constructeur
        public reponse(int id, int reclamationId, String reponseText, LocalDate dateReponse, String statutReponse) {
            this.id = id;
            this.reclamationId = reclamationId;
            this.reponseText = reponseText;
            this.dateReponse = dateReponse;
            this.statutReponse = statutReponse;
        }

        // Getters et setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getReclamationId() { return reclamationId; }
        public void setReclamationId(int reclamationId) { this.reclamationId = reclamationId; }

        public String getReponseText() { return reponseText; }
        public void setReponseText(String reponseText) { this.reponseText = reponseText; }

        public LocalDate getDateReponse() { return dateReponse; }
        public void setDateReponse(LocalDate dateReponse) { this.dateReponse = dateReponse; }

        public String getStatutReponse() { return statutReponse; }
        public void setStatutReponse(String statutReponse) { this.statutReponse = statutReponse; }
    }
