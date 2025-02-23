package Projet.entities;


import jakarta.persistence.*;

public class reservation_voyage {



        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_resV")
        private int idResV;

        @ManyToOne
        @JoinColumn(name = "id_user", nullable = false)
        private User user;

        @ManyToOne
        @JoinColumn(name = "id_vol", nullable = false)
        private Vol vol;

        @Column(name = "place", nullable = false)
        private int place;

        @Column(name = "prixTotal", nullable = false)
        private double prixTotal;

        // Constructors
        public reservation_voyage() {
        }

        public reservation_voyage(User user, Vol vol, int place, double prixTotal) {
            this.user = user;
            this.vol = vol;
            this.place = place;
            this.prixTotal = prixTotal;
        }

        // Getters and Setters
        public int getIdResV() {
            return idResV;
        }

        public void setIdResV(int idResV) {
            this.idResV = idResV;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Vol getVol() {
            return vol;
        }

        public void setVol(Vol vol) {
            this.vol = vol;
        }

        public int getPlace() {
            return place;
        }

        public void setPlace(int place) {
            this.place = place;
        }

        public double getPrixTotal() {
            return prixTotal;
        }

        public void setPrixTotal(double prixTotal) {
            this.prixTotal = prixTotal;
        }

        // toString method for debugging
        @Override
        public String toString() {
            return "ReservationVoyage{" +
                    "idResV=" + idResV +
                    ", user=" + user +
                    ", vol=" + vol +
                    ", place=" + place +
                    ", prixTotal=" + prixTotal +
                    '}';
        }
    }
