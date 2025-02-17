package Reclamation.entities;

import java.time.LocalDate;

public class Reponse {
    private int IDRep;
    private String DescriptionRep;
    private LocalDate DateRep;
    private int IDR;  // Clé étrangère vers la réclamation

    // Constructeurs
    public Reponse() {}

    public Reponse(int IDRep, String DescriptionRep, LocalDate DateRep, int IDR) {
        this.IDRep = IDRep;
        this.DescriptionRep = DescriptionRep;
        this.DateRep = DateRep;
        this.IDR = IDR;
    }

    public Reponse(String DescriptionRep, LocalDate DateRep, int IDR) {
        this.DescriptionRep = DescriptionRep;
        this.DateRep = DateRep;
        this.IDR = IDR;
    }

    // Getters & Setters
    public int getIDRep() {
        return IDRep;
    }

    public void setIDRep(int IDRep) {
        this.IDRep = IDRep;
    }

    public String getDescriptionRep() {
        return DescriptionRep;
    }

    public void setDescriptionRep(String DescriptionRep) {
        this.DescriptionRep = DescriptionRep;
    }

    public LocalDate getDateRep() {
        return DateRep;
    }

    public void setDateRep(LocalDate DateRep) {
        this.DateRep = DateRep;
    }

    public int getIDR() {
        return IDR;
    }

    public void setIDR(int IDR) {
        this.IDR = IDR;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "IDRep=" + IDRep +
                ", DescriptionRep='" + DescriptionRep + '\'' +
                ", DateRep=" + DateRep +
                ", IDR=" + IDR +
                '}';
    }
}
