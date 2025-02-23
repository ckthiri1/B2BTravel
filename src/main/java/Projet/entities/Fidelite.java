package Projet.entities;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public class Fidelite {

    private int IDF;
    private int points;
    private double remise;


    @OneToOne
    @JoinColumn(name = "idUser")
    private User user_id;

    @OneToOne
    @JoinColumn(name = "IdRank")
    private Rank IdRank;

    // Constructor
    public Fidelite(int IDF, int points, double remise, Rank IdRank , User user_id) {
        this.IDF = IDF;
        this.points = points;
        this.remise = remise;
        this.IdRank = IdRank;
        this.user_id = user_id;
    }

    // Getters and Setters
    public int getIDF() {
        return IDF;
    }

    public void setIDF(int IDF) {
        this.IDF = IDF;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getRemise() {
        return remise;
    }

    public void setRemise(double remise) {
        this.remise = remise;
    }

    public Rank getRang() {
        return IdRank;
    }

    public void setRang(Rank rank) {
        this.IdRank = rank;
    }

    @Override
    public String toString() {
        return "Fidelite{" +
                "IDF=" + IDF +
                ", points=" + points +
                ", remise=" + remise +
                ", rang=" + IdRank.getNomRank() +
                '}';
    }
}
