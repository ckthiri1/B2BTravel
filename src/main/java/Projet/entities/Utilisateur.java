package Projet.entities;

import jakarta.persistence.*;

public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IdUser;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private Integer nbrVoyage;
    private Roles role;

    @OneToOne(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private Fidelite fidelite;

    public Utilisateur() {

    }

    public enum Roles {
        Utilisateur,
        Administrateur
    }
    public Utilisateur(int nbrVoyage, String password, String email, String prenom, String nom, int idUser , Roles role) {
        this.nbrVoyage = nbrVoyage;
        this.password = password;
        this.email = email;
        this.prenom = prenom;
        this.nom = nom;
        IdUser = idUser;
        role = Roles.Administrateur;
    }

    public int getIdUser() {
        return IdUser;
    }

    public void setIdUser(int idUser) {
        IdUser = idUser;
    }

    public String getNom() {
        return nom;
    }

    public void setNbrVoyage(Integer nbrVoyage) {
        this.nbrVoyage = nbrVoyage;
    }

    public Fidelite getFidelite() {
        return fidelite;
    }

    public void setFidelite(Fidelite fidelite) {
        this.fidelite = fidelite;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getNbrVoyage() {
        return nbrVoyage;
    }

    public void setNbrVoyage(int nbrVoyage) {
        this.nbrVoyage = nbrVoyage;
    }

    // Method to increment the number of trips
    public void incrementNbrVoyage() {
        this.nbrVoyage = (this.nbrVoyage == null) ? 1 : this.nbrVoyage + 1;
    }
}
