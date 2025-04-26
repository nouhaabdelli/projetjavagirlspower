package esprit.tn.models;

public class User {
    private String nom ,prenom ;
    private int age , id;

    public User(String nom, String prenom, int age, int id) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;

        this.id = id;

    }
    public User(){}
    public User(String nom, String prenom, int age) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;

    }

    public void setNom(String nom) {
        this.nom = nom;
    }



    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNom() {
        return nom;
    }


    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return "user{" +
                "age=" + age +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", id=" + id +
                '}';
    }
}
