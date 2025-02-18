package CRUD.entities;
import java.time.LocalDateTime;

public class Reservation {
    private int id_resH;
    private LocalDateTime date ;
    private int prix;
    private String status;
    private int idH;
    private String hebergementName; // New field to hold Hebergement name


    public Reservation() {}

    public Reservation(int id_resH, LocalDateTime date, int prix, String status, int idH) {
        this.id_resH = id_resH;
        this.date = date;
        this.prix = prix;
        this.status = status;
        this.idH = idH;
    }

    public Reservation(LocalDateTime date, int prix, String status, int idH) {
        this.date = date;
        this.prix = prix;
        this.status = status;
        this.idH = idH;
    }


    public String getHebergementName() {
        return hebergementName;
    }

    public void setHebergementName(String hebergementName) {
        this.hebergementName = hebergementName;
    }

    public int getId_reservation() {
        return id_resH;
    }

    public void setId_reservation(int id_resH) {
        this.id_resH = id_resH;
    }

    public LocalDateTime getDate_reservation() {
        return date;
    }

    public void setDate_reservation(LocalDateTime date) {
        this.date = date;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getHebergement_id() {
        return idH;
    }

    public void setHebergement_id(int idH) {
        this.idH = idH;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id_reservation=" + id_resH +
                ", date_reservation=" + date +
                ", prix=" + prix +
                ", status='" + status + '\'' +
                ", hebergement_id=" + idH +
                '}';
    }
}
