package Projet.entities;

import java.util.ArrayList;
import java.util.List;

public class Voyage {

    private int VID;
    private String depart;
    private String destination;
    private String description;
    private List<Vol> vols;

    // Constructeur
    public Voyage(int VID, String depart, String destination, String description) {
        this.VID = VID;
        this.depart = depart;
        this.destination = destination;
        this.description = description;
        this.vols = new ArrayList<>();
    }

    public Voyage() {

    }

    // Getters et Setters
    public int getVID() {
        return VID;
    }

    public void setVID(int VID) {
        this.VID = VID;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Vol> getVols() {
        return vols;
    }

    // Méthode pour ajouter un vol au voyage
    public void addVol(Vol vol) {
        vols.add(vol);
        vol.setVoyage(this);
    }

    @Override
    public String toString() {
        return "Voyage{" +
                "VID=" + VID +
                ", depart='" + depart + '\'' +
                ", destination='" + destination + '\'' +
                ", description='" + description + '\'' +
                ", vols=" + vols +
                '}';
    }
}
