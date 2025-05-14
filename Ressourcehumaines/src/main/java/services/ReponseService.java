package services;

import entities.ReponseDemande;
import entities.Demande;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReponseService {

    private List<ReponseDemande> reponses;

    public ReponseService() {
        this.reponses = new ArrayList<>();
    }

    // Ajouter une réponse
    public void ajouterReponse(String statut, LocalDate dateValidation, Demande demande) {
        ReponseDemande reponse = new ReponseDemande(statut, dateValidation, demande);
        reponses.add(reponse);
        System.out.println("Réponse ajoutée : " + reponse);
    }

    // Obtenir toutes les réponses
    public List<ReponseDemande> getAllReponses() {
        return reponses;
    }

    // Obtenir une réponse par son ID
    public ReponseDemande getReponseById(int id) {
        for (ReponseDemande reponse : reponses) {
            if (reponse.getId() == id) {
                return reponse;
            }
        }
        return null;  // Retourne null si la réponse n'est pas trouvée
    }

    // Mettre à jour une réponse
    public void updateReponse(int id, String statut, LocalDate dateValidation) {
        ReponseDemande reponse = getReponseById(id);
        if (reponse != null) {
            reponse.setStatut(statut);
            reponse.setDateValidation(dateValidation);
            System.out.println("Réponse mise à jour : " + reponse);
        } else {
            System.out.println("Réponse avec ID " + id + " non trouvée.");
        }
    }

    // Supprimer une réponse
    public void deleteReponse(int id) {
        ReponseDemande reponse = getReponseById(id);
        if (reponse != null) {
            reponses.remove(reponse);
            System.out.println("Réponse supprimée : " + reponse);
        } else {
            System.out.println("Réponse avec ID " + id + " non trouvée.");
        }
    }

    // Obtenir les réponses par la demande associée
    public List<ReponseDemande> getReponsesByDemande(Demande demande) {
        List<ReponseDemande> reponsesPourDemande = new ArrayList<>();
        for (ReponseDemande reponse : reponses) {
            if (reponse.getDemande().getId() == demande.getId()) {
                reponsesPourDemande.add(reponse);
            }
        }
        return reponsesPourDemande;
    }
}