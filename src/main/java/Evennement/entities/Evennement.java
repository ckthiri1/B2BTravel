package Evennement.entities;

import java.util.Date;

public class Evennement {
    private int IDE;
    private String NomE;
    private String Local;
    private Date DateE;
    private String DesE;

    public Evennement(String nomE, String local, String desE, java.util.Date dateE) {
        this.IDE = IDE;
        NomE = nomE;
        Local = local;
        DesE = desE;
        this.DateE = dateE;
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

    @Override
    public String toString() {
        return "Evennement{" +
                "IDE=" + IDE +
                ", NomE='" + NomE + '\'' +
                ", Local='" + Local + '\'' +
                ", DateE=" + DateE +
                ", DesE='" + DesE + '\'' +
                '}';
    }

}
