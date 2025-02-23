package Projet.entities;

import jakarta.persistence.*;

import java.util.Date;

public class Vol {
    public enum TypeVol {
        Aller, Aller_Retour
    }

    public enum StatusVol {
        NON_RESERVER, RESERVER
    }
    private int volID;
    private Date dateDepart;
    private Date dateArrival;
    private String airLine;
    private int flightNumber;
    private String dureeVol;
    private int prixVol;
    private TypeVol typeVol;
    private StatusVol status; // ✅ Added Status Enum

    @ManyToOne
    @JoinColumn(name = "voyage_id", nullable = false)
    private Voyage voyage;

    private User user;

    public int getVolID() {
        return volID;
    }

    public void setVolID(int volID) {
        this.volID = volID;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Date getDateArrival() {
        return dateArrival;
    }

    public void setDateArrival(Date dateArrival) {
        this.dateArrival = dateArrival;
    }

    public String getAirLine() {
        return airLine;
    }

    public void setAirLine(String airLine) {
        this.airLine = airLine;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDureeVol() {
        return dureeVol;
    }

    public void setDureeVol(String dureeVol) {
        this.dureeVol = dureeVol;
    }

    public int getPrixVol() {
        return prixVol;
    }

    public void setPrixVol(int prixVol) {
        this.prixVol = prixVol;
    }

    public TypeVol getTypeVol() {
        return typeVol;
    }

    public void setTypeVol(TypeVol typeVol) {
        this.typeVol = typeVol;
    }

    public Voyage getVoyage() {
        return voyage;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vol(int volID, Date dateDepart, Date dateArrival, String airLine, int flightNumber,
               String dureeVol, int prixVol, TypeVol typeVol, Voyage voyage, User user, StatusVol nonReserver) {
        this.volID = volID;
        this.dateDepart = dateDepart;
        this.dateArrival = dateArrival;
        this.airLine = airLine;
        this.flightNumber = flightNumber;
        this.dureeVol = dureeVol;
        this.prixVol = prixVol;
        this.typeVol = typeVol;
        this.voyage = voyage;
        this.user = user;
        this.status = StatusVol.NON_RESERVER;
    }

    // Getters & Setters
    public StatusVol getStatus() { return status; }
    public void setStatus(StatusVol status) { this.status = status; }

    @Override
    public String toString() {
        return "Vol{" +
                "volID=" + volID +
                ", dateDepart=" + dateDepart +
                ", dateArrival=" + dateArrival +
                ", airLine='" + airLine + '\'' +
                ", flightNumber=" + flightNumber +
                ", dureeVol='" + dureeVol + '\'' +
                ", prixVol=" + prixVol +
                ", typeVol=" + typeVol +
                ", status=" + status +  // ✅ Display status
                ", voyageID=" + (voyage != null ? voyage.getVID() : "null") +
                ", userID=" + (user != null ? user.getUser_id() : "null") +
                '}';
    }
}
