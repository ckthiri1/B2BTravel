package CRUD.entities;

public class Hebergement {
    private int id_hebergement;
    private String nom;
    private String adresse;
    private String type;
    private String description;

    public Hebergement(String nom, String adresse, String type, String description) {
        this.nom = nom;
        this.adresse = adresse;
        this.type = type;
        this.description = description;
    }

    public Hebergement(int id_hebergement, String nom, String adresse, String type, String description) {
        this.id_hebergement = id_hebergement;
        this.nom = nom;
        this.adresse = adresse;
        this.type = type;
        this.description = description;
    }

    public Hebergement(int id_hebergement, String nom) {
        this.id_hebergement = id_hebergement;
        this.nom = nom;
    }

    public Hebergement() {}

    public int getId_hebergement() {
        return id_hebergement;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setId_hebergement(int id_hebergement) {
        this.id_hebergement = id_hebergement;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Hebergement{" +
                "id_hebergement=" + id_hebergement +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
