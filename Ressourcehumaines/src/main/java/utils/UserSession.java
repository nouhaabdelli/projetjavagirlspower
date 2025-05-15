package utils;

public class UserSession {
    private static UserSession instance;

    private String email;
    private String statut;
    private String token; // ex: JWT ou Firebase token

    private UserSession() {
        // Constructeur privé pour singleton
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setSessionData(String email, String statut, String token) {
        this.email = email;
        this.statut = statut;
        this.token = token;
    }

    public void clearSession() {
        email = null;
        statut = null;
        token = null;
    }

    // GETTERS
    public String getEmail() {
        return email;
    }

    public String getStatut() {
        return statut;
    }

    public String getToken() {
        return token;
    }

    public boolean isAuthenticated() {
        return email != null && token != null;
    }

    public boolean isStatut(String role) {
        return statut != null && statut.equalsIgnoreCase(role);
    }
    private String firebaseIdToken;
    private String firebaseRefreshToken;

    public String getFirebaseIdToken() {
        return firebaseIdToken;
    }

    public void setFirebaseIdToken(String firebaseIdToken) {
        this.firebaseIdToken = firebaseIdToken;
    }

    public String getFirebaseRefreshToken() {
        return firebaseRefreshToken;
    }

    public void setFirebaseRefreshToken(String firebaseRefreshToken) {
        this.firebaseRefreshToken = firebaseRefreshToken;
    }


}
