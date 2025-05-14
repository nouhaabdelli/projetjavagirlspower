package entities;

public class user {
    // Attributs
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String cin;
    private String numTelephone;
    private String genre;
    private String adresse;

    // Constructeur vide
    public user() {}

    // Constructeur avec paramètres
    public user(int id, String nom, String prenom, String email, String cin, String numTelephone, String genre, String adresse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.cin = cin;
        this.numTelephone = numTelephone;
        this.genre = genre;
        this.adresse = adresse;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getCin() {
        return cin;
    }

    public String getNumTelephone() {
        return numTelephone;
    }

    public String getGenre() {
        return genre;
    }

    public String getAdresse() {
        return adresse;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", cin='" + cin + '\'' +
                ", numTelephone='" + numTelephone + '\'' +
                ", genre='" + genre + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}
