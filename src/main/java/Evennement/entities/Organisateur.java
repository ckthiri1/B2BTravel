package Evennement.entities;

public class Organisateur {
    private int IDOr;
    private String NomOr;
    private int Contact;
    public Organisateur() {}
    public Organisateur(String NomOr, int Contact) {
        this.IDOr = IDOr;
        this.NomOr = NomOr;
        this.Contact = Contact;
    }
    public int getIDOr() {
        return IDOr;
    }
    public void setIDOr(int IDOr) {
        this.IDOr = IDOr;
    }
    public String getNomOr() {
        return NomOr;
    }
    public void setNomOr(String NomOr) {
        this.NomOr = NomOr;
    }
    public int getContact() {
        return Contact;
    }
    public void setContact(int Contact) {
        this.Contact = Contact;
    }

    @Override
    public String toString() {
        return "Organisateur{" +
                "IDOr=" + IDOr +
                ", NomOr='" + NomOr + '\'' +
                ", Contact=" + Contact +
                '}';
    }


}
