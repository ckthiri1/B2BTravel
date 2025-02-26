package Projet.entities;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;

import java.util.List;
import java.util.Objects;

public class User {

    private int user_id;
    private String nom;
    private String prenom;
    private String email;
    private String pwd;
    private Integer nbrVoyage =0;
    private String role;
    private String image_url;
    @Convert(converter = VoiceFeaturesConverter.class)
    private List<List<Double>> voiceFeatures; // List of voice samples

    public List<List<Double>> getVoiceFeatures() {
        return voiceFeatures;
    }
    @Converter
    public static class VoiceFeaturesConverter implements AttributeConverter<List<List<Double>>, String> {
        private static final Gson gson = new Gson();

        @Override
        public String convertToDatabaseColumn(List<List<Double>> attribute) {
            return gson.toJson(attribute);
        }

        @Override
        public List<List<Double>> convertToEntityAttribute(String dbData) {
            return gson.fromJson(dbData, new TypeToken<List<List<Double>>>(){}.getType());
        }
    }

    public enum Roles {
        user,
        admin
    }


    public void setVoiceFeatures(List<List<Double>> voiceFeatures) {
        this.voiceFeatures = voiceFeatures;
    }

    public User(){}

    public User(String nom, String prenom, String email, String pwd, int user_id, int nbr_voyage, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.pwd = pwd;
        this.user_id = user_id;
        this.nbrVoyage = nbrVoyage;
        this.role = role;
    }




    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public User(String nom, String prenom, String email, String pwd, int nbrVoyage, String role, String image_url) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.pwd = pwd;
        this.nbrVoyage = nbrVoyage;
        this.role = role;
        this.image_url = image_url;
    }

    public User(String nom, String prenom, String email, String pwd, int nbrVoyage, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.pwd = pwd;
        this.nbrVoyage = nbrVoyage;
        this.role = role;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public Integer getNbrVoyage() {
        return nbrVoyage;
    }

    public void setNbrVoyage(Integer nbrVoyage) {
        this.nbrVoyage = nbrVoyage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", pwd='" + pwd + '\'' +
                ", nbr_voyage=" + nbrVoyage +
                ", role='" + role + '\'' +
                '}';
    }
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + this.user_id;
        hash = 61 * hash + Objects.hashCode(this.nom);
        return hash;
    }
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            User other = (User)obj;
            if (this.user_id != other.user_id) {
                return false;
            } else {
                return Objects.equals(this.nom, other.nom);
            }
        }
    }

    // Method to increment the number of trips
    public void incrementNbrVoyage() {
        this.nbrVoyage = (this.nbrVoyage == null) ? 1 : this.nbrVoyage + 1;
    }
}


