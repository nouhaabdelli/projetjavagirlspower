package entities;

import java.sql.Timestamp;

public class Notifications {
    private int id;
    private int reclamationId;
    private String message;
    private Timestamp createdAt;
    private boolean seen;

    // Constructors
public Notifications() {

}

    public Notifications(int id, int reclamationId, String message, Timestamp createdAt, boolean seen) {
        this.id = id;
        this.reclamationId = reclamationId;
        this.message = message;
        this.createdAt = createdAt;
        this.seen = seen;
    }

    public Notifications(int reclamationId, String message, Timestamp createdAt, boolean seen) {
        this.reclamationId = reclamationId;
        this.message = message;
        this.createdAt = createdAt;
        this.seen = seen;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReclamationId() {
        return reclamationId;
    }

    public void setReclamationId(int reclamationId) {
        this.reclamationId = reclamationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }


    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", reclamationId=" + reclamationId +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                ", seen=" + seen +
                '}';
    }
}
