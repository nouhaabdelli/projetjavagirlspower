package utils;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class StatistiqueModel {
    private LocalDateTime dateHeure;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private int dureeMinutes;
    private String actions;

    public StatistiqueModel(LocalDateTime dateHeure, LocalTime heureDebut, LocalTime heureFin, int dureeMinutes, String actions) {
        this.dateHeure = dateHeure;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.dureeMinutes = dureeMinutes;
        this.actions = actions;
    }

    // Getters
    public LocalDateTime getDateHeure() { return dateHeure; }
    public LocalTime getHeureDebut() { return heureDebut; }
    public LocalTime getHeureFin() { return heureFin; }
    public int getDureeMinutes() { return dureeMinutes; }
    public String getActions() { return actions; }

    // Setters
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure = dateHeure; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }
    public void setDureeMinutes(int dureeMinutes) { this.dureeMinutes = dureeMinutes; }
    public void setActions(String actions) { this.actions = actions; }
} 