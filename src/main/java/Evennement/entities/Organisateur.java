package Evennement.entities;

public class Organisateur {
    private int IDOr;
    private String NomOr;
    private String Contact;



        // Constructor
        public Organisateur(int IDOr, String nomOr, String contact) {
            this.IDOr = IDOr;
            NomOr = nomOr;
            this.Contact = contact;
        }
        public Organisateur(String  NomOr, String contact) {
            this.NomOr = NomOr;
            this.Contact = contact;

        }

        // Getters and Setters
        public int getIDOr() {
            return IDOr;
        }

        public void setIDOr(int IDOr) {
            this.IDOr = IDOr;
        }

        public String getNomOr() {
            return NomOr;
        }

        public void setNomOr(String nomOr) {
            this.NomOr = nomOr;
        }

        public String getContact() {
            return Contact;
        }

        public void setContact(String contact) {
            this.Contact = contact;
        }

        @Override
        public String toString() {
            return NomOr; // Display the name in the ComboBox
        }
    }

