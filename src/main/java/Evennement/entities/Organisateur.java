package Evennement.entities;

public class Organisateur {
    private int IDOr;
    private String NomOr;
    private int Contact;



        // Constructor
        public Organisateur(int IDOr, String nomOr, int contact) {
            this.IDOr = IDOr;
            NomOr = nomOr;
            this.Contact = contact;
        }
        public Organisateur(String  NomOr, int contact) {
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

        public int getContact() {
            return Contact;
        }

        public void setContact(int contact) {
            this.Contact = contact;
        }

        @Override
        public String toString() {
            return NomOr; // Display the name in the ComboBox
        }
    }

