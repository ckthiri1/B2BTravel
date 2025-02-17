package Projet.entities;

public class Rank {

        private int IDRang;
        private  String NomRank;
        private String description;
        private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Rank(int IDRang, String NomRank,  int points ) {
            this.IDRang = IDRang;
            this.NomRank = NomRank;
            this.points = points;
        }

    public Rank() {

    }

    public Integer getIDRang() {
            return IDRang;
        }

        public void setIDRang(int IDRang) {
            this.IDRang = IDRang;
        }

        public String getNomRank() {
            return NomRank;
        }

        public void setNomRank(String NomRank) {
            this.NomRank = NomRank;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "Rang{" +
                    "IDRang=" + IDRang +
                    ", NomRank=" + NomRank +
                    ", description='" + description + '\'' +
                    '}';
        }
    }


