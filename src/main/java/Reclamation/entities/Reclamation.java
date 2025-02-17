package Reclamation.entities;

import java.time.LocalDate;

public class Reclamation {
    private int IDR; // Identifiant de la réclamation
    private String Titre; // Titre de la réclamation
    private String Description; // Description de la réclamation
    private LocalDate DateR; // Date de la réclamation
    private String status; // Statut de la réclamation

    // Constructeur non paramétré (par défaut)
    public Reclamation() {
        this.status = "En attente"; // Statut par défaut
    }

    // Constructeur paramétré sans IDR (pour l'ajout d'une nouvelle réclamation)
    public Reclamation(String Titre, String Description, LocalDate DateR) {
        this.Titre = Titre;
        this.Description = Description;
        this.DateR = DateR;
        this.status = "En attente"; // Statut par défaut
    }

    // Constructeur paramétré avec IDR (pour la récupération depuis la base de données)
    public Reclamation(int IDR, String Titre, String Description, LocalDate DateR, String status) {
        this.IDR = IDR;
        this.Titre = Titre;
        this.Description = Description;
        this.DateR = DateR;
        this.status = status;
    }

    // Constructeur paramétré avec statut personnalisé (optionnel)
    public Reclamation(String Titre, String Description, LocalDate DateR, String status) {
        this.Titre = Titre;
        this.Description = Description;
        this.DateR = DateR;
        this.status = status;
    }

    // Getters et setters

    public int getIDR() {
        return IDR;
    }

    public void setIDR(int IDR) {
        this.IDR = IDR;
    }

    public String getTitre() {
        return Titre;
    }

    public void setTitre(String Titre) {
        this.Titre = Titre;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public LocalDate getDateR() {
        return DateR;
    }

    public void setDateR(LocalDate DateR) {
        this.DateR = DateR;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Méthode toString pour afficher les informations de la réclamation
    @Override
    public String toString() {
        return "Reclamation{" +
                "IDR=" + IDR +
                ", Titre='" + Titre + '\'' +
                ", Description='" + Description + '\'' +
                ", DateR=" + DateR +
                ", status='" + status + '\'' +
                '}';
    }
}