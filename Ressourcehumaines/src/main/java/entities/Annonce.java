package entities;

    import java.time.LocalDateTime;

    public class Annonce {

        private int idAnnonce;
        private String titre;
        private String contenu;
        private LocalDateTime datePublication;
        private String pieceJointe; // Peut être null


        public Annonce(String titre, String contenu, LocalDateTime datePublication, String pieceJointe) {
            this.titre = titre;
            this.contenu = contenu;
            this.datePublication = datePublication;
            this.pieceJointe = pieceJointe;
        }


        public Annonce(int idAnnonce, String titre, String contenu, LocalDateTime datePublication, String pieceJointe) {
            this.idAnnonce = idAnnonce;
            this.titre = titre;
            this.contenu = contenu;
            this.datePublication = datePublication;
            this.pieceJointe = pieceJointe;
        }

        public int getIdAnnonce() {
            return idAnnonce;
        }

        public void setIdAnnonce(int idAnnonce) {
            this.idAnnonce = idAnnonce;
        }

        public String getTitre() {
            return titre;
        }

        public void setTitre(String titre) {
            this.titre = titre;
        }

        public String getContenu() {
            return contenu;
        }

        public void setContenu(String contenu) {
            this.contenu = contenu;
        }

        public LocalDateTime getDatePublication() {
            return datePublication;
        }

        public void setDatePublication(LocalDateTime datePublication) {
            this.datePublication = datePublication;
        }

        public String getPieceJointe() {
            return pieceJointe;
        }

        public void setPieceJointe(String pieceJointe) {
            this.pieceJointe = pieceJointe;
        }

        @Override
        public String toString() {
            return "Annonce{" +
                    "idAnnonce=" + idAnnonce +
                    ", titre='" + titre + '\'' +
                    ", contenu='" + contenu + '\'' +
                    ", datePublication=" + datePublication +
                    ", pieceJointe='" + pieceJointe + '\'' +
                    '}';
        }
    }


