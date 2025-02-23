package Evennement.entities;

import java.util.Date;

public class Evennement {
    private int IDE;
    private String NomE;
    private String Local;
    private Date DateE;
    private String DesE;
    private Organisateur organisateur;
    private EventType event_type; // Added event type attribute

    // Existing constructor (without event type)
    public Evennement(String nomE, String local, String desE, Date dateE, Organisateur organisateur) {
        if (organisateur == null || organisateur.getIDOr() == 0) {
            throw new IllegalArgumentException("Organisateur cannot be null or have ID 0");
        }
        this.IDE = IDE;
        NomE = nomE;
        Local = local;
        DateE = dateE;
        DesE = desE;
        this.organisateur = organisateur;
    }

    // Existing constructor (without event type)
    public Evennement(int IDE, String nomE, String local, String desE, Date dateE, Organisateur organisateur) {
        if (organisateur == null || organisateur.getIDOr() == 0) {
            throw new IllegalArgumentException("Organisateur cannot be null or have ID 0");
        }
        this.IDE = IDE;
        NomE = nomE;
        Local = local;
        DateE = dateE;
        DesE = desE;
        this.organisateur = organisateur;
    }

    // New Constructor with event type
    public Evennement(int IDE, String nomE, String local, String desE, Date dateE, Organisateur organisateur, EventType event_type) {
        if (organisateur == null || organisateur.getIDOr() == 0) {
            throw new IllegalArgumentException("Organisateur cannot be null or have ID 0");
        }
        this.IDE = IDE;
        NomE = nomE;
        Local = local;
        DateE = dateE;
        DesE = desE;
        this.organisateur = organisateur;
        this.event_type = event_type; // Assign event type
    }
    public Evennement(String nomE, String local, String desE, Date dateE, Organisateur organisateur, EventType eventType) {
        this.NomE = nomE;
        this.Local = local;
        this.DateE = dateE;
        this.DesE = desE;
        this.organisateur = organisateur;
        this.eventType = eventType; // Ensure this is correctly set
        System.out.println("EventType set in Evennement: " + eventType); // Debugging log
    }

    public EventType getEventType() {
        System.out.println("Getting eventType: " + eventType); // Debugging log
        return eventType;
    }

    public void setEventType(EventType event_type) {
        this.event_type = event_type;
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

    private EventType eventType;

    // Getter for eventType


    @Override
    public String toString() {
        return "Evennement{" +
                "IDE=" + IDE +
                ", NomE='" + NomE + '\'' +
                ", Local='" + Local + '\'' +
                ", DateE=" + DateE +
                ", DesE='" + DesE + '\'' +
                ", Organisateur=" + (organisateur != null ? organisateur.getIDOr() : "null") +
                ", EventType=" + (event_type != null ? event_type.name() : "null") + // Added event type
                '}';
    }
}
