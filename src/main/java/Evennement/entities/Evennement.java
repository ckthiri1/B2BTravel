package Evennement.entities;

import java.util.Date;

public class Evennement {
    private int IDE;
    private String NomE;
    private String Local;
    private Date DateE;
    private String DesE;
    private Organisateur organisateur; // Reference to the Organisateur entity

    public Evennement(String nomE, String local, String desE, Date dateE, Organisateur organisateur) {
        if (organisateur == null || organisateur.getIDOr() == 0) {
            throw new IllegalArgumentException("Organisateur cannot be null or have ID 0");
        }   this.IDE = IDE;
        NomE = nomE;
        Local = local;
        DateE = dateE;
        DesE = desE;
        this.organisateur = organisateur;
    }


    public Evennement(int IDE, String nomE, String local, String desE, Date dateE, Organisateur organisateur) {
        if (organisateur == null || organisateur.getIDOr() == 0) {
            throw new IllegalArgumentException("Organisateur cannot be null or have ID 0");
        }   this.IDE = IDE;
        NomE = nomE;
        Local = local;
        DateE = dateE;
        DesE = desE;
        this.organisateur = organisateur;
    }

    public int getIDE() {
        return IDE;
    }

    public void setIDE(int IDE) {
        this.IDE = IDE;
    }

    public String getNomE() {
        return NomE;
    }

    public void setNomE(String nomE) {
        NomE = nomE;
    }

    public String getLocal() {
        return Local;
    }

    public void setLocal(String local) {
        Local = local;
    }

    public Date getDateE() {
        return DateE;
    }

    public void setDateE(Date dateE) {
        DateE = dateE;
    }

    public String getDesE() {
        return DesE;
    }

    public void setDesE(String desE) {
        DesE = desE;
    }

    public Organisateur getOrganisateur() {
        return organisateur;
    }

    public void setOrganisateur(Organisateur organisateur) {
        this.organisateur = organisateur;
    }

    public String getNomOr() {
        return organisateur != null ? organisateur.getNomOr() : null;
    }
    @Override
    public String toString() {
        return "Evennement{" +
                "IDE=" + IDE +
                ", NomE='" + NomE + '\'' +
                ", Local='" + Local + '\'' +
                ", DateE=" + DateE +
                ", DesE='" + DesE + '\'' +
                ", Organisateur=" + (organisateur != null ? organisateur.getIDOr() : "null") +
                '}';
    }
}
