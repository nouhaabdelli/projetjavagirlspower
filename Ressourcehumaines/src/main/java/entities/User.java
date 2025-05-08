package entities;

public class User {

    private int id;
    private String nom;
    private String prenom;
    private java.time.LocalDate dateNaissance;
    private String motDePasse;
    private String email;
    private String numTelephone;  //
    private String role;
    private String rib;
    private int nombreEnfant;
    private String cnam;
    private java.time.LocalDate dateEmbauche;
    private String photoProfil;
    private String statut;
    private String adresse;
    private String genre;
    private String situationFamiliale;
    private int cin;


    // 🔹 Constructeur vide
    public User() {
    }

    // 🔹 Constructeur complet
    public User(int id, String nom, String prenom, java.time.LocalDate dateNaissance, String motDePasse,
                String email, String numTelephone, String role, String rib, int nombreEnfant, String cnam,
                java.time.LocalDate dateEmbauche, String photoProfil, String statut, String adresse,
                String genre, String situationFamiliale, int cin) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.motDePasse = motDePasse;
        this.email = email;
        this.numTelephone = numTelephone;
        this.role = role;
        this.rib = rib;
        this.nombreEnfant = nombreEnfant;
        this.cnam = cnam;
        this.dateEmbauche = dateEmbauche;
        this.photoProfil = photoProfil;
        this.statut = statut;
        this.adresse = adresse;
        this.genre = genre;
        this.situationFamiliale = situationFamiliale;
        this.cin = cin;
    }

    // 🔹 Getters et Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public java.time.LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(java.time.LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTelephone() {
        return numTelephone;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public int getNombreEnfant() {
        return nombreEnfant;
    }

    public void setNombreEnfant(int nombreEnfant) {
        this.nombreEnfant = nombreEnfant;
    }

    public String getCnam() {
        return cnam;
    }

    public void setCnam(String cnam) {
        this.cnam = cnam;
    }

    public java.time.LocalDate getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(java.time.LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public String getPhotoProfil() {
        return photoProfil;
    }

    public void setPhotoProfil(String photoProfil) {
        this.photoProfil = photoProfil;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSituationFamiliale() {
        return situationFamiliale;
    }

    public void setSituationFamiliale(String situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }
    public int getCin() {return cin;}
    public void setCin(int cin) {this.cin = cin;}


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", statut='" + statut + '\'' +
                ", cin=" + cin +
                '}';
    }
}

