package entities;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String cin;
    private String NumTelephone;
    private String Genre;
    private String Adresse;
    // Constructeur
    public User(int id, String nom, String prenom, String email, String cin , String NumTelephone, String Genre, String Adresse  ) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.cin = cin;
        this.NumTelephone = NumTelephone;
        this.Genre = Genre;
        this.Adresse = Adresse;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getCin() { return cin; }
    public String getNumTelephone() { return NumTelephone; }
    public String getGenre() { return Genre; }
    public String getAdresse() { return Adresse; }
    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setCin(String cin) { this.cin = cin; }
    public void setNumTelephone(String NumTelephone) { this.NumTelephone = NumTelephone; }
    public void setGenre(String Genre) { this.Genre = Genre; }
    public void setAdresse(String Adresse) { this.Adresse = Adresse; }

}
